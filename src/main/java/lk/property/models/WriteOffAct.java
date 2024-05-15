package lk.property.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public class WriteOffAct {

    private int id;
    @NotNull(message = "Укажите дату")
    private Date date;
    private Device device;
    @NotEmpty(message = "Укажите причину списания")
    private String writeOffReason;

    public WriteOffAct(){}

    public WriteOffAct(int id, Date date, Device device, String writeOffReason) {
        this.id = id;
        this.date = date;
        this.device = device;
        this.writeOffReason = writeOffReason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getWriteOffReason() {
        return writeOffReason;
    }

    public void setWriteOffReason(String writeOffReason) {
        this.writeOffReason = writeOffReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WriteOffAct that = (WriteOffAct) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
