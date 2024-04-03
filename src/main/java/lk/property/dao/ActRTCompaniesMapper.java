package lk.property.dao;

import lk.property.models.ActRTCompanies;
import lk.property.models.Company;
import lk.property.models.Device;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActRTCompaniesMapper implements RowMapper<ActRTCompanies> {

    private final DeviceMapper deviceMapper;
    private final CompanyMapper companyMapper;
    //private final NomenclatureMapper nomenclatureMapper;

    public ActRTCompaniesMapper(DeviceMapper deviceMapper, CompanyMapper companyMapper) {
        this.deviceMapper = deviceMapper;
        this.companyMapper = companyMapper;
        //this.nomenclatureMapper = nomenclatureMapper;
    }

    @Override
    public ActRTCompanies mapRow(ResultSet resultSet, int rowNum) throws SQLException{
        ActRTCompanies actRTCompanies = new ActRTCompanies();
        actRTCompanies.setId(resultSet.getInt("код_документа"));
        actRTCompanies.setDateOfTransmission(resultSet.getDate("дата_передачи"));
        actRTCompanies.setDateOfReception(resultSet.getDate("дата_приемки"));

        Device device = this.deviceMapper.mapRow(resultSet, rowNum);
        Company company = this.companyMapper.mapRow(resultSet, rowNum);

        actRTCompanies.setDevice(device);
        actRTCompanies.setCompany(company);

        return actRTCompanies;
    }

}
