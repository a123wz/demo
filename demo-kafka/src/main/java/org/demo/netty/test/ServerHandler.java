package org.demo.netty.test;

import java.io.File;
import java.io.FileOutputStream;



import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler  extends ChannelHandlerAdapter {  
	  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
    	Request request = (Request) msg;

        System.out.println("Server:"+ request.getId() + "," + request.getName() + "," +request.getReqeustMessag());

 

        Response response = new Response();

        response.setId(request.getId());

        response.setName("response "+ request.getId());

        response.setResponseMessage("响应内容：" +request.getReqeustMessag());

        byte[] unGizpData =GzipUtils.unGzip(request.getAttachment());

        char separator = File.separatorChar;

        FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + separator +"recieve" + separator + "1.png");

        outputStream.write(unGizpData);

        outputStream.flush();

        outputStream.close();

        ctx.writeAndFlush(response);
              
  
    }  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        ctx.close();  
    }  
  
}  