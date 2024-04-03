package lk.property.models;

/*import jakarta.persistence.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.data.relational.core.mapping.MappedCollection;*/

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Date;
import java.time.LocalDate;

public class Device {

    @NotEmpty(message = "Номер карточки не может быть пустым") private String inventoryCard;
    private Nomenclature nomenclature;
    @NotEmpty(message = "Серийный номер не может быть пустым")
    private String serialNumber;
    @PastOrPresent(message = "Дата не должна быть позже сегодняшней") @NotNull(message = "Дата не может быть пустой")
    private Date dateOfSupply;
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Double price;
    private String status;
    private String note;

    public Device(){}

    public Device(String inventoryCard, Nomenclature nomenclature, String serialNumber, Date dateOfSupply, Double price, String status) {
        this.inventoryCard = inventoryCard;
        this.nomenclature = nomenclature;
        this.serialNumber = serialNumber;
        this.dateOfSupply = dateOfSupply;
        this.price = price;
        this.status = status;
    }

    public Device(String inventoryCard, Nomenclature nomenclature, String serialNumber, Date dateOfSupply, Double price, String note, String status) {
        this.inventoryCard = inventoryCard;
        this.nomenclature = nomenclature;
        this.serialNumber = serialNumber;
        this.dateOfSupply = dateOfSupply;
        this.price = price;
        this.status = status;
        this.note = note;
    }

    public String getInventoryCard() {
        return inventoryCard;
    }

    public void setInventoryCard(String inventoryCard) {
        this.inventoryCard = inventoryCard;
    }

    public Nomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Nomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getDateOfSupply() {
        return dateOfSupply;
    }

    public void setDateOfSupply(Date dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
