package abudu.lms.library.security;

import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;

public class AccessControl {
    public static boolean hasRole(User user, Role role) {
        return user.getRoles().contains(role);
    }

}
