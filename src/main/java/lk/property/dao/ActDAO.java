package lk.property.dao;

import lk.property.models.Act;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class ActDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Act> showActsByDevice(String inventoryCard){
        return jdbcTemplate.query("SELECT * FROM all_acts LEFT JOIN Оборудование ON " +
                        "all_acts.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                        "WHERE all_acts.инвентарная_карточка = ? ORDER BY дата DESC", new Object[]{inventoryCard},
                new ActMapper(new DeviceMapper(new NomenclatureMapper())));
    }

    public Act showLastAct(String inventoryCard){
        return jdbcTemplate.query("SELECT * FROM all_acts LEFT JOIN Оборудование ON " +
                "all_acts.инвентарная_карточка = Оборудование.инвентарная_карточка " +
                        "LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры " +
                        "WHERE all_acts.инвентарная_карточка = ? ORDER BY дата DESC LIMIT 1", new Object[]{inventoryCard},
                new ActMapper(new DeviceMapper(new NomenclatureMapper()))).stream().findAny().orElse(null);
    }
}
