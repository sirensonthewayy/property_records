package lk.property.dao;

import lk.property.models.ActRTRepair;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ActRTRepairDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActRTRepairDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ActRTRepair> showAll(){
        return jdbcTemplate.query("SELECT * FROM Акты_ПП_ремонт LEFT JOIN Ремонтные_центры ON " +
                "Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper())));
    }

    public List<ActRTRepair> showAllCurrent(){
        return jdbcTemplate.query("SELECT * FROM Акты_ПП_ремонт LEFT JOIN Ремонтные_центры ON " +
                        "Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                        "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                        "WHERE дата_приемки IS NULL",
                new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper())));
    }

/*    public List<ActRTRepair> showAllCurrent(String inventoryCard){
        return jdbcTemplate.query("SELECT * FROM Акты_ПП_ремонт LEFT JOIN Ремонтные_центры ON " +
                        "Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                        "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                        "WHERE дата_приемки IS NULL UNION ALL " +
                        "SELECT * FROM Акты_ПП_ремонт LEFT JOIN Ремонтные_центры ON " +
                        "Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                        "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                        "WHERE Акты_ПП_ремонт.инвентарная_карточка = ?", new Object[]{inventoryCard},
                new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper())));
    }*/

    public ActRTRepair showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Акты_ПП_ремонт LEFT JOIN Ремонтные_центры ON " +
                "Акты_ПП_ремонт.код_ремонтного_центра = Ремонтные_центры.код_ремонтного_центра " +
                "LEFT JOIN Оборудование ON Акты_ПП_ремонт.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                "WHERE Акты_ПП_ремонт.код_документа = ?", new Object[]{id}, new ActRTRepairMapper(new RepairCompaniesMapper(), new DeviceMapper(new NomenclatureMapper()))).stream().findAny().orElse(null);
    }

    public void save(ActRTRepair actRTRepair) throws DataIntegrityViolationException {
        jdbcTemplate.update("INSERT INTO Акты_ПП_ремонт(инвентарная_карточка, код_ремонтного_центра," +
                        "дата_передачи, дата_приемки) VALUES(?, ?, ?, ?)", actRTRepair.getDevice().getInventoryCard(),
                actRTRepair.getRepairCompany().getId(), actRTRepair.getDateOfTransmission(), actRTRepair.getDateOfReception());
    }

    public void update(ActRTRepair actRTRepair, Integer id) throws DataIntegrityViolationException{
        jdbcTemplate.update("UPDATE Акты_ПП_ремонт SET инвентарная_карточка = ?, код_ремонтного_центра = ?, " +
                "дата_передачи = ?, дата_приемки = ? WHERE код_документа = ?", actRTRepair.getDevice().getInventoryCard(),
                actRTRepair.getRepairCompany().getId(), actRTRepair.getDateOfTransmission(), actRTRepair.getDateOfReception(), id);
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM Акты_ПП_ремонт WHERE код_документа = ?", id);
    }
}
