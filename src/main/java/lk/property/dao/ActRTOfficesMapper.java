package lk.property.dao;

import lk.property.models.ActRTOffices;
import lk.property.models.Device;
import lk.property.models.Office;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActRTOfficesMapper implements RowMapper<ActRTOffices> {

    private final DeviceMapper deviceMapper;
    private final OfficeMapper officeMapper;


    public ActRTOfficesMapper(DeviceMapper deviceMapper, OfficeMapper officeMapper) {
        this.deviceMapper = deviceMapper;
        this.officeMapper = officeMapper;
    }

    @Override
    public ActRTOffices mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ActRTOffices actRTOffices = new ActRTOffices();
        actRTOffices.setId(resultSet.getInt("код_документа"));
        actRTOffices.setDateOfTransmission(resultSet.getDate("дата_передачи"));
        actRTOffices.setDateOfReception(resultSet.getDate("дата_приемки"));

        Device device = this.deviceMapper.mapRow(resultSet, rowNum);
        Office office = this.officeMapper.mapRow(resultSet, rowNum);

        actRTOffices.setDevice(device);
        actRTOffices.setOffice(office);

        return actRTOffices;
    }
}
