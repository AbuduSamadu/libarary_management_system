package abudu.lms.library.security;


import abudu.lms.library.models.User;

public class AccessControl {
    public static boolean hasRole(User user, String role) {
        return user.getRoles().contains(role);
    }

}
