package lk.property.dao;

import lk.property.models.Nomenclature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NomenclatureDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NomenclatureDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Nomenclature> showAll(){
        return jdbcTemplate.query("SELECT * FROM Номенклатура", new NomenclatureMapper());
    }

    public Nomenclature showOne(int id){
        return jdbcTemplate.query("SELECT * FROM Номенклатура WHERE код_номенклатуры = ?",
                new Object[]{id},
                new NomenclatureMapper()).stream().findAny().orElse(null);
    }

    public void save(Nomenclature nomenclature){
        jdbcTemplate.update("INSERT INTO Номенклатура(вид_оборудования, производитель, модель) VALUES(?, ?, ?)",
                 nomenclature.getType(), nomenclature.getProducer(), nomenclature.getName());
    }

    public void update(int id, Nomenclature updatedNomenclature){
        jdbcTemplate.update("UPDATE Номенклатура SET вид_оборудования = ?," +
                "производитель = ?, модель = ? WHERE код_номенклатуры = ?",
                 updatedNomenclature.getType(), updatedNomenclature.getProducer(), updatedNomenclature.getName(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM Номенклатура WHERE код_номенклатуры = ?", id);
    }

    public boolean haveDevices(Integer id){
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT цена FROM Оборудование WHERE код_номенклатуры = ?)", new Integer[]{id}, Boolean.class);
    }
}
