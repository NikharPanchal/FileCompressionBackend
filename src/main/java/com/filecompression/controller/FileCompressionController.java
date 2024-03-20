package com.filecompression.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.filecompression.entity.FileCompression;
import com.filecompression.entity.FileVo;
import com.filecompression.service.FileCompressionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/file")
@Slf4j
@CrossOrigin("*")
public class FileCompressionController {

	@Autowired
	private FileCompressionService compressionService;

	private static final Logger log = LoggerFactory.getLogger(FileCompressionController.class);
	
	private static final String DIRECTORY= System.getProperty("user.home") + "/Downloads/"; 

	@GetMapping("/test")
	public String hello() {
		return "welcome to file compression";
	}

	@PostMapping("/upload")
	public ResponseEntity<FileVo> compressFile(@RequestParam("file") MultipartFile file) {
		try {
			FileVo saveZipContentToDatabase = null;
			if (file != null && file.getSize() > 0) {
				log.info("User file name :: {} , size :: {}  ", file.getOriginalFilename(), file.getSize());
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				Path fileStorage = Paths.get(DIRECTORY, fileName).toAbsolutePath().normalize();
				Files.copy(file.getInputStream(), fileStorage, StandardCopyOption.REPLACE_EXISTING);
				byte[] zipContent = compressionService.addContentToZipFile(file.getInputStream(), fileName);
				saveZipContentToDatabase = compressionService.saveZipContentToDatabase(zipContent, fileName,
						fileStorage.toAbsolutePath().toString());
				return ResponseEntity.ok(saveZipContentToDatabase);
			} else {
				log.error("File not uploaded or it's empty");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(saveZipContentToDatabase);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in compress file. message :: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/download/{fileId}")
	public ResponseEntity<byte[]> getFile(@PathVariable("fileId") String fileId) throws IOException {
		FileCompression file = compressionService.getFilebyFileId(fileId);
		if (file != null) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(file.getFileContent());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
//	@GetMapping("/download/{fileName}")
//	public ResponseEntity<UrlResource> downloadFile(@PathVariable("fileName") String fileName) throws IOException{
//		Path filePath =Paths.get(DIRECTORY).toAbsolutePath().normalize().resolve(fileName);
//		if(!Files.exists(filePath)) {
//			throw new FileNotFoundException( fileName + "File not found in our server");
//		}
//		UrlResource resource=new UrlResource(filePath.toUri());
//		HttpHeaders header=new HttpHeaders();
//		header.add("File-name", fileName);
//		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
//		
//		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath))).headers(header).body(resource);
//	}
}
