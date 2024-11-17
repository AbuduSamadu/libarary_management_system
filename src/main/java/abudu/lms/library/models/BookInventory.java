package abudu.lms.library.models;

import java.util.HashMap;
import java.util.Map;

public class BookInventory {
    private final Map<Book, Integer> inventory;

    public BookInventory() {
        this.inventory = new HashMap<>();
    }

    // Method to add a book to the inventory
    public void addBook(Book book, int quantity) {
        inventory.put(book, inventory.getOrDefault(book, 0) + quantity);
    }

    // Method to remove a book from the inventory
    public void removeBook(Book book, int quantity) {
        if (inventory.containsKey(book)) {
            int currentQuantity = inventory.get(book);
            if (currentQuantity <= quantity) {
                inventory.remove(book);
            } else {
                inventory.put(book, currentQuantity - quantity);
            }
        }
    }

    // Method to update the quantity of a book in the inventory
    public void updateBookQuantity(Book book, int quantity) {
        if (inventory.containsKey(book)) {
            inventory.put(book, quantity);
        }
    }

    // Method to get the quantity of a book in the inventory
    public int getBookQuantity(Book book) {
        return inventory.getOrDefault(book, 0);
    }

    // Method to check if a book is in the inventory
    public boolean isBookAvailable(Book book) {
        return inventory.containsKey(book) && inventory.get(book) > 0;
    }
}