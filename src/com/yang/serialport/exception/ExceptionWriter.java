package com.yang.serialport.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 负责将传入的Exception中的错误信息提取出来并转换成字符串；
 * 
 * @author yangle
 */
public class ExceptionWriter {

	/**
	 * 将Exception中的错误信息封装到字符串中并返回该字符串
	 * 
	 * @param e
	 *            包含错误的Exception
	 * @return 错误信息字符串
	 */
	public static String getErrorInfoFromException(Exception e) {

		StringWriter sw = null;
		PrintWriter pw = null;

		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";

		} catch (Exception e2) {
			return "出错啦！未获取到错误信息，请检查后重试!";
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (sw != null) {
					sw.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
