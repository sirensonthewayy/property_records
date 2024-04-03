package lk.property.dao;

import lk.property.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Company> showAll(){
        return jdbcTemplate.query("SELECT * FROM Контрагенты", new CompanyMapper());
    }

    public Company showOne(String inn){
        return jdbcTemplate.query("SELECT * FROM Контрагенты WHERE инн = ?", new Object[]{inn},
                new CompanyMapper()).stream().findAny().orElse(null);
    }

    public void save(Company company){
        jdbcTemplate.update("INSERT INTO Контрагенты(инн, название_организации, адрес, фио_руководителя, телефон) VALUES(?, ?, ?, ?, ?)",
                company.getInn(), company.getName(), company.getAddress(), company.getChief(), company.getPhoneNumber());
    }

    public void update(Company updatedCompany, String inn){
        jdbcTemplate.update("UPDATE Контрагенты SET инн = ?, название_организации = ?, адрес = ?, фио_руководителя = ?, телефон = ? WHERE инн = ?",
                updatedCompany.getInn(), updatedCompany.getName(), updatedCompany.getAddress(), updatedCompany.getChief(),
                updatedCompany.getPhoneNumber(), inn);
    }

    public void delete(String inn){
        jdbcTemplate.update("DELETE FROM Контрагенты WHERE инн = ?", inn);
    }

    public boolean haveActs(String inn){
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT инн FROM Акт_ПП_контрагенты WHERE инн = ?)",
                new String[]{inn}, Boolean.class);
    }
}
