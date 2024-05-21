package lk.property.dao;

import lk.property.models.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfficeDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OfficeDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Office> showAll(){
        return jdbcTemplate.query("SELECT * FROM Офисы", new OfficeMapper());
    }

    public Office showOne(int id){
        return jdbcTemplate.query("SELECT * FROM Офисы WHERE код_офиса = ?",
                new Object[]{id},
                new OfficeMapper()).stream().findAny().orElse(null);
    }

    public void save(Office office){
        jdbcTemplate.update("INSERT INTO Офисы(адрес, ответственное_лицо, телефон)" +
                "VALUES(?, ?, ?)", office.getAddress(), office.getResponsiblePerson(), office.getPhoneNumber());
    }

    public void update(int id, Office updatedOffice){
        jdbcTemplate.update("UPDATE Офисы SET адрес = ?, ответственное_лицо = ?, телефон = ? " +
                "WHERE код_офиса = ?", updatedOffice.getAddress(),
                updatedOffice.getResponsiblePerson(), updatedOffice.getPhoneNumber(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Офисы WHERE код_офиса = ?", id);
    }
}
