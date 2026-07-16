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

    public RentalManager() {
        this.records = new ArrayList<>();
    }

    public RentalRecord createRental(User user, Equipment equipment, int durationDays, LocalDate rentDate, double depositPaid) {
        String recordId = "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        RentalRecord record = new RentalRecord(recordId, user, equipment, durationDays, rentDate, depositPaid);
        equipment.setStatus(Equipment.EquipmentStatus.RENTED);
        records.add(record);
        return record;
    }

    public List<RentalRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public Optional<RentalRecord> findRecordById(String recordId) {
        return records.stream()
                .filter(r -> r.getRecordId().equalsIgnoreCase(recordId))
                .findFirst();
    }

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
