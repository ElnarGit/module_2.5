package com.elnar.module25.dto;

import com.elnar.module25.entity.Status;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDto {
  private Long id;
  private Long userId;
  private Long fileId;
  private Status status;
}
