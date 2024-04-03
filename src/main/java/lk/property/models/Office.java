package lk.property.models;

public class Office {
    Integer id;
    String address;
    String chief;
    String phoneNumber;

    public Office(){}

    public Office(Integer id, String address, String chief, String phoneNumber) {
        this.id = id;
        this.address = address;
        this.chief = chief;
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

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
