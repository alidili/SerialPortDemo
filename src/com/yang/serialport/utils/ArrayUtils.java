package com.yang.serialport.utils;

/**
 * 数组工具
 * 
 * @author yangle
 */
public class ArrayUtils {

	/**
	 * 合并数组
	 * 
	 * @param firstArray
	 *            第一个数组
	 * @param secondArray
	 *            第二个数组
	 * @return 合并后的数组
	 */
	public static byte[] concat(byte[] firstArray, byte[] secondArray) {
		if (firstArray == null || secondArray == null) {
			return null;
		}
		byte[] bytes = new byte[firstArray.length + secondArray.length];
		System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
		System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
		return bytes;
	}
}
