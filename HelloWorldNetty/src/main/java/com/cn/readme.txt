netty 快速入门教程


netty版本大致版本分为  netty3.x  和  netty4.x、netty5.x

netty可以运用在那些领域？

1分布式进程通信
例如: hadoop、dubbo、akka等具有分布式功能的框架，底层RPC通信都是基于netty实现的，这些框架使用的版本通常都还在用netty3.x

2、游戏服务器开发
最新的游戏服务器有部分公司可能已经开始采用netty4.x 或 netty5.x


1、netty3服务端hello world案例

SimpleChannelHandler 处理消息接收和写
{
	messageReceived接收消息

	channelConnected新连接，通常用来检测IP是否是黑名单

	channelDisconnected链接关闭，可以再用户断线的时候清楚用户的缓存数据等
}

2、netty客户端hello world案例

channelDisconnected与channelClosed的区别？

channelDisconnected只有在连接建立后断开才会调用
channelClosed无论连接是否成功都会调用关闭资源


