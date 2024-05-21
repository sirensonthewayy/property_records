package lk.property.dao;

import lk.property.models.Device;
import lk.property.models.Nomenclature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class DeviceDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DeviceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Device> showAll(){
        return jdbcTemplate.query("SELECT * FROM Оборудование LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры",
                                    new DeviceMapper(new NomenclatureMapper()));
    }

    public List<Device> showAllByStatuses(String[] statuses){
        if(statuses.length == 0){return null;}
        StringBuilder queryText = new StringBuilder("SELECT * FROM Оборудование LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры WHERE статус = '" + statuses[0] + "'");
        for(int i = 1; i < statuses.length; i++){
            queryText.append(" OR статус = '" + statuses[i] + "'");
        }
        return jdbcTemplate.query(String.valueOf(queryText), new DeviceMapper(new NomenclatureMapper()));
    }

/*    public List<Device> showAllWorkingInStock(){
        return jdbcTemplate.query("SELECT * FROM Оборудование LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры WHERE статус = 'Работает (на складе)'",
                new DeviceMapper(new NomenclatureMapper()));
    }

    public List<Device> showAllWorking(){
        return jdbcTemplate.query("SELECT * FROM Оборудование LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры WHERE статус = 'Работает (на складе)' OR статус = 'Работает (у клиента)'",
                new DeviceMapper(new NomenclatureMapper()));
    }*/

/*    public HashMap<String, List<Double>> getAmountAndPriceByStatuses(){
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("Работает (на складе)");
        statuses.add("Работает (у клиента)");
        statuses.add("Не работает");
        statuses.add("Списан");
        statuses.add("В ремонте");
        HashMap<String, List<Double>> results = new HashMap<>();
        for(String status: statuses){
            List<Double> result = new ArrayList<>();
            result.add(jdbcTemplate.queryForObject("SELECT count(цена) FROM Оборудование WHERE статус = ?", new Object[]{status}, Double.class));
            result.add(jdbcTemplate.queryForObject("SELECT sum(цена) FROM Оборудование WHERE статус = ?", new Object[]{status}, Double.class));
            results.put(status, result);
        }
        return results;
    }*/

    public Device showOne(String inventoryCard){
        return jdbcTemplate.query("SELECT * FROM Оборудование LEFT JOIN Номенклатура ON Оборудование.код_номенклатуры = Номенклатура.код_номенклатуры WHERE инвентарная_карточка = ?",
                new Object[]{inventoryCard},
                new DeviceMapper(new NomenclatureMapper())).stream().findAny().orElse(null);
    }

/*    public void save(Device device){
        if(device.getNomenclature() == null){
            jdbcTemplate.update("INSERT INTO Оборудование VALUES (?, ?, ?, ?, ?, ?, ?)",
                    device.getInventoryCard(),
                    null,
                    device.getSerialNumber(),
                    device.getDateOfSupply(),
                    device.getPrice(),
                    device.getStatus(),
                    device.getNote()
            );
        } else{
            jdbcTemplate.update("INSERT INTO Оборудование VALUES (?, ?, ?, ?, ?, ?, ?)",
                    device.getInventoryCard(),
                    device.getNomenclature().getId(),
                    device.getSerialNumber(),
                    device.getDateOfSupply(),
                    device.getPrice(),
                    device.getStatus(),
                    device.getNote()
            );
        }
    }*/

    public void save(Device device) throws DuplicateKeyException {
        jdbcTemplate.update("INSERT INTO Оборудование VALUES (?, ?, ?, ?, ?, ?, ?)",
                device.getInventoryCard(),
                device.getNomenclature().getId(),
                device.getSerialNumber(),
                device.getDateOfSupply(),
                device.getPrice(),
                device.getStatus(),
                device.getNote()
        );
    }

    public void update(String oldInvCard, Device updatedDevice){
        jdbcTemplate.update("UPDATE Оборудование SET инвентарная_карточка = ?," +
                        "код_номенклатуры = ?, серийный_номер = ?, дата_поступления = ?," +
                        "цена = ?, статус = ?, примечание = ? WHERE инвентарная_карточка = ?",
                updatedDevice.getInventoryCard(), updatedDevice.getNomenclature().getId(), updatedDevice.getSerialNumber(),
                updatedDevice.getDateOfSupply(), updatedDevice.getPrice(), updatedDevice.getStatus(),
                updatedDevice.getNote(), oldInvCard
        );
    }

    public void delete(String invCard){
        jdbcTemplate.update("DELETE FROM Оборудование WHERE инвентарная_карточка = ?", invCard);
    }
}
