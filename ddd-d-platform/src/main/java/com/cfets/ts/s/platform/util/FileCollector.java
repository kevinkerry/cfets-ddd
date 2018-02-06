package com.cfets.ts.s.platform.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileCollector {

	public static List<File> collectPom(String filePath) throws Exception {
		ArrayList<File> fileList = new ArrayList<File>();
		findPomFiles(filePath, "pom.xml", fileList);
		return fileList;
	}

	public static void findPomFiles(String basePath, String targetFileName, List<File> fileList) {
		File baseFile = new File(basePath);
		if(!baseFile.isDirectory()){
			fileList.add(baseFile);
			return;
		}
		File[] listFiles = baseFile.listFiles();
		for (File file : listFiles) {
			if (file.isDirectory()) {
				findPomFiles(file.getAbsolutePath(), targetFileName, fileList);
			} else if (file.isFile()) {
				if (file.getName().equals(targetFileName)
						&& !file.getPath().contains(File.separator + "target" + File.separator)) {
					fileList.add(file);
				}
			}
		}
	}

	public static void findFiles(String basePath, String targetFileName, List<File> fileList) {
		File baseFile = new File(basePath);
		if(!baseFile.isDirectory()){
			fileList.add(baseFile);
			return;
		}
		File[] listFiles = baseFile.listFiles();
		for (File file : listFiles) {
			if (file.isDirectory()) {
				findFiles(file.getAbsolutePath(), targetFileName, fileList);
			} else if (file.isFile()) {
				if (file.getName().equals(targetFileName)) {
					fileList.add(file);
				}
			}
		}
	}

}
