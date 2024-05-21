package lk.property.models;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Objects;

public class ActRTRepair {

    private Integer id;
    private Device device;
    private RepairCompany repairCompany;
    @NotNull(message = "Укажите дату передачи")
    private Date dateOfTransmission;
    private Date dateOfReception;

    public ActRTRepair(){}

    public ActRTRepair(Integer id, Device device, RepairCompany repairCompany, Date dateOfTransmission, Date dateOfReception) {
        this.id = id;
        this.device = device;
        this.repairCompany = repairCompany;
        this.dateOfTransmission = dateOfTransmission;
        this.dateOfReception = dateOfReception;
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

    public RepairCompany getRepairCompany() {
        return repairCompany;
    }

    public void setRepairCompany(RepairCompany repairCompany) {
        this.repairCompany = repairCompany;
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
        ActRTRepair that = (ActRTRepair) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
