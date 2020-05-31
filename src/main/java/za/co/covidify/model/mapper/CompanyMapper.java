package za.co.covidify.model.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import za.co.covidify.model.Company;
import za.co.covidify.model.dto.CompanyDto;

@Mapper
public interface CompanyMapper {

  CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

  CompanyDto toDto(Company company);

  @IterableMapping(elementTargetType = CompanyDto.class)
  List<CompanyDto> toDtos(List<Company> companys);

  Company dtoToEntity(CompanyDto companytDto);
}
