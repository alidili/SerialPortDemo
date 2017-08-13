package com.yang.serialport.exception;

public class TooManyListeners extends Exception {

	private static final long serialVersionUID = 1L;

	public TooManyListeners() {
	}

	@Override
	public String toString() {
		return "串口监听类数量过多！添加操作失败！";
	}
}
