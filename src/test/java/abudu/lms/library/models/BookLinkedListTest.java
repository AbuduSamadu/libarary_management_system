package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookLinkedListTest {
    private BookLinkedList bookLinkedList;

    @BeforeEach
    void setUp() {
        bookLinkedList = new BookLinkedList();
    }

    @Test
    void addBook() {
        bookLinkedList.addBook(new Book(1, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1));

    }

    @Test
    void removeBook() {
        bookLinkedList.removeBook(1);
    }

    @Test
    void findBookById() {
        bookLinkedList.findBookById(1);
        bookLinkedList.findBookById(2);
    }

    @Test
    void printBooks() {
        bookLinkedList.printBooks();
    }

    @Test
    void getHead() {
        bookLinkedList.getHead();
    }
}