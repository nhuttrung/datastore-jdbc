package vn.khtt.datastore.test.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
    private @Id @GeneratedValue Long id;
    private String fullName;
    
    private @ManyToOne Manager manager;

    public Employee() {
    }
    public Employee(String fullName, Manager manager) {
        this.fullName = fullName;
        this.manager = manager;
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

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }
}
