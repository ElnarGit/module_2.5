package com.elnar.module25.service.impl;


import com.elnar.module25.entity.File;
import com.elnar.module25.repository.FileRepository;
import com.elnar.module25.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileRepository fileRepository;
	
	@Override
	public Mono<File> getById(Long id) {
		return fileRepository.findById(id)
				.doOnNext(user -> log.debug("Found file by id: {}", id))
				.doOnError(error -> log.error("Error finding file by id: {}", id, error));
	}
	
	@Override
	public Flux<File> getAll() {
		return fileRepository.findAll()
				.doOnNext(file -> log.debug("Retrieved file: {}", file.getId()))
				.doOnError(error -> log.error("Error retrieving files", error));
	}
	
	@Override
	public Mono<File> save(File file) {
		return fileRepository.save(file)
				.doOnNext(savedFile -> log.debug("Saved file with id: {}", savedFile.getId()))
				.doOnError(error -> log.error("Error saving file", error));
	}
	
	@Override
	public Mono<File> update(File file) {
		return fileRepository.save(file)
				.doOnNext(updatedFile -> log.debug("Updated file with id: {}", updatedFile.getId()))
				.doOnError(error -> log.error("Error updating file", error));
	}
	
	@Override
	public Mono<Void> deleteById(Long id) {
		return fileRepository.deleteById(id)
				.doOnSuccess(result -> log.debug("Deleted file with id: {}", id))
				.doOnError(error -> log.error("Error deleting file with id: {}", id, error));
	}
}
