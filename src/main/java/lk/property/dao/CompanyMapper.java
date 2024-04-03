package lk.property.dao;

import lk.property.models.Company;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyMapper implements RowMapper<Company> {

    @Override
    public Company mapRow(ResultSet resultSet, int rowNum) throws SQLException{

        Company company = new Company();
        company.setInn(resultSet.getString("инн"));
        company.setName(resultSet.getString("название_организации"));
        company.setChief(resultSet.getString("фио_руководителя"));
        company.setAddress(resultSet.getString("адрес"));
        company.setPhoneNumber(resultSet.getString("телефон"));
        return company;

    }
}
