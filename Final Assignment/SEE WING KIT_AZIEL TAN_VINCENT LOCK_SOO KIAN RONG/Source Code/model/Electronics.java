package model;

/**
 * (Code Explanation - Inheritance & Polymorphism):
 * Subclass of Equipment. Demonstrates inheritance by extending the base class.
 * Demonstrates polymorphism by overriding calculateBaseFee and calculatePenalty 
 * to provide specific business rules (e.g., 1.5x late rate and $150 damage fee) for Electronics.
 */
public class Electronics extends Equipment {
    private static final long serialVersionUID = 1L;

    public Electronics(String equipmentId, String name, double dailyRentalRate) {
        super(equipmentId, name, "Electronics", dailyRentalRate);
    }

    @Override
    public double calculateBaseFee(int days) {
        return getDailyRentalRate() * days;
    }

    @Override
    public double calculatePenalty(int lateDays, boolean isDamaged) {
        double penalty = 0;
        if (lateDays > 0) {
            penalty += getDailyRentalRate() * 1.5 * lateDays; // 1.5x daily rate per late day
        }
        if (isDamaged) {
            penalty += 150.0; // Flat damage fee for electronics
        }
        return penalty;
    }
}
