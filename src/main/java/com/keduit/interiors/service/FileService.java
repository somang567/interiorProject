package com.keduit.interiors.service;

import org.springframework.stereotype.Service;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {	//업로드 해주고
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

		// Universally Unique IdentiFier : 범용 고유 식별자, 중복되지 않는 유일한 값을 구성할 때 사용
		UUID uuid = UUID.randomUUID();
		String extention = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extention;
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		fos.write(fileData);
		fos.close();
		return savedFileName;
	}
	public void deleteFile(String filePath) throws Exception {
		// 저장된 파일의 경로를 이용하여 파일 객체를 생성
		File deleteFile = new File(filePath);

		// 해당 파일이 있으면 삭제
		if(deleteFile.exists()){
			deleteFile.delete();
			log.info("파일을 삭제하였습니다.");
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	}
}
