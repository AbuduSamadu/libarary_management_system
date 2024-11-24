package abudu.lms.library.models;

public class BookLinkedList {
    private BookNode head;

    public BookLinkedList() {
        this.head = null;
    }

    public void addBook(Book book) {
        BookNode newNode = new BookNode(book);
        if (head == null) {
            head = newNode;
        } else {
            BookNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public void removeBook(int bookId) {
        if (head == null) return;

        if (head.getBook().getId() == bookId) {
            head = head.getNext();
            return;
        }

        BookNode current = head;
        while (current.getNext() != null && current.getNext().getBook().getId() != bookId) {
            current = current.getNext();
        }

        if (current.getNext() != null) {
            current.setNext(current.getNext().getNext());
        }
    }

    public Book findBookById(int bookId) {
        BookNode current = head;
        while (current != null) {
            if (current.getBook().getId() == bookId) {
                return current.getBook();
            }
            current = current.getNext();
        }
        return null;
    }

    public void printBooks() {
        BookNode current = head;
        while (current != null) {
            System.out.println(current.getBook().getTitle());
            current = current.getNext();
        }
    }

    public BookNode getHead() {
        return head;
    }
}