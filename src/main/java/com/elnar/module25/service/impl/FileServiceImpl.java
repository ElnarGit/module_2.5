package com.elnar.module25.service.impl;

import com.elnar.module25.entity.Event;
import com.elnar.module25.entity.File;
import com.elnar.module25.entity.Status;
import com.elnar.module25.entity.User;
import com.elnar.module25.exception.NotFoundException;
import com.elnar.module25.repository.EventRepository;
import com.elnar.module25.repository.FileRepository;
import com.elnar.module25.repository.UserRepository;
import com.elnar.module25.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
  private final FileRepository fileRepository;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  @Override
  public Mono<File> getById(Long id) {
    return fileRepository
        .findById(id)
        .doOnNext(user -> log.debug("Found file by id: {}", id))
        .doOnError(error -> log.error("Error finding file by id: {}", id, error));
  }

  @Override
  public Flux<File> getAll() {
    return fileRepository
        .findAll()
        .doOnNext(file -> log.debug("Retrieved file: {}", file.getId()))
        .doOnError(error -> log.error("Error retrieving files", error));
  }

  @Override
  public Mono<File> save(File file) {
    return fileRepository
        .save(file)
        .doOnNext(savedFile -> log.debug("Saved file with id: {}", savedFile.getId()))
        .doOnError(error -> log.error("Error saving file", error));
  }

  @Override
  public Mono<File> update(File file) {
    return fileRepository
        .save(file)
        .doOnNext(updatedFile -> log.debug("Updated file with id: {}", updatedFile.getId()))
        .doOnError(error -> log.error("Error updating file", error));
  }

  @Override
  public Mono<Void> deleteById(Long id) {
    return fileRepository
        .deleteById(id)
        .doOnSuccess(result -> log.debug("Deleted file with id: {}", id))
        .doOnError(error -> log.error("Error deleting file with id: {}", id, error));
  }

  @Override
  public Mono<File> uploadFile(FilePart filePart, Long userId) {
    return validateUser(userId)
        .flatMap(
            user -> {
              String fileName = UUID.randomUUID() + "_" + filePart.filename();
              Path uploadPath = Paths.get("uploads");
              Path filePath = uploadPath.resolve(fileName);

              return createDirectoryIfNotExists(uploadPath)
                  .then(filePart.transferTo(filePath))
                  .doOnSuccess(unused -> log.debug("File transferred to path: {}", filePath))
                  .doOnError(
                      error -> log.error("Error transferring file to path: {}", filePath, error))
                  .then(createFile(filePath.toString()))
                  .flatMap(
                      savedFile ->
                          createEvent(user.getId(), savedFile.getId()).thenReturn(savedFile));
            });
  }

  private Mono<User> validateUser(Long userId) {
    return userRepository
        .findById(userId)
        .switchIfEmpty(
            Mono.error(
                new NotFoundException("User not found with id: " + userId, "USER_NOT_FOUND")));
  }

  private Mono<Void> createDirectoryIfNotExists(Path uploadPath) {
    return Mono.fromRunnable(
        () -> {
          try {
            if (!Files.exists(uploadPath)) {
              Files.createDirectories(uploadPath);
            }
          } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for file upload", e);
          }
        });
  }

  private Mono<File> createFile(String filePath) {
    File file = File.builder().location(filePath).status(Status.ACTIVE).build();

    return fileRepository
        .save(file)
        .doOnNext(savedFile -> log.debug("File saved with ID: {}", savedFile.getId()))
        .doOnError(error -> log.error("Error saving file entity", error));
  }

  private Mono<Event> createEvent(Long userId, Long fileId) {
    Event event = Event.builder().userId(userId).fileId(fileId).status(Status.ACTIVE).build();

    return eventRepository
        .save(event)
        .doOnNext(savedEvent -> log.debug("Event saved with ID: {}", savedEvent.getId()))
        .doOnError(error -> log.error("Error saving event entity", error));
  }
}
