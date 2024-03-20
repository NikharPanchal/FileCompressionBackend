package com.filecompression.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileVo {
	
	private String responseId;
	
	private String fileName;
	
	private boolean status;
}
