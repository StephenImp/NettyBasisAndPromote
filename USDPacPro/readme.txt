Netty入门教程之自定义数据包协议

give me a coffee   give me a tea


give me a coffeegive me a tea   粘包现象

give me
 a coffeegive me a tea     分包现象


粘包和分包出现的原因是：没有一个稳定数据结构

分割符

give me a coffee|give me a tea|

give me a coffee|
give me a tea|


长度 + 数据(效率要高一些)

16give me a coffee13give me a tea

16give me a coffee
13give me a tea


***********************************************************************
客户端发送数据给服务端
服务端接收数据后再回写到客户端。




