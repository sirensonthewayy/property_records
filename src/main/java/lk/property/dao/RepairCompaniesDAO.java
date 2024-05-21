package lk.property.dao;

import lk.property.models.RepairCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepairCompaniesDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RepairCompaniesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RepairCompany> showAll(){
        return jdbcTemplate.query("SELECT * FROM Ремонтные_центры", new RepairCompaniesMapper());
    }

    public RepairCompany showOne(Integer id){
        return jdbcTemplate.query("SELECT * FROM Ремонтные_центры WHERE код_ремонтного_центра = ?",
                new Object[]{id}, new RepairCompaniesMapper()).stream().findAny().orElse(null);
    }

    public void save(RepairCompany repairCompany){
        jdbcTemplate.update("INSERT INTO Ремонтные_центры(название, адрес, телефон) VALUES(?, ?, ?)",
                repairCompany.getName(), repairCompany.getAddress(), repairCompany.getPhoneNumber());
    }

    public void update(Integer id, RepairCompany repairCompany){
        jdbcTemplate.update("UPDATE Ремонтные_центры SET название = ?, адрес = ?, телефон = ? WHERE код_ремонтного_центра = ?",
                repairCompany.getName(), repairCompany.getAddress(), repairCompany.getPhoneNumber(), id);
    }

    public void delete(Integer id){
        jdbcTemplate.update("DELETE FROM Ремонтные_центры WHERE код_ремонтного_центра = ?", id);
    }
}
