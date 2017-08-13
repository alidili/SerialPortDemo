package com.yang.serialport.exception;

public class SendDataToSerialPortFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public SendDataToSerialPortFailure() {
	}

	@Override
	public String toString() {
		return "往串口发送数据失败！";
	}
}
