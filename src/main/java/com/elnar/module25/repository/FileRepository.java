package com.elnar.module25.repository;

import com.elnar.module25.entity.File;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FileRepository extends R2dbcRepository<File, Long> {
}
