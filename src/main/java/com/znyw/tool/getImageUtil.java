package com.znyw.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class getImageUtil {
	public static byte[] GetImage(String path){
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(path));
			int filelong = fis.available();
			byte[] long_buf = new byte[filelong];
			fis.read(long_buf);
			return long_buf;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
