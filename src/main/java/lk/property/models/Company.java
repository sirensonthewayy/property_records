package lk.property.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Company {

    @Size(min = 10, max = 12, message = "ИНН для юрлиц состоит из 10 цифр, в некоторых случаях для ИП - из 12")
    private String inn;
    @NotEmpty(message = "Название филиала не должно быть пустым")
    private String name;
    @NotEmpty(message = "Адрес не должен быть пустым")
    private String address;
    @NotEmpty(message = "ФИО начальника не должно быть пустым")
    private String chief;
    private String phoneNumber;

    public Company(){}

    public Company(String inn, String name, String address, String chief, String phoneNumber) {
        this.inn = inn;
        this.name = name;
        this.address = address;
        this.chief = chief;
        this.phoneNumber = phoneNumber;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
