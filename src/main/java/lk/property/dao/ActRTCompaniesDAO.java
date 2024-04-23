package lk.property.dao;

import lk.property.models.ActRTCompanies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActRTCompaniesDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActRTCompaniesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ActRTCompanies> showAll(){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_контрагенты LEFT JOIN Оборудование " +
                "ON Акт_ПП_контрагенты.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                "LEFT JOIN Контрагенты ON Акт_ПП_контрагенты.инн = Контрагенты.инн LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                new ActRTCompaniesMapper(new DeviceMapper(new NomenclatureMapper()), new CompanyMapper()));
    }

/*    public List<ActRTCompanies> showAllTransmissions(){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_контрагенты LEFT JOIN Оборудование " +
                        "ON Акт_ПП_контрагенты.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Контрагенты ON Акт_ПП_контрагенты.инн = Контрагенты.инн LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры" +
                        "= Номенклатура.код_номенклатуры WHERE вид_движения = 'Передача'",
                new ActRTCompaniesMapper(new DeviceMapper(new NomenclatureMapper()), new CompanyMapper()));
    }*/

    public ActRTCompanies showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_контрагенты LEFT JOIN Оборудование\n" +
                "ON Акт_ПП_контрагенты.инвентарная_карточка = Оборудование.инвентарная_карточка\n" +
                "LEFT JOIN Контрагенты ON Акт_ПП_контрагенты.инн = Контрагенты.инн LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры WHERE код_документа = ?",
                new Object[]{id}, new ActRTCompaniesMapper(new DeviceMapper(new NomenclatureMapper()), new CompanyMapper()))
                .stream().findAny().orElse(null);
    }

    public void save(ActRTCompanies actRTCompanies){
        jdbcTemplate.update("INSERT INTO Акт_ПП_контрагенты(инвентарная_карточка, дата_передачи, инн, дата_приемки) VALUES(?, ?, ?, ?)",
                actRTCompanies.getDevice().getInventoryCard(),
                actRTCompanies.getDateOfTransmission(),
                actRTCompanies.getCompany().getInn(),
                actRTCompanies.getDateOfReception());
    }

    public void update(Integer id, ActRTCompanies updatedAct){
        jdbcTemplate.update("UPDATE Акт_ПП_контрагенты SET инвентарная_карточка = ?, " +
                "дата_передачи = ?, инн = ?, дата_приемки = ? WHERE код_документа = ?",
                updatedAct.getDevice().getInventoryCard(), updatedAct.getDateOfTransmission(),
                updatedAct.getCompany().getInn(), updatedAct.getDateOfReception(), id);
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM Акт_ПП_контрагенты WHERE код_документа = ?", id);
    }

    public List<ActRTCompanies> showCurrentActs(){
        return jdbcTemplate.query("SELECT * FROM Акт_ПП_контрагенты\n" +
                "LEFT JOIN Оборудование ON Акт_ПП_контрагенты.инвентарная_карточка = Оборудование.инвентарная_карточка\n" +
                "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры\n" +
                "LEFT JOIN Контрагенты ON Акт_ПП_контрагенты.инн = Контрагенты.инн\n" +
                "WHERE дата_приемки IS NULL",
                new ActRTCompaniesMapper(new DeviceMapper(new NomenclatureMapper()), new CompanyMapper()));
    }

}
