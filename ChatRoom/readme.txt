思路：(详见Netty3ChatRoom)

1.自定义注解
    首先，先定义自定义注解，作用于目标方法，或者类上

2.定义Invoker
    把method对象和bean对象抽象成一个执行器。

3.定义InvokerHoler用来存放所有Invoker

spring在扫描HandlerScaner 时，因为HandlerScaner implements BeanPostProcessor(bean的后置处理器方法)
所以在bean初始化之后（也可以再之前）扫描所有标注了@SocketModule注解的类，以及所有@SocketCommand的方法

将这些扫描到的bean与需要执行的方法(@SocketCommand的方法标注的方法)绑定起来（Invoker），
并存入InvokerHoler

那么在处理具体业务请求的handler中，就不需要各种if,else的判断了，直接从InvokerHoler中，取出Invoker去执行。

channel对象抽象成了一个session接口。(将netty对channel进行封装)
这样做的好处就是，不管是netty3还是netty4，只需要对session的实现类即可。

在server端ServerHandler中，对以上知识点进行应用。
            在ServerHandler中有一个从session中获取绑定对象。
           PlayerServiceImpl中，有一个把绑定对象放入session中。
           分段锁存session.

netty中的handler有点像Spring中的拦截器，个人理解。


异步处理业务请求
    方式① netty3 server ServerHandler中，处理消息时，新建线程池组去处理
    方式② netty4 server Server中 把busyGroup放到管道中，netty帮我们异步的去处理业务。

验证方法，同netty3 ServerHandler中，获取当前线程的线程名称

    方式②要好一些。有一种消息串行化的功能。(有序的请求处理线程池)
                    有序的任务队列
                    一个线程一个任务队列
                    每一个玩家分配一个线程。
