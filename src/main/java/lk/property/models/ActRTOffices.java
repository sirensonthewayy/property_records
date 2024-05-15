package lk.property.models;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

public class ActRTOffices {

    private Integer id;
    private Office office;
    private Device device;
    @NotNull(message = "Укажите дату передачи")
    /*@DateTimeFormat(pattern = "yyyy-MM-dd")*/
    private Date dateOfTransmission;
    private Date dateOfReception;

    public ActRTOffices(){}

    public ActRTOffices(Integer id, Office office, Device device, @NotNull(message = "Укажите дату передачи") Date dateOfTransmission, Date dateOfReception) {
        this.id = id;
        this.office = office;
        this.device = device;
        this.dateOfTransmission = dateOfTransmission;
        this.dateOfReception = dateOfReception;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getDateOfTransmission() {
        return dateOfTransmission;
    }

    public void setDateOfTransmission(Date dateOfTransmission) {
        this.dateOfTransmission = dateOfTransmission;
    }

    public Date getDateOfReception() {
        return dateOfReception;
    }

    public void setDateOfReception(Date dateOfReception) {
        this.dateOfReception = dateOfReception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActRTOffices that = (ActRTOffices) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
