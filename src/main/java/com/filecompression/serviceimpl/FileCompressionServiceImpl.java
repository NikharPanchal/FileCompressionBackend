package com.filecompression.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filecompression.entity.FileCompression;
import com.filecompression.entity.FileVo;
import com.filecompression.repository.FileCompressionRepository;
import com.filecompression.service.FileCompressionService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileCompressionServiceImpl implements FileCompressionService {

	@Autowired
	private FileCompressionRepository fileRepo;

	@Override
	public byte[] addContentToZipFile(InputStream inputStream, String fileName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
			ZipEntry entry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(entry);
			IOUtils.copy(inputStream, zipOutputStream);
			zipOutputStream.closeEntry();
			zipOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in addContentToZipFile. message :: {}", e.getMessage());
		}
		return outputStream.toByteArray();
	}

	@Override
	public void saveZipFile(byte[] zipContent, String filename, HttpServletResponse response) {
		try {
			if (!response.isCommitted()) {
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".zip");
				try (ServletOutputStream outputStream = response.getOutputStream()) {
					outputStream.write(zipContent);
					outputStream.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in saveZipFile. message :: {}", e.getMessage());
		}
	}

	@Override
	public FileVo saveZipContentToDatabase(byte[] zipContent, String filename,String filePath) {
		try {
			FileCompression file=FileCompression.builder().fileName(filename).fileContent(zipContent).filePath(filePath).build();
			FileCompression save = fileRepo.save(file);
			if(save!=null) {
				FileVo response = FileVo.builder().responseId(save.getId()).fileName(save.getFileName()).status(true)
						.build();
				return response;
			}
			else
			{
				FileVo response=FileVo.builder().status(false).build();
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in saveZipContentToDatabase. message :: {}", e.getMessage());
		}
		return null;
	}

	@Override
	public FileCompression getFilebyFileId(String fileId) {
		FileCompression fileCompression = new FileCompression();
		try {
			fileCompression = fileRepo.findByFileId(fileId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("General Error in getFileByFileName. message :: {}", e.getMessage());
		}
		return fileCompression;
	}
}