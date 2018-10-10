package com.cn.codc;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.cn.constant.ConstantValue;
import com.cn.model.Request;

/**
 * 请求编码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * | 包头          | 模块号        | 命令号      |  长度        |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节short
 * 命令号2字节short
 * 长度4字节(描述数据部分字节长度)
 * 
 */
public class RequestEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext context, Channel channel, Object rs) throws Exception {

		/**
		 * 接收到的message
		 */
		Request request = (Request)(rs);

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		//包头(数据结构的开始，某些情况下，使数据结构更加的稳定。)
		buffer.writeInt(ConstantValue.FLAG);
		//module
		buffer.writeShort(request.getModule());
		//cmd
		buffer.writeShort(request.getCmd());
		//长度
		buffer.writeInt(request.getDataLength());
		//data
		if(request.getData() != null){
			buffer.writeBytes(request.getData());
		}
		
		return buffer;
	}

}
