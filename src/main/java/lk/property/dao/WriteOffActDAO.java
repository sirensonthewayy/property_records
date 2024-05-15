package lk.property.dao;

import lk.property.models.WriteOffAct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriteOffActDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WriteOffActDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WriteOffAct> showAll(){
        return jdbcTemplate.query("SELECT * FROM Акт_списания LEFT JOIN Оборудование" +
                " ON Акт_списания.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                new WriteOffActMapper(new DeviceMapper(new NomenclatureMapper())));
    }

    public WriteOffAct showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Акт_списания LEFT JOIN Оборудование" +
                " ON Акт_списания.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                                "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                                "WHERE код_документа = ?",
                        new Object[]{id}, new WriteOffActMapper(new DeviceMapper(new NomenclatureMapper())))
                .stream().findAny().orElse(null);
    }

    public void save(WriteOffAct writeOffAct){
        jdbcTemplate.update("INSERT INTO Акт_списания(дата, причина_списания, инвентарная_карточка) VALUES (?, ?, ?)",
                writeOffAct.getDate(),
                writeOffAct.getWriteOffReason(),
                writeOffAct.getDevice().getInventoryCard());
    }

    public void update(WriteOffAct updatedAct, int id){
        jdbcTemplate.update("UPDATE Акт_списания SET дата = ?, причина_списания = ?, инвентарная_карточка = ? WHERE код_документа = ?",
                updatedAct.getDate(),
                updatedAct.getWriteOffReason(),
                updatedAct.getDevice().getInventoryCard(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Акт_списания WHERE код_документа = ?", id);
    }
}
