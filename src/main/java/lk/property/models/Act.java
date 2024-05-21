package lk.property.models;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class Act {

    private Integer id;
    private Device device;
    private Date date;
    private String typeOfAct;
    private String link;
    private String message;

    public Act(){}

    public Act(Integer id, Device device, Date date, String typeOfAct, String link, String message) {
        this.id = id;
        this.device = device;
        this.date = date;
        this.typeOfAct = typeOfAct;
        this.link = link;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTypeOfAct() {
        return typeOfAct;
    }

    public void setTypeOfAct(String typeOfAct) {
        this.typeOfAct = typeOfAct;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

