package com.elnar.module25.service;

import com.elnar.module25.entity.File;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileService extends GenericService<File, Long> {
  Mono<File> uploadFile(FilePart file, Long userId);
}
