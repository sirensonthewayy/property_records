package lk.property.dao;

import lk.property.models.Act;
import lk.property.models.Device;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActMapper implements RowMapper<Act> {

    private final DeviceMapper deviceMapper;

    public ActMapper(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public Act mapRow(ResultSet rs, int rowNum) throws SQLException {
        Act act = new Act();
        act.setId(rs.getInt("код_документа"));
        act.setDate(rs.getDate("дата"));
        act.setTypeOfAct(rs.getString("вид_документа"));
        act.setLink(rs.getString("ссылка_на_документ"));
        act.setMessage(rs.getString("сообщение"));
        Device device = this.deviceMapper.mapRow(rs, rowNum);
        act.setDevice(device);
        return act;
    }
}
