package lk.property.models;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

public class ActOfCompletedWork {

    Integer id;
    ActRTRepair actRTRepair;
    String works;
    Integer price;
    @NotNull(message = "Введите дату возврата")
    Date dateOfReception;
    Boolean malfunction;

    public ActOfCompletedWork(){}

    public ActOfCompletedWork(Integer id, ActRTRepair actRTRepair, String works, Integer price, Date dateOfReception, Boolean malfunction) {
        this.id = id;
        this.actRTRepair = actRTRepair;
        this.works = works;
        this.price = price;
        this.dateOfReception = dateOfReception;
        this.malfunction = malfunction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActRTRepair getActRTRepair() {
        return actRTRepair;
    }

    public void setActRTRepair(ActRTRepair actRTRepair) {
        this.actRTRepair = actRTRepair;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getDateOfReception() {
        return dateOfReception;
    }

    public void setDateOfReception(Date DateOfReception) {
        this.dateOfReception = DateOfReception;
    }

    public Boolean getMalfunction() {
        return malfunction;
    }

    public void setMalfunction(Boolean malfunction) {
        this.malfunction = malfunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActOfCompletedWork that = (ActOfCompletedWork) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
