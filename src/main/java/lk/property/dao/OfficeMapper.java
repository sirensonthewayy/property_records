package lk.property.dao;

import lk.property.models.Office;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficeMapper implements RowMapper<Office> {

    @Override
    public Office mapRow(ResultSet resultSet, int rowNum) throws SQLException{
        Office office = new Office();
        office.setId(resultSet.getInt("код_офиса"));
        office.setAddress(resultSet.getString("адрес"));
        office.setResponsiblePerson(resultSet.getString("ответственное_лицо"));
        office.setPhoneNumber(resultSet.getString("телефон"));
        return office;
    }
}
