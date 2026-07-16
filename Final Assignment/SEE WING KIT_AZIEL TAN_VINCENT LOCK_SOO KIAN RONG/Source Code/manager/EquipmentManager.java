package manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.Equipment;

/**
 * 1. Diagram (Class & Architecture)
 * - Class Role: Part of the Subsystem layer. Holds list of Equipment objects.
 * - Relationships: Composition of Equipment items in a List.
 * 
 * 2. System (System Flow)
 * - State: Tracks inventory collection.
 * - Persistence: Implements Serializable so its inventory list can be stored in system_data.dat.
 */
public class EquipmentManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Equipment> inventory;

    public EquipmentManager() {
        this.inventory = new ArrayList<>();
    }

    public void addEquipment(Equipment equipment) {
        inventory.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        inventory.remove(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return new ArrayList<>(inventory);
    }

    public List<Equipment> getAvailableEquipment() {
        List<Equipment> available = new ArrayList<>();
        for (Equipment eq : inventory) {
            if (eq.isAvailable()) {
                available.add(eq);
            }
        }
        return available;
    }

    public Optional<Equipment> findEquipmentById(String id) {
        return inventory.stream()
                .filter(eq -> eq.getEquipmentId().equalsIgnoreCase(id))
                .findFirst();
    }
}
