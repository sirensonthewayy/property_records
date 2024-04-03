package lk.property.dao;

import lk.property.models.Nomenclature;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NomenclatureMapper implements RowMapper<Nomenclature> {
    @Override
    public Nomenclature mapRow(ResultSet resultSet, int rowNum) throws SQLException{
        Nomenclature nomenclature = new Nomenclature();
        nomenclature.setId(resultSet.getInt("код_номенклатуры"));
        nomenclature.setProducer(resultSet.getString("производитель"));
        nomenclature.setType(resultSet.getString("вид_оборудования"));
        nomenclature.setName(resultSet.getString("модель"));
        return nomenclature;
    }
}
