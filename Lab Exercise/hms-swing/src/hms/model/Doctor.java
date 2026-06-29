package hms.model;

public class Doctor {
    private final int id;
    private String name;
    private String specialization;
    private String status;

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.status = "Available";
        update(name, specialization);
    }

    public void update(String name, String specialization) {
        update(name, specialization, this.status != null ? this.status : "Available");
    }

    public void update(String name, String specialization, String status) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Doctor name is required.");
        }
        if (name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Doctor name cannot contain numbers.");
        }
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization is required.");
        }
        if (specialization.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Specialization cannot contain numbers.");
        }
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required.");
        }
        this.name = name.trim();
        this.specialization = specialization.trim();
        this.status = status.trim();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + specialization + ") [" + status + "]";
    }
}
