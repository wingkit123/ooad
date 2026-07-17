package model;

/**
 * (Code Explanation - Inheritance & Polymorphism):
 * Subclass of Equipment. Demonstrates inheritance by extending the base class.
 * Demonstrates polymorphism by overriding calculateBaseFee and calculatePenalty 
 * to provide specific business rules (e.g., 2.5x late rate and $300 damage fee) for Laboratory Equipment.
 */
public class LaboratoryEquipment extends Equipment {
    private static final long serialVersionUID = 1L;

    public LaboratoryEquipment(String equipmentId, String name, double dailyRentalRate) {
        super(equipmentId, name, "Laboratory Equipment", dailyRentalRate);
    }

    @Override
    public double calculateBaseFee(int days) {
        return getDailyRentalRate() * days;
    }

    @Override
    public double calculatePenalty(int lateDays, boolean isDamaged) {
        double penalty = 0;
        if (lateDays > 0) {
            penalty += getDailyRentalRate() * 2.5 * lateDays; // 2.5x daily rate per late day for lab equipment
        }
        if (isDamaged) {
            penalty += 300.0; // Higher flat damage fee for sensitive laboratory equipment
        }
        return penalty;
    }
}
