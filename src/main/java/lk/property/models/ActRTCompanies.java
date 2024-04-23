package lk.property.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

public class ActRTCompanies{

    private Integer id;
    private Company company;
    private Device device;
    @NotNull(message = "Укажите дату передачи")
    /*@DateTimeFormat(pattern = "yyyy-MM-dd")*/
    private Date dateOfTransmission;
    private Date dateOfReception;

    public ActRTCompanies(){}

    public ActRTCompanies(Integer id, Company company, Device device, Date dateOfTransmission, Date dateOfReception) {
        this.id = id;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
}
