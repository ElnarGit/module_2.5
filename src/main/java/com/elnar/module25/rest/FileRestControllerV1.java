package com.elnar.module25.rest;


import com.elnar.module25.dto.FileDto;
import com.elnar.module25.entity.File;
import com.elnar.module25.mapper.FileMapper;
import com.elnar.module25.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public Mono<ResponseEntity<FileDto>> getFileById(@PathVariable("id") Long id){
		return fileService.getById(id)
				.map(fileMapper::map)
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping()
	public Flux<FileDto> getAllFiles(){
		return fileService.getAll()
				.map(fileMapper::map);
	}
	
	@PostMapping
	public Mono<ResponseEntity<FileDto>> saveFile(@Valid @RequestBody FileDto fileDto){
		File file = fileMapper.map(fileDto);
		return fileService.save(file)
				.map(fileMapper::map)
				.map(savedFile -> ResponseEntity.status(HttpStatus.CREATED).body(savedFile));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<FileDto>> updateFile(@PathVariable("id") Long id, @Valid @RequestBody FileDto fileDto){
		return fileService.getById(id)
				.flatMap(existingFile -> {
					File fileToUpdate = fileMapper.map(fileDto);
					fileToUpdate.setId(existingFile.getId());
					return fileService.update(fileToUpdate)
							.map(fileMapper::map)
							.map(ResponseEntity::ok);
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteFileById(@PathVariable("id") Long id){
		return fileService.getById(id)
				.flatMap(file -> fileService.deleteById(file.getId())
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
				.switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
	}
}
