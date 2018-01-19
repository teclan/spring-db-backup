package com.znyw.tool;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FileUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
	// 默认每次读取 50 MB
	private static final int DEFAULT_READ_LENGTH = 50 * 1024 * 1024;

	public static boolean copyFile(File src, File des) {
		if (src.exists()) {
			try {
				new File(des.getParent()).mkdirs();

				FileInputStream fis = new FileInputStream(src);
				FileOutputStream fos = new FileOutputStream(des);

				int read = 0;
				int hasRead = 0;

				byte[] content = new byte[(int) (src.length() - hasRead > DEFAULT_READ_LENGTH ? DEFAULT_READ_LENGTH
						: (src.length() - hasRead))];

				while ((read = fis.read(content)) != -1) {
					fos.write(content);
					fos.flush();
					hasRead += read;

					if (hasRead == src.length()) {
						break;
					}
					content = new byte[(int) (src.length() - hasRead > DEFAULT_READ_LENGTH ? DEFAULT_READ_LENGTH
							: (src.length() - hasRead))];
				}
				fos.close();
				fis.close();
				return true;
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.warn("File is not exists ! {}", src.getAbsolutePath());
		}
		return false;
	}
	
	public static void copyDir(File srcDir, File desDir) {
		if (!srcDir.exists()) {
			LOGGER.warn("\nthe file {} is not exists!", srcDir.getAbsolutePath());
			return;
		}
		if (srcDir.isDirectory()) {
			File[] files = srcDir.listFiles();
			for (int i = 0; i < files.length; i++) {

				if (files[i].isDirectory()) {
					copyDir(files[i], new File(desDir.getAbsoluteFile() + File.separator + files[i].getName()));
				} else {
					copyFile(files[i], new File(desDir.getAbsoluteFile() + File.separator + files[i].getName()));
				}
			}
		}

	}

	public static void main(String[] args) {
		
		copyDir(new File("E:/1"), new File("E:/2"));
		System.err.println("===");

	}

}
