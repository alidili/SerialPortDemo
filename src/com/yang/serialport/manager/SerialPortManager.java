package com.yang.serialport.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import com.yang.serialport.utils.ArrayUtils;
import com.yang.serialport.utils.ShowUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * 串口管理
 * 
 * @author yangle
 */
@SuppressWarnings("all")
public class SerialPortManager {

	/**
	 * 查找所有可用端口
	 * 
	 * @return 可用端口名称列表
	 */
	public static final ArrayList<String> findPorts() {
		// 获得当前所有可用串口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 将可用串口名添加到List并返回该List
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @return 串口对象
	 * @throws PortInUseException
	 *             串口已被占用
	 */
	public static final SerialPort openPort(String portName, int baudrate) throws PortInUseException {
		try {
			// 通过端口名识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
			CommPort commPort = portIdentifier.open(portName, 2000);
			// 判断是不是串口
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// 设置一下串口的波特率等参数
					// 数据位：8
					// 停止位：1
					// 校验位：None
					serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					e.printStackTrace();
				}
				return serialPort;
			}
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialport
	 *            待关闭的串口对象
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param order
	 *            待发送数据
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order) {
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 */
	public static byte[] readFromPort(SerialPort serialPort) {
		InputStream in = null;
		byte[] bytes = {};
		try {
			in = serialPort.getInputStream();
			// 缓冲区大小为一个字节
			byte[] readBuffer = new byte[1];
			int bytesNum = in.read(readBuffer);
			while (bytesNum > 0) {
				bytes = ArrayUtils.concat(bytes, readBuffer);
				bytesNum = in.read(readBuffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口存在有效数据监听
	 */
	public static void addListener(SerialPort serialPort, DataAvailableListener listener) {
		try {
			// 给串口添加监听器
			serialPort.addEventListener(new SerialPortListener(listener));
			// 设置当有数据到达时唤醒监听接收线程
			serialPort.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 串口监听
	 */
	public static class SerialPortListener implements SerialPortEventListener {

		private DataAvailableListener mDataAvailableListener;

		public SerialPortListener(DataAvailableListener mDataAvailableListener) {
			this.mDataAvailableListener = mDataAvailableListener;
		}

		public void serialEvent(SerialPortEvent serialPortEvent) {
			switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
				if (mDataAvailableListener != null) {
					mDataAvailableListener.dataAvailable();
				}
				break;

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
				break;

			case SerialPortEvent.CTS: // 3.清除待发送数据
				break;

			case SerialPortEvent.DSR: // 4.待发送数据准备好了
				break;

			case SerialPortEvent.RI: // 5.振铃指示
				break;

			case SerialPortEvent.CD: // 6.载波检测
				break;

			case SerialPortEvent.OE: // 7.溢位（溢出）错误
				break;

			case SerialPortEvent.PE: // 8.奇偶校验错误
				break;

			case SerialPortEvent.FE: // 9.帧错误
				break;

			case SerialPortEvent.BI: // 10.通讯中断
				ShowUtils.errorMessage("与串口设备通讯中断");
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 串口存在有效数据监听
	 */
	public interface DataAvailableListener {
		/**
		 * 串口存在有效数据
		 */
		void dataAvailable();
	}
}
