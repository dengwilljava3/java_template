package com.ay.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Decoder;

public class FileUtil {

	public static String encryptToBase64(byte[] b) {
		return Base64.getEncoder().encodeToString(b);
	}

	public static String encryptToBase64(String filePath) {
		if (filePath == null) {
			return null;
		}
		try {
			byte[] b = Files.readAllBytes(Paths.get(filePath));
			return Base64.getEncoder().encodeToString(b);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean generateBase64StringToFile(String fileStr, String fileFilePath) {
		if (fileStr == null) // 文件base64位数据为空
			return false;
		try {
			// Base64解码
			byte[] b = Base64.getDecoder().decode(fileStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成文件
			OutputStream out = new FileOutputStream(fileFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
