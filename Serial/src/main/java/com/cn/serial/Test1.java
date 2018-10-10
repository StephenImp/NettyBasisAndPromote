package com.cn.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * IO中。
 * int类型的数据与byte类型的数据  的序列化转换过程。
 */
public class Test1 {

	public static void main(String[] args) throws IOException {
		int id = 101;
		int age = 21;
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		arrayOutputStream.write(int2bytes(id));
		arrayOutputStream.write(int2bytes(age));
		
		byte[] byteArray = arrayOutputStream.toByteArray();
		
		System.out.println(Arrays.toString(byteArray));
		//[0, 0, 0, 101, 0, 0, 0, 21]
		
		//==============================================================
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
		byte[] idBytes = new byte[4];
		arrayInputStream.read(idBytes);
		System.out.println("id:" + bytes2int(idBytes));
		
		byte[] ageBytes = new byte[4];
		arrayInputStream.read(ageBytes);
		System.out.println("age:" + bytes2int(ageBytes));
		
	}
	
	
	/**
	 * 大端字节序列(先写高位，再写低位)
	 * 		还有一种是小端序列(先写低位，再写高位)
	 *
	 * 百度下 大,小端字节序列
	 * @param i
	 * @return
	 */
	public static byte[] int2bytes(int i){
		/**
		 * 大端字节序列(先写高位，再写低位)
		 * int 数据在内存中占4个字节
		 */
		byte[] bytes = new byte[4];
		bytes[0] = (byte)(i >> 3*8);//字节数组0 存放 int 最高位的一个字节  一个字节占8位  以此类推。
		bytes[1] = (byte)(i >> 2*8);
		bytes[2] = (byte)(i >> 1*8);
		bytes[3] = (byte)(i >> 0*8);
		return bytes;
	}
	
	
	/**
	 * 大端
	 * @param bytes
	 * @return
	 */
	public static int bytes2int(byte[] bytes){
		return (bytes[0] << 3*8) |
				(bytes[1] << 2*8) |
				(bytes[2] << 1*8) |
				(bytes[3] << 0*8);
	}

}
