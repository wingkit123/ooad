import facade.RentalSystemFacade;
import model.Equipment;
import model.RentalRecord;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================================");
        System.out.println("          Smart Equipment Rental System - Comprehensive QA Run           ");
        System.out.println("==========================================================================");

        try {
            testAuthenticationFlow();
            testEquipmentManagement();
            
            // Run matrix test for all combinations of Roles, Equipment Categories, and Return Scenarios
            testMatrix();

            System.out.println("\n==========================================================================");
            System.out.printf("Comprehensive QA Summary: %d/%d tests passed.%n", passedTests, totalTests);
            System.out.println("==========================================================================");
        } catch (Exception e) {
            System.out.println("\n[ERROR] An unexpected error occurred during testing:");
            e.printStackTrace();
        }
    }

    private static void assertEquals(String message, Object expected, Object actual) {
        totalTests++;
        if (expected == null && actual == null) {
            passedTests++;
            System.out.println("  [PASS] " + message);
        } else if (expected != null && expected.equals(actual)) {
            passedTests++;
            System.out.println("  [PASS] " + message);
        } else {
            System.err.println("  [FAIL] " + message);
            System.err.println("         Expected: " + expected);
            System.err.println("         Actual:   " + actual);
        }
    }

    private static void assertTrue(String message, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("  [PASS] " + message);
        } else {
            System.err.println("  [FAIL] " + message);
        }
    }

    private static void testAuthenticationFlow() {
        System.out.println("\n--- 1. Testing Authentication Flow ---");
        RentalSystemFacade facade = new RentalSystemFacade();

        String res1 = facade.userLogin("S101", "Wing Kit", "student");
        assertEquals("New student registration", "Success", res1);
        assertEquals("Current user ID", "S101", facade.getCurrentUser().getUserId());
        assertEquals("User type is STUDENT", User.UserType.STUDENT, facade.getCurrentUser().getType());

        facade.logout();
        assertTrue("Session cleared after logout", facade.getCurrentUser() == null);
        
        String res2 = facade.userLogin("S101", "Wrong Name", "student");
        assertTrue("Re-login with mismatched name should fail", res2.startsWith("Error"));

        String res3 = facade.userLogin("S101", "Wing Kit", "student");
        assertEquals("Re-login with correct name should succeed", "Success", res3);

        boolean adminRes = facade.adminLogin("admin123", "admin123");
        assertTrue("Admin login with correct credentials", adminRes);

        boolean adminFail = facade.adminLogin("admin123", "wrong_pass");
        assertTrue("Admin login with incorrect credentials should fail", !adminFail);
    }

    private static void testEquipmentManagement() {
        System.out.println("\n--- 2. Testing Equipment Management (Admin Actions) ---");
        RentalSystemFacade facade = new RentalSystemFacade();
        facade.adminLogin("admin123", "admin123");

        int initialSize = facade.getAllEquipment().size();
        assertEquals("Seeded equipment count is 6", 6, initialSize);

        facade.addEquipment("E103", "HP EliteBook", "Electronics", 30.00);
        assertEquals("Total equipment size is 7", 7, facade.getAllEquipment().size());

        facade.updateEquipment("E103", "HP EliteBook Pro", 32.00, "MAINTENANCE");
        Equipment updated = facade.getAllEquipment().stream()
                .filter(e -> e.getEquipmentId().equals("E103"))
                .findFirst().orElse(null);
        
        assertTrue("Equipment name updated to 'HP EliteBook Pro'", updated != null && "HP EliteBook Pro".equals(updated.getName()));
        assertEquals("Equipment status updated to MAINTENANCE", Equipment.EquipmentStatus.MAINTENANCE, updated.getStatus());
        assertTrue("Equipment in MAINTENANCE is restricted from rental", !updated.isAvailable());

        facade.removeEquipment("E103");
        assertEquals("Equipment size returns to 6", 6, facade.getAllEquipment().size());
    }

    private static void testMatrix() {
        System.out.println("\n--- 3. Testing QA Scenario Matrix (Roles x Categories x Return Conditions) ---");

        // Roles to test
        String[] roles = {"student", "staff", "final year student"};
        
        // Equipment to test: ID, Category, Rate
        String[] eqIds = {"E101", "M201", "L301"}; // Dell XPS Laptop ($35), Canon DSLR Camera ($45), Digital Oscilloscope ($60)
        
        for (String role : roles) {
            for (String eqId : eqIds) {
                // 1. On-Time Return, Undamaged
                runTestCase(role, eqId, 5, 5, false);
                
                // 2. Late Return, Undamaged
                runTestCase(role, eqId, 5, 7, false);
                
                // 3. On-Time Return, Damaged
                runTestCase(role, eqId, 5, 5, true);
            }
        }
    }

    private static void runTestCase(String roleName, String equipmentId, int plannedDays, int actualDays, boolean damaged) {
        RentalSystemFacade facade = new RentalSystemFacade();
        
        // Login
        String userId = "USER_" + roleName.toUpperCase().replace(" ", "_");
        facade.userLogin(userId, "Test User", roleName);
        User user = facade.getCurrentUser();
        
        // Find equipment
        Equipment eq = facade.getAllEquipment().stream()
                .filter(e -> e.getEquipmentId().equals(equipmentId))
                .findFirst().orElse(null);
        
        if (eq == null) {
            System.err.println("Equipment not found: " + equipmentId);
            return;
        }

        // Calculate expected upfront payment
        double baseFee = eq.calculateBaseFee(plannedDays);
        double discount = 0.0;
        if (user.getType() == User.UserType.STAFF) {
            discount = baseFee * 0.20;
        } else if (user.getType() == User.UserType.FINAL_YEAR_STUDENT) {
            discount = baseFee * 0.10;
        }
        double upfrontPaid = baseFee - discount + 50.00; // includes $50 deposit

        // Perform Checkout
        String checkoutResult = facade.rentEquipmentList(Arrays.asList(equipmentId), plannedDays);
        
        // Calculate expected penalties
        int lateDays = Math.max(0, actualDays - plannedDays);
        double expectedPenalty = eq.calculatePenalty(lateDays, damaged);
        double expectedNetSettlement = 50.00 - expectedPenalty; // positive is refund, negative is balance due

        // Perform Return
        RentalRecord record = facade.getCurrentUserActiveRentals().get(0);
        String returnResult = facade.returnEquipment(record.getRecordId(), actualDays, damaged);

        // Assertions
        String scenarioLabel = String.format("Role: %-20s | Item: %-4s | Planned: %d | Actual: %d | Damaged: %-5s", 
                roleName.toUpperCase(), equipmentId, plannedDays, actualDays, String.valueOf(damaged));
        
        System.out.println("\nScenario: " + scenarioLabel);
        
        // Check upfront payment in result
        String expectedUpfrontStr = String.format("Total Paid Immediately (Base Rate + Deposits): $%.2f", upfrontPaid);
        assertTrue("  Correct Upfront Fee calculated (" + upfrontPaid + ")", checkoutResult.contains(expectedUpfrontStr));
        
        // Check return outcome
        if (expectedNetSettlement >= 0) {
            String expectedRefundStr = String.format("GRAND TOTAL REFUND TO USER:  $%8.2f", expectedNetSettlement);
            assertTrue("  Correct refund calculated ($" + expectedNetSettlement + ")", returnResult.contains(expectedRefundStr));
        } else {
            String expectedInvoiceStr = String.format("GRAND TOTAL OUTSTANDING DUE: $%8.2f", Math.abs(expectedNetSettlement));
            assertTrue("  Correct outstanding invoice calculated ($" + Math.abs(expectedNetSettlement) + ")", returnResult.contains(expectedInvoiceStr));
        }
        
        // Check status update on damage
        if (damaged) {
            assertEquals("  Equipment marked DAMAGED", Equipment.EquipmentStatus.DAMAGED, eq.getStatus());
        } else {
            assertEquals("  Equipment marked AVAILABLE", Equipment.EquipmentStatus.AVAILABLE, eq.getStatus());
        }
    }
}
