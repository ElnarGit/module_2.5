package com.elnar.module25.dto;


import com.elnar.module25.entity.File;
import com.elnar.module25.entity.Status;
import com.elnar.module25.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDto {
	private Long id;
	private User user;
	private File file;
	private Status status;
}
