package com.filecompression.service;

import java.io.InputStream;

import com.filecompression.entity.FileCompression;
import com.filecompression.entity.FileVo;

import jakarta.servlet.http.HttpServletResponse;

public interface FileCompressionService {

	public byte[] addContentToZipFile(InputStream inputStream, String fileName);
	
	public void saveZipFile(byte[] zipContent, String filename, HttpServletResponse response);
	
	public FileVo saveZipContentToDatabase(byte[] zipContent, String filename, String Path);

	public FileCompression getFilebyFileId(String fileId);
}
