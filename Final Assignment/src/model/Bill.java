package model;

public class Bill {
    private double baseFee;
    private double discountAmount;
    private double penaltyAmount;
    private double depositPaid;
    private double totalCharges;
    private double netSettlement; // positive represents refund, negative represents additional pay due

    // Context fields for transparent receipt generation
    private User user;
    private Equipment equipment;
    private int actualDurationDays;
    private int plannedDurationDays;
    private boolean isDamaged;

    public Bill(double baseFee, double discountAmount, double penaltyAmount, double depositPaid) {
        this.baseFee = baseFee;
        this.discountAmount = discountAmount;
        this.penaltyAmount = penaltyAmount;
        this.depositPaid = depositPaid;
        this.totalCharges = Math.max(0.0, baseFee - discountAmount + penaltyAmount);
        this.netSettlement = depositPaid - penaltyAmount;
    }

    public Bill(User user, Equipment equipment, int actualDurationDays, int plannedDurationDays, boolean isDamaged, 
                double baseFee, double discountAmount, double penaltyAmount, double depositPaid) {
        this.user = user;
        this.equipment = equipment;
        this.actualDurationDays = actualDurationDays;
        this.plannedDurationDays = plannedDurationDays;
        this.isDamaged = isDamaged;
        this.baseFee = baseFee;
        this.discountAmount = discountAmount;
        this.penaltyAmount = penaltyAmount;
        this.depositPaid = depositPaid;
        this.totalCharges = Math.max(0.0, baseFee - discountAmount + penaltyAmount);
        this.netSettlement = depositPaid - penaltyAmount;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public double getDepositPaid() {
        return depositPaid;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getNetSettlement() {
        return netSettlement;
    }

    public String generateDetailedReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("        RENTAL SETTLEMENT RECEIPT        \n");
        sb.append("=========================================\n");
        
        // Renter & Equipment Info
        if (user != null) {
            sb.append(String.format("Renter ID:   %s\n", user.getUserId()));
            sb.append(String.format("Renter Name: %s (%s)\n", user.getName(), user.getType()));
        }
        if (equipment != null) {
            sb.append(String.format("Equipment:   %s (%s)\n", equipment.getName(), equipment.getCategory()));
        }
        sb.append("-----------------------------------------\n");

        // 1. Base Rental Fee with details
        double dailyRate = (equipment != null) ? equipment.getDailyRentalRate() : 0.0;
        sb.append(String.format("Base Rental Fee:            $%8.2f\n", baseFee));
        sb.append(String.format("  (%d days × $%.2f/day)\n", plannedDurationDays, dailyRate));
        sb.append("\n");

        // 2. Discounts Applied with percentage
        sb.append("Discounts Applied:\n");
        double discountPct = 0.0;
        String discountType = "Student Discount";
        if (user != null) {
            if (user.getType() == User.UserType.STAFF) {
                discountPct = 20.0;
                discountType = "Staff Discount";
            } else if (user.getType() == User.UserType.FINAL_YEAR_STUDENT) {
                discountPct = 10.0;
                discountType = "Final Year Student Discount";
            }
        }
        sb.append(String.format("  %s (%.0f%%):       -$%8.2f\n", discountType, discountPct, discountAmount));
        sb.append("\n");

        // 3. Penalties / Additional Charges
        sb.append("Penalties / Additional Charges:\n");
        int lateDays = Math.max(0, actualDurationDays - plannedDurationDays);
        boolean hasPenalties = false;
        
        if (lateDays > 0) {
            double rateMultiplier = 1.5;
            if (equipment != null) {
                if ("Media Equipment".equalsIgnoreCase(equipment.getCategory())) {
                    rateMultiplier = 2.0;
                } else if ("Laboratory Equipment".equalsIgnoreCase(equipment.getCategory())) {
                    rateMultiplier = 2.5;
                }
            }
            double latePenaltyPerDay = dailyRate * rateMultiplier;
            double totalLatePenalty = lateDays * latePenaltyPerDay;
            sb.append(String.format("  Late Return Penalty (x%.1f rate):\n", rateMultiplier));
            sb.append(String.format("    %d late days × $%.2f/day: +$%8.2f\n", lateDays, latePenaltyPerDay, totalLatePenalty));
            hasPenalties = true;
        }
        if (isDamaged) {
            double damageFee = 150.0;
            if (equipment != null) {
                if ("Media Equipment".equalsIgnoreCase(equipment.getCategory())) {
                    damageFee = 200.0;
                } else if ("Laboratory Equipment".equalsIgnoreCase(equipment.getCategory())) {
                    damageFee = 300.0;
                }
            }
            sb.append(String.format("  Equipment Damage Fee:     +$%8.2f\n", damageFee));
            hasPenalties = true;
        }
        
        if (!hasPenalties) {
            sb.append("  No penalties or additional charges applied.\n");
        }
        sb.append("\n");

        // 4. Deposit
        sb.append("Deposit Details:\n");
        sb.append(String.format("  Security Deposit:\n"));
        sb.append(String.format("    1 item × $50.00 =        $%8.2f\n", depositPaid));
        sb.append("\n");

        // 5. Final Settlement
        sb.append("-----------------------------------------\n");
        sb.append("FINAL SETTLEMENT SUMMARY\n");
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Subtotal (Base Rental Fee):  $%8.2f\n", baseFee));
        sb.append(String.format("Total Discounts:            -$%8.2f\n", discountAmount));
        sb.append(String.format("Total Charges (Penalties):  +$%8.2f\n", penaltyAmount));
        sb.append(String.format("Security Deposit Paid:       $%8.2f\n", depositPaid));
        sb.append("-----------------------------------------\n");

        if (netSettlement >= 0) {
            sb.append(String.format("GRAND TOTAL REFUND TO USER:  $%8.2f\n", netSettlement));
            sb.append("=========================================\n");
            sb.append("   STATUS: CLOSED - DEPOSIT REFUNDED     \n");
        } else {
            sb.append(String.format("GRAND TOTAL OUTSTANDING DUE: $%8.2f\n", Math.abs(netSettlement)));
            sb.append("=========================================\n");
            sb.append("   STATUS: CLOSED - BALANCE PAID         \n");
        }
        sb.append("=========================================\n");
        return sb.toString();
    }
}
