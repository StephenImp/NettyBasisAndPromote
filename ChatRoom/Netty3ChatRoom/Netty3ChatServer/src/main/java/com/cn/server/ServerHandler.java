package com.cn.server;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.cn.common.core.model.Request;
import com.cn.common.core.model.Response;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.serial.Serializer;
import com.cn.common.core.session.Session;
import com.cn.common.core.session.SessionImpl;
import com.cn.common.core.session.SessionManager;
import com.cn.common.module.ModuleId;
import com.cn.server.module.player.dao.entity.Player;
import com.cn.server.scanner.Invoker;
import com.cn.server.scanner.InvokerHoler;
import com.google.protobuf.GeneratedMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息接受处理类
 */
public class ServerHandler extends SimpleChannelHandler {

	/**
	 * 业务线程池
	 */
	public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	/**
	 * 接收消息,当前线程处于worker线程中。还处理了业务逻辑部分
	 * 如果处理业务时间较长，会形成阻塞.
	 *
	 * 业务可以异步去处理。新建一个线程去执行。
	 * netty3，当你在异步线程池中回写消息时，
	 * netty3才会去异步的回写消息
	 *
	 * 否则还是在同一个worker中。
	 *
	 *
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

		/**
		 * 验证是否为同一个线程
		 */
		System.out.println("ServerHandler=========="+Thread.currentThread().getName());

		Request request = (Request)e.getMessage();

		Runnable runnable = handlerMessage(new SessionImpl(ctx.getChannel()), request);

		/**
		 * 异步的方式去处理业务，就不会造成阻塞。
		 */
		executorService.execute(runnable);

	}
	
	
	/**
	 * 消息处理
	 * @param request
	 */
	private Runnable handlerMessage(Session session, Request request){

	return	new Runnable(){

			@Override
			public void run() {

				Response response = new Response(request);

				System.out.println("module:"+request.getModule() + "   " + "cmd：" + request.getCmd());

				//获取命令执行器
				Invoker invoker = InvokerHoler.getInvoker(request.getModule(), request.getCmd());
				if(invoker != null){
					try {
						Result<?> result = null;
						//假如是玩家模块传入channel参数，否则传入playerId参数
						if(request.getModule() == ModuleId.PLAYER){
							result = (Result<?>)invoker.invoke(session, request.getData());
						}else{

							System.out.println("从session中获取绑定对象。");
							/**
							 * 从session中获取绑定对象。
							 * 在玩家模块登录时，进行绑定。
							 */
							Object attachment = session.getAttachment();
							if(attachment != null){
								Player player = (Player) attachment;
								result = (Result<?>)invoker.invoke(player.getPlayerId(), request.getData());
							}else{
								//会话未登录拒绝请求
								response.setStateCode(ResultCode.LOGIN_PLEASE);
								session.write(response);
								return;
							}
						}

						//判断请求是否成功
						if(result.getResultCode() == ResultCode.SUCCESS){
							//回写数据
							Object object = result.getContent();
							if(object != null){
								if(object instanceof Serializer){
									System.out.println("自定义协议对象");
									Serializer content = (Serializer)object;
									response.setData(content.getBytes());
								}else if(object instanceof GeneratedMessage){
									System.out.println("Protobuf协议对象");
									GeneratedMessage content = (GeneratedMessage)object;
									response.setData(content.toByteArray());
								}else{
									System.out.println(String.format("不可识别传输对象:%s", object));
								}
							}
							session.write(response);
						}else{
							//返回错误码
							response.setStateCode(result.getResultCode());
							session.write(response);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						//系统未知异常
						response.setStateCode(ResultCode.UNKOWN_EXCEPTION);
						session.write(response);
					}
				}else{
					//未找到执行者
					response.setStateCode(ResultCode.NO_INVOKER);
					session.write(response);
					return;
				}
			}
		};

	}
	
	/**
	 * 断线移除会话
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Session session = new SessionImpl(ctx.getChannel());
		Object object = session.getAttachment();
		if(object != null){
			Player player = (Player)object;
			SessionManager.removeSession(player.getPlayerId());
		}
	}
}
