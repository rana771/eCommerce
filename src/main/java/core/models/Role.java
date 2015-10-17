package core.models;

import java.util.List;

/**
 * Created by Raihan on 15-Oct-15.
 */
public class Role {
    private Long id;
    private String name;
    private List<Right> rights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    public void addRight(Right right) {
        this.rights.add(right);
    }
}
