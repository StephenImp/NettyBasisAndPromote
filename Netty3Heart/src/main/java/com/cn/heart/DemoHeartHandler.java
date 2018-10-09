package com.cn.heart;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.jboss.netty.handler.timeout.IdleStateHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018\10\9 0009.
 */
public class DemoHeartHandler extends IdleStateAwareChannelHandler implements ChannelHandler {

    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("ss");
        System.out.println(e.getState()+" "+dateFormat.format(new Date()));
    }

}
