package com.elnar.module25.mapper;


import com.elnar.module25.dto.EventDto;
import com.elnar.module25.entity.Event;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
	EventDto map(Event event);
	
	@InheritInverseConfiguration
	Event map(EventDto eventDto);
}
