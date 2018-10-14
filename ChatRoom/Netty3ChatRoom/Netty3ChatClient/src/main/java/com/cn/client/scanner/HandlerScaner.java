package com.cn.client.scanner;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
/**
 * handler扫描器
 */
@Component
public class HandlerScaner implements BeanPostProcessor {

	/**
	 * bean初始化之前执行
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * bean初始化之后执行。
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class<? extends Object> clazz = bean.getClass();
		
		Class<?>[] interfaces = clazz.getInterfaces();
		
		if(interfaces != null && interfaces.length > 0){
			//扫描类的所有接口父类
			for (Class<?> interFace : interfaces) {
				//判断是否为handler接口类
				SocketModule socketModule = interFace.getAnnotation(SocketModule.class);
				if (socketModule == null) {
					continue;
				}
				
				//找出命令方法(该注解标注在方法上)
				Method[] methods = interFace.getMethods();
				if(methods != null && methods.length > 0){
					for(Method method : methods){
						SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
						if(socketCommand == null){
							continue;
						}
						
						final short module = socketModule.module();
						final short cmd = socketCommand.cmd();


						/**
						 * 在bean的后置处理器中
						 * 把method对象和bean对象抽象成一个执行器。
						 * 存到InvokerHoler中保存。
						 */
						Invoker invoker = Invoker.valueOf(method, bean);
						if(InvokerHoler.getInvoker(module, cmd) == null){
							InvokerHoler.addInvoker(module, cmd, invoker);
						}else{
							System.out.println("重复命令:"+"module:"+module +" "+"cmd：" + cmd);
						}
					}
				}
				
			}
		}
		return bean;
	}

}
