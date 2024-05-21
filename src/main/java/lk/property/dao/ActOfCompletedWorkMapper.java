package lk.property.dao;

import lk.property.models.ActOfCompletedWork;
import lk.property.models.ActRTRepair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActOfCompletedWorkMapper implements RowMapper<ActOfCompletedWork> {

    private final ActRTRepairMapper actRTRepairMapper;

    public ActOfCompletedWorkMapper(ActRTRepairMapper actRTRepairMapper) {
        this.actRTRepairMapper = actRTRepairMapper;
    }

    @Override
    public ActOfCompletedWork mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActOfCompletedWork actOfCompletedWork = new ActOfCompletedWork();
        actOfCompletedWork.setId(rs.getInt("код_документа"));
        actOfCompletedWork.setWorks(rs.getString("объект_починки"));
        actOfCompletedWork.setPrice(rs.getInt("цена_ремонта"));
        actOfCompletedWork.setDateOfReception(rs.getDate("дата_приемки"));
        actOfCompletedWork.setMalfunction(rs.getBoolean("неисправность"));

        ActRTRepair actRTRepair = this.actRTRepairMapper.mapRow(rs, rowNum);
        actRTRepair.setId(rs.getInt("код_документа_ПП_ремонт"));
        actOfCompletedWork.setActRTRepair(actRTRepair);
        return actOfCompletedWork;
    }
}
