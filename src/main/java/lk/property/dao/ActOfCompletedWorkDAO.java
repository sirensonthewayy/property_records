package lk.property.dao;

import lk.property.models.ActOfCompletedWork;
import lk.property.models.ActRTRepair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ActOfCompletedWorkDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActOfCompletedWorkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ActOfCompletedWork> showAll(){
        return jdbcTemplate.query("SELECT * FROM Акты_выполненных_работ LEFT JOIN Акты_ПП_ремонт " +
                "ON Акты_выполненных_работ.код_документа_ПП_ремонт = Акты_ПП_ремонт.код_документа " +
                "LEFT JOIN Ремонтные_центры ON Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                new ActOfCompletedWorkMapper(new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper()))));
    }

    public ActOfCompletedWork showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Акты_выполненных_работ LEFT JOIN Акты_ПП_ремонт " +
                "ON Акты_выполненных_работ.код_документа_ПП_ремонт = Акты_ПП_ремонт.код_документа " +
                "LEFT JOIN Ремонтные_центры ON Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                "WHERE Акты_выполненных_работ.код_документа = ?", new Object[]{id},
                new ActOfCompletedWorkMapper(new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper()))))
                .stream().findAny().orElse(null);
    }

    public void save(ActOfCompletedWork actOfCompletedWork){
        jdbcTemplate.update("INSERT INTO Акты_выполненных_работ(код_документа_ПП_ремонт, цена_ремонта," +
                "объект_починки, дата_приемки, неисправность) VALUES(?, ?, ?, ?, ?)",
                actOfCompletedWork.getActRTRepair().getId(),
                actOfCompletedWork.getPrice(), actOfCompletedWork.getWorks(),
                actOfCompletedWork.getDateOfReception(), actOfCompletedWork.getMalfunction());
    }

    public void update(ActOfCompletedWork actOfCompletedWork, Integer id){
        jdbcTemplate.update("UPDATE Акты_выполненных_работ SET код_документа_ПП_ремонт = ?, цена_ремонта = ?, " +
                "объект_починки = ?, дата_приемки = ?, неисправность = ? WHERE код_документа = ?",
                actOfCompletedWork.getActRTRepair().getId(), actOfCompletedWork.getPrice(),
                actOfCompletedWork.getWorks(), actOfCompletedWork.getDateOfReception(),
                actOfCompletedWork.getMalfunction(), id);
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM Акты_выполненных_работ WHERE код_документа = ?", id);
    }
}
