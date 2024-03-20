package com.filecompression.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileCompression {

	@Id
	private String id;

	@Field(name = "fileName")
	private String fileName;

	@Field(name = "filePath")
	private String filePath;

	@Field(name = "fileContent")
	private byte[] fileContent;
}
