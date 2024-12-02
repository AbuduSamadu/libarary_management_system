package abudu.lms.library.utils;

import abudu.lms.library.models.User;

import java.time.LocalDateTime;
import java.time.Duration;

public class UserSession {
    private static volatile UserSession instance;
    private User currentUser;
    private LocalDateTime lastAccessTime;

    private UserSession() {
        // Private constructor to prevent instantiation
    }

    public static UserSession getInstance() {
        if (instance == null) {
            synchronized (UserSession.class) {
                if (instance == null) {
                    instance = new UserSession();
                }
            }
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.lastAccessTime = LocalDateTime.now();
    }

    public User getCurrentUser() {
        if (currentUser != null && Duration.between(lastAccessTime, LocalDateTime.now()).toMinutes() < 30) {
            lastAccessTime = LocalDateTime.now();
            return currentUser;
        } else {
            clearSession();
            return null;
        }
    }

    public void clearSession() {
        currentUser = null;
        lastAccessTime = null;
    }
}