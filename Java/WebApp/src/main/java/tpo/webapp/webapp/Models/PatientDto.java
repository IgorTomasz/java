package tpo.webapp.webapp.Models;

import java.util.Date;

public class PatientDto {
    private String name;
    private String lastName;
    private String pesel;
    private String phone;

    public PatientDto(String name, String lastName, String pesel, String phone) {
        this.name = name;
        this.lastName = lastName;
        this.phone=phone;
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
