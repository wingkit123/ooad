package model;

import java.io.Serializable;

/**
 * (Code Explanation - Abstraction & Encapsulation):
 * Abstract base class that defines the blueprint for all equipment types.
 * Encapsulates core attributes (ID, name, status, rate) using private modifiers 
 * and provides public getters/setters. Declares abstract methods for pricing logic,
 * forcing subclasses to implement their own specific business rules.
 */
public abstract class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum EquipmentStatus implements Serializable {
        AVAILABLE,
        RENTED,
        MAINTENANCE,
        DAMAGED;

        private static final long serialVersionUID = 1L;
    }

    private String equipmentId;
    private String name;
    private String category;
    private double dailyRentalRate;
    private EquipmentStatus status;

    public Equipment(String equipmentId, String name, String category, double dailyRentalRate) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.category = category;
        this.dailyRentalRate = dailyRentalRate;
        this.status = EquipmentStatus.AVAILABLE;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public double getDailyRentalRate() {
        return dailyRentalRate;
    }

    public void setDailyRentalRate(double dailyRentalRate) {
        this.dailyRentalRate = dailyRentalRate;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return this.status == EquipmentStatus.AVAILABLE;
    }

    // Abstract methods to demonstrate abstraction and allow polymorphic pricing/penalties
    public abstract double calculateBaseFee(int days);
    public abstract double calculatePenalty(int lateDays, boolean isDamaged);

    @Override
    public String toString() {
        return name + " [" + category + "] - $" + dailyRentalRate + "/day (" + status + ")";
    }
}
