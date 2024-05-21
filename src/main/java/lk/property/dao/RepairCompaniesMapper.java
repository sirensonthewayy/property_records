package lk.property.dao;

import lk.property.models.RepairCompany;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepairCompaniesMapper implements RowMapper<RepairCompany>{

    @Override
    public RepairCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
        RepairCompany repairCompany = new RepairCompany();
        repairCompany.setId(rs.getInt("код_ремонтного_центра"));
        repairCompany.setName(rs.getString("название"));
        repairCompany.setAddress(rs.getString("адрес"));
        repairCompany.setPhoneNumber(rs.getString("телефон"));
        return repairCompany;
    }
}
