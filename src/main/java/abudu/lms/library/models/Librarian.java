package abudu.lms.library.models;

import java.time.LocalDateTime;
import java.util.Set;

public class Librarian extends User{

    public Librarian(int id, String firstName, String lastName, String username, String email, String password, LocalDateTime createdAt, Set<Role> role) {
        super(id, firstName, lastName, username, email, password, createdAt, role);
        this.getRoles().add(new Role(2, ERole.Librarian)); // Ensures role is set
    }

}
