package com.elnar.module25.mapper;


import com.elnar.module25.dto.FileDto;
import com.elnar.module25.entity.File;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
	FileDto map(File file);
	
	@InheritInverseConfiguration
	File map(FileDto fileDto);
}
