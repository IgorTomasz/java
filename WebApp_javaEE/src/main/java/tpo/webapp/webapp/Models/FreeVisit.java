package tpo.webapp.webapp.Models;

import java.util.Date;

public class FreeVisit {
    private String Specialization;
    private Date data;
    private String Name;
    private String LastName;

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public FreeVisit(String specialization, Date data, String name, String lastName) {
        Specialization = specialization;
        this.data = data;
        Name = name;
        LastName = lastName;
    }
}
