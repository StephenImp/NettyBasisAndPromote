Netty入门教程之年粘包分包分析

1、消息如何在管道中流转

当前的一个handler如何往下面的一个handler传递一个对象

一个管道中会有多个handler

Netty 把整个消息封装成一个事件（event）
把event传递到整个管道中
管道中会有很多的handler
handler处理完消息后，会产生一个新的handler event
新的handler event会传递给下一个handler
一个handler不一定只产生一个handler event  ,也可以产生多个handler event
多个handler event也可以继续向下传递

所有的handler处理完event后，整个流程结束。
整个事件的源头是worker


一个线程分配一个selector
boss selector 负责监听端口
worker selector 负责channel的读写任务


handler往下传递对象的方法是sendUpstream(event)

2、看下粘包和分包是怎么样一个情况
hello hello

定义一个稳定的结构 length + hello


心中会有连个疑惑

1、为什么FrameDecoder return的对象就是往下传递的对象  （还是调用了sendUpstream）

2、buffer里面数据未被读取完怎么办？    （cumulation缓存）

3、为什么return null就可以缓存buffer     （cumulation缓存）

=============================分割线===========================

3、FrameDecoder里面的cumulation其实就是一个缓存的buffer对象


包头+长度+数据

Intger.max

把长度定义的很大，这种数据包，通常被称为socket攻击，字节流式攻击

2048

分包截断








