package lk.property.dao;

import lk.property.models.ActRTOffices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActRTOfficesDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActRTOfficesDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ActRTOffices> showAll(){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_офисы LEFT JOIN Оборудование " +
                "ON Акт_ПП_офисы.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Офисы ON Акт_ПП_офисы.код_офиса = Офисы.код_офиса LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                new ActRTOfficesMapper(new DeviceMapper(new NomenclatureMapper()), new OfficeMapper()));
    }

    public void save(ActRTOffices actRTOffices){
        jdbcTemplate.update("INSERT INTO Акт_ПП_офисы(инвентарная_карточка, " +
                        "дата_передачи, код_офиса, дата_приемки) VALUES(?, ?, ?, ?)",
                actRTOffices.getDevice().getInventoryCard(),
                actRTOffices.getDateOfTransmission(),
                actRTOffices.getOffice().getId(),
                actRTOffices.getDateOfReception());
    }

    public ActRTOffices showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_офисы LEFT JOIN Оборудование " +
                "ON Акт_ПП_офисы.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Офисы ON Акт_ПП_офисы.код_офиса = Офисы.код_офиса LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                "WHERE Акт_ПП_офисы.код_документа = ?", new Object[]{id}, new ActRTOfficesMapper(new DeviceMapper(new NomenclatureMapper()), new OfficeMapper()))
                .stream().findAny().orElse(null);
    }

    public void update(Integer id, ActRTOffices updatedAct){
        jdbcTemplate.update("UPDATE Акт_ПП_офисы SET инвентарная_карточка = ?, дата_передачи = ? ," +
                "код_офиса = ?, дата_приемки = ? WHERE код_документа = ?",
                updatedAct.getDevice().getInventoryCard(), updatedAct.getDateOfTransmission(),
                updatedAct.getOffice().getId(), updatedAct.getDateOfReception(), id);
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM Акт_ПП_офисы WHERE код_документа = ?", id);
    }
}
