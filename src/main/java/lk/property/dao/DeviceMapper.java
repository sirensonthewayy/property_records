package lk.property.dao;

import lk.property.models.Device;
import lk.property.models.Nomenclature;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeviceMapper implements RowMapper<Device> {

    private final NomenclatureMapper nomenclatureMapper;

    public DeviceMapper(NomenclatureMapper nomenclatureMapper){
        this.nomenclatureMapper = nomenclatureMapper;
    }

    @Override
    public Device mapRow(ResultSet resultSet, int rowNum) throws SQLException{
        Device device = new Device();
        device.setInventoryCard(resultSet.getString("инвентарная_карточка"));
        device.setSerialNumber(resultSet.getString("серийный_номер"));
        device.setDateOfSupply(resultSet.getDate("дата_поступления"));
        device.setStatus(resultSet.getString("статус"));
        device.setPrice(resultSet.getDouble("цена"));
        device.setNote(resultSet.getString("примечание"));

        Nomenclature nomenclature = this.nomenclatureMapper.mapRow(resultSet, rowNum);
        device.setNomenclature(nomenclature);

        return device;
    }
}
