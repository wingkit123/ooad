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

    /**
     * (Code Explanation - Constructor):
     * Initializes the empty inventory list. 
     */
    public EquipmentManager() {
        this.inventory = new ArrayList<>();
    }

    public void addEquipment(Equipment equipment) {
        inventory.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        inventory.remove(equipment);
    }

    /**
     * (Code Explanation - Get All Equipment):
     * Returns a new ArrayList containing all inventory items to prevent external
     * classes from directly modifying the private internal list (Encapsulation).
     */
    public List<Equipment> getAllEquipment() {
        return new ArrayList<>(inventory);
    }

    /**
     * (Code Explanation - Get Available Equipment):
     * Iterates through the inventory and filters items based on their status.
     * This is used by the GUI to only display items that can currently be rented.
     */
    public List<Equipment> getAvailableEquipment() {
        List<Equipment> available = new ArrayList<>();
        for (Equipment eq : inventory) {
            if (eq.isAvailable()) {
                available.add(eq);
            }
        }
        return available;
    }

    /**
     * (Code Explanation - Find Equipment):
     * Uses Java 8 Streams and Optional to safely search for an item by its ID.
     * Helps avoid NullPointerExceptions if an item doesn't exist.
     */
    public Optional<Equipment> findEquipmentById(String id) {
        return inventory.stream()
                .filter(eq -> eq.getEquipmentId().equalsIgnoreCase(id))
                .findFirst();
    }
}
