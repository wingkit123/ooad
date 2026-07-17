package manager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import model.Equipment;
import model.RentalRecord;
import model.User;

/**
 * 1. Diagram (Class & Architecture)
 * - Class Role: Part of the Subsystem layer. Manages RentalRecord transactions.
 * - Relationships: Direct association with User, Equipment, and RentalRecord.
 * 
 * 2. System (System Flow)
 * - State: Tracks active and historical rental logs.
 * - Persistence: Implements Serializable to save all transaction histories to system_data.dat.
 */
public class RentalManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<RentalRecord> records;

    /**
     * (Code Explanation - Constructor):
     * Initializes the empty rental records list.
     */
    public RentalManager() {
        this.records = new ArrayList<>();
    }

    /**
     * (Code Explanation - Create Rental):
     * Generates a unique transaction ID using UUID. Creates the RentalRecord object,
     * links the User to the Equipment, updates the Equipment's status to RENTED, 
     * and stores the record in the manager's list.
     */
    public RentalRecord createRental(User user, Equipment equipment, int durationDays, LocalDate rentDate, double depositPaid) {
        String recordId = "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        RentalRecord record = new RentalRecord(recordId, user, equipment, durationDays, rentDate, depositPaid);
        equipment.setStatus(Equipment.EquipmentStatus.RENTED);
        records.add(record);
        return record;
    }

    /**
     * (Code Explanation - Get All Records):
     * Returns a copy of the records list to preserve data integrity (Encapsulation).
     */
    public List<RentalRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    /**
     * (Code Explanation - Find Record By ID):
     * Utilizes Java 8 Streams to find a specific rental record by its unique ID.
     * Crucial for processing returns.
     */
    public Optional<RentalRecord> findRecordById(String recordId) {
        return records.stream()
                .filter(r -> r.getRecordId().equalsIgnoreCase(recordId))
                .findFirst();
    }

    /**
     * (Code Explanation - Get Active Records):
     * Filters the total history to only return records that are currently 'ACTIVE'.
     * Used by the GUI to display what items users currently have checked out.
     */
    public List<RentalRecord> getActiveRecords() {
        List<RentalRecord> active = new ArrayList<>();
        for (RentalRecord r : records) {
            if (r.getStatus() == RentalRecord.RentalStatus.ACTIVE) {
                active.add(r);
            }
        }
        return active;
    }
}
