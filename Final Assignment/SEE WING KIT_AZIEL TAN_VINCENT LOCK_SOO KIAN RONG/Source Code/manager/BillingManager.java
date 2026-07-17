package manager;

import model.Bill;
import model.Equipment;
import model.User;

/**
 * 1. Diagram (Class & Architecture)
 * - Class Role: Stateless subsystem manager. Computes rental rates, discounts, and penalties.
 * - Relationships: Works with User, Equipment, and returns a Bill object.
 * 
 * 2. System (System Flow)
 * - State: Stateless calculation helper.
 * - Logic: Applies user discounts (20% Staff, 10% Final Year Student) and handles late return penalties.
 */
public class BillingManager {
    /**
     * (Code Explanation - Calculate Bill):
     * The central pricing engine. It receives the raw parameters from the Facade, 
     * delegates base fee and penalty calculations to the polymorphic Equipment subclasses,
     * applies the business logic for User discounts, and generates the final Bill object.
     */
    public Bill calculateBill(User user, Equipment equipment, int actualDurationDays, int plannedDurationDays, boolean isDamaged, double depositPaid) {
        // 1. Calculate Base Fee (delegated to polymorphic equipment class)
        double baseFee = equipment.calculateBaseFee(plannedDurationDays);

        // 2. Calculate User Type Discounts
        double discountAmount = 0.0;
        if (user.getType() == User.UserType.STAFF) {
            discountAmount = baseFee * 0.20; // 20% discount for Staff
        } else if (user.getType() == User.UserType.FINAL_YEAR_STUDENT) {
            discountAmount = baseFee * 0.10; // 10% discount for Final Year Students
        }

        // 3. Calculate Penalties (delegated to polymorphic equipment class)
        int lateDays = Math.max(0, actualDurationDays - plannedDurationDays);
        double penaltyAmount = equipment.calculatePenalty(lateDays, isDamaged);

        // 4. Create and return the Bill
        return new Bill(user, equipment, actualDurationDays, plannedDurationDays, isDamaged, baseFee, discountAmount, penaltyAmount, depositPaid);
    }
}
