package com.cn.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.cn.codc.RequestDecoder;
import com.cn.codc.ResponseEncoder;
/**
 * netty服务端入门
 */
public class Server {

	public static void main(String[] args) {

		//服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		//boss线程监听端口，worker线程负责数据读写
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		
		//设置niosocket工厂
		bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
		
		//设置管道的工厂
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {

				ChannelPipeline pipeline = Channels.pipeline();
				//服务器端接收客户端发送过来的请求，并解码。
				pipeline.addLast("decoder", new RequestDecoder());
				//响应给客户端时，进行编码。
				pipeline.addLast("encoder", new ResponseEncoder());
				pipeline.addLast("helloHandler", new HelloHandler());
				return pipeline;
			}
		});
		
		bootstrap.bind(new InetSocketAddress(10101));
		
		System.out.println("start!!!");
		
	}

}
