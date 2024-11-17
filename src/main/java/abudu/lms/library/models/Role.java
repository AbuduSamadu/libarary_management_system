package abudu.lms.library.models;

public class Role {
    private int id;
    private ERole name;


    /**
     * Constructor for the Role class.
     * @param id The id of the role.
     * @param name The name of the role.
     */
    public Role(int id, ERole name) {
        this.id = id;
        this.name = name;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}