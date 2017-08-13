package com.yang.serialport.exception;

public class SerialPortOutputStreamCloseFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public SerialPortOutputStreamCloseFailure() {
	}

	@Override
	public String toString() {
		return "关闭串口对象的输出流（OutputStream）时出错！";
	}
}
