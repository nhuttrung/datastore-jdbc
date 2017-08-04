package vn.khtt.datastore.test.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Manager {
    private @Id @GeneratedValue Long id;
    private String fullName;
    private String[] roles;

    public Manager() {
    }
    public Manager(String fullName, String[] roles) {
        this.fullName = fullName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String[] getRoles() {
        return roles;
    }
}
