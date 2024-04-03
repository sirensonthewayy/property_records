package lk.property.models;

// этот класс описывает номенклатуру имущества отдела

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class Nomenclature {

    private Integer id;
    private String type;
    @NotEmpty(message = "Поле производителя не должно быть пустым")
    private String producer;
    @NotEmpty(message = "Название не должно быть пустым")
    private String name;

    public Nomenclature(){
    }

    public Nomenclature(int id, String type, String producer, String name) {
        this.type = type;
        this.producer = producer;
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nomenclature that = (Nomenclature) o;
        return Objects.equals(type, that.type) && Objects.equals(producer, that.producer) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, producer, name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
