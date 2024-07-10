package com.elnar.module25.rest;

import com.elnar.module25.dto.FileDto;
import com.elnar.module25.entity.File;
import com.elnar.module25.mapper.FileMapper;
import com.elnar.module25.security.CustomPrincipal;
import com.elnar.module25.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
  private final FileService fileService;
  private final FileMapper fileMapper;

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<FileDto>> getFileById(@PathVariable("id") Long id) {
    return fileService
        .getById(id)
        .map(fileMapper::map)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping()
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Flux<FileDto> getAllFiles() {
    return fileService.getAll().map(fileMapper::map);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public Mono<ResponseEntity<FileDto>> saveFile(@Valid @RequestBody FileDto fileDto) {
    File file = fileMapper.map(fileDto);
    return fileService
        .save(file)
        .map(fileMapper::map)
        .map(savedFile -> ResponseEntity.status(HttpStatus.CREATED).body(savedFile));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<FileDto>> updateFile(
      @PathVariable("id") Long id, @Valid @RequestBody FileDto fileDto) {
    return fileService
        .getById(id)
        .flatMap(
            existingFile -> {
              File fileToUpdate = fileMapper.map(fileDto);
              fileToUpdate.setId(existingFile.getId());
              return fileService.update(fileToUpdate).map(fileMapper::map).map(ResponseEntity::ok);
            })
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<Void>> deleteFileById(@PathVariable("id") Long id) {
    return fileService
        .getById(id)
        .flatMap(
            file ->
                fileService
                    .deleteById(file.getId())
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
        .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
  }

  @PostMapping("/upload")
  @PreAuthorize("hasAnyAuthority('USER', 'MODERATOR', 'ADMIN')")
  public Mono<ResponseEntity<File>> uploadFile(
      Authentication authentication, @RequestPart("file") Mono<FilePart> filePart) {
    CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
    Long userId = customPrincipal.getId();

    return filePart
        .flatMap(file -> fileService.uploadFile(file, userId))
        .map(ResponseEntity::ok)
        .onErrorResume(
            e -> {
              return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            });
  }
}
