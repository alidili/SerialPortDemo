package com.yang.serialport.exception;

public class SerialPortInputStreamCloseFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public SerialPortInputStreamCloseFailure() {
	}

	@Override
	public String toString() {
		return "关闭串口对象输入流（InputStream）时出错！";
	}
}
