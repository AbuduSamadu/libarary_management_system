package abudu.lms.library.models;

public enum Role {
    PATRON("Patron"),
    LIBRARIAN("Librarian");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
