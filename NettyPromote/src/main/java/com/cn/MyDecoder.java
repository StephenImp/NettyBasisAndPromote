package com.cn;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class MyDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

		//可读的数据包长度大于4时
		if(buffer.readableBytes() > 4){

			/**
			 * 防止socket攻击
			 * 如果可读数据的buffer大于2048
			 * 就把buffer中的数据清空掉。
			 * 但这里会有分包截断问题
			 * 所以要加包头标识。
			 *
			 * 包头+长度+*****+数据体
			 *
			 * 详见USDPacPro  RequestDecoder.
			 */
			if(buffer.readableBytes() > 2048){

				buffer.skipBytes(buffer.readableBytes());
			}

			//标记
			buffer.markReaderIndex();
			//长度
			int length = buffer.readInt();

			//buffer中可读数据的长度小于可读数据的长度
			//这里length是在client端定义发送长度时，int类型的长度
			if(buffer.readableBytes() < length){

				//在前面做了标记。当buffer中可读数据的长度小于4时，把读指针还原。
				buffer.resetReaderIndex();
				//缓存当前剩余的buffer数据，等待剩下数据包到来
				return null;
			}
			
			//读数据
			byte[] bytes = new byte[length];
			buffer.readBytes(bytes);
			//往下传递对象
			return new String(bytes);
		}
		//缓存当前剩余的buffer数据，等待剩下数据包到来
		return null;
	}

}
