package com.filecompression.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.filecompression.entity.FileCompression;

public interface FileCompressionRepository extends MongoRepository<FileCompression, String> {

	@Query("{'_id':?0}")
	FileCompression findByFileId(String findByFileId);
}
