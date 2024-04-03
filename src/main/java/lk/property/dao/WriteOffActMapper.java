package lk.property.dao;

import lk.property.models.Device;
import lk.property.models.WriteOffAct;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WriteOffActMapper implements RowMapper<WriteOffAct> {

    private final DeviceMapper deviceMapper;

    public WriteOffActMapper(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public WriteOffAct mapRow(ResultSet rs, int rowNum) throws SQLException {
        WriteOffAct writeOffAct = new WriteOffAct();
        writeOffAct.setId(rs.getInt("код_документа"));
        writeOffAct.setDate(rs.getDate("дата"));
        writeOffAct.setWriteOffReason(rs.getString("причина_списания"));

        Device device = this.deviceMapper.mapRow(rs, rowNum);
        writeOffAct.setDevice(device);

        return writeOffAct;
    }
}
