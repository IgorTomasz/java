package tpo.webapp.webapp.Models;

import java.util.Date;

public class Patient {
    private int id;
    private String name;
    private String lastName;
    private String pesel;
    private String phone;
    private Date data;

    public Patient(int id, String name, String lastName, String pesel, String phone, Date data) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;

        char[] arr = phone.toCharArray();
        int i = 1;
        this.phone="";
        for(char el : arr){
            if(i == 4 || i == 7){
                this.phone+="-";
            }
            this.phone+=el;
            i++;
        }

        this.pesel = pesel;
        this.data=data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
