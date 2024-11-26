package abudu.lms.library.services;


import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.repository.BookRepository;

public class DashboardService {
    private final UserDataHandler userRepository;
    private final BookRepository bookRepository;

    public DashboardService(UserDataHandler userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public int getTotalUsers() {
        return userRepository.countUsers();
    }

    public int getTotalBooks() {
        return bookRepository.countBooks();
    }
}