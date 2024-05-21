package lk.property.models;

public class Office {
    Integer id;
    String address;
    String responsiblePerson;
    String phoneNumber;

    public Office(){}

    public Office(Integer id, String address, String responsiblePerson, String phoneNumber) {
        this.id = id;
        this.address = address;
        this.responsiblePerson = responsiblePerson;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
