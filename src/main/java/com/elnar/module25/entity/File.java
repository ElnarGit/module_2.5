package com.elnar.module25.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class File {
  @Id
  private Long id;
  private String location;
  private Status status;
}
