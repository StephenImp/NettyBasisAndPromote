package com.cn.serial;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * NIO中int类型 与 byte数据 序列化。
 */
public class Test2 {

	public static void main(String[] args) {
		int id = 101;
		int age = 21;

		/**
		 * 缺点：一开始指定分配的内存大小。
		 * 		没有扩容的功能。
		 */
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putInt(id);
		buffer.putInt(age);

		//序列化后的byte数组
		byte[] array = buffer.array();
		System.out.println(Arrays.toString(array));
		
		//====================================================

		//将byte数组反序列化
		ByteBuffer buffer2 = ByteBuffer.wrap(array);
		System.out.println("id:"+buffer2.getInt());
		System.out.println("age:"+buffer2.getInt());

	}

}
