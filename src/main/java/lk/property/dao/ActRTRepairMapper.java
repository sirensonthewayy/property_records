package lk.property.dao;

import lk.property.models.ActRTRepair;
import lk.property.models.Device;
import lk.property.models.Nomenclature;
import lk.property.models.RepairCompany;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActRTRepairMapper implements RowMapper<ActRTRepair> {

    private final RepairCompaniesMapper repairCompaniesMapper;
    private final DeviceMapper deviceMapper;

    public ActRTRepairMapper(RepairCompaniesMapper repairCompaniesMapper, DeviceMapper deviceMapper) {
        this.repairCompaniesMapper = repairCompaniesMapper;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public ActRTRepair mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActRTRepair actRTRepair = new ActRTRepair();
        actRTRepair.setId(rs.getInt("код_документа"));
        actRTRepair.setDateOfTransmission(rs.getDate("дата_передачи"));
        actRTRepair.setDateOfReception(rs.getDate("дата_приемки"));

        Device device = this.deviceMapper.mapRow(rs, rowNum);
        RepairCompany repairCompany = this.repairCompaniesMapper.mapRow(rs, rowNum);

        actRTRepair.setDevice(device);
        actRTRepair.setRepairCompany(repairCompany);
        return actRTRepair;
    }
}
