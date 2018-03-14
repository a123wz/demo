package org.demo.netty.test;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter {  
	  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        try {  
        	Response response = (Response) msg;

            System.out.println("Client:"+ response.getId() + "," + response.getName() + "," +response.getResponseMessage());
        } finally {  
            ReferenceCountUtil.release(msg);  
        }  
    }  
  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        ctx.close();  
    }  
  
}  