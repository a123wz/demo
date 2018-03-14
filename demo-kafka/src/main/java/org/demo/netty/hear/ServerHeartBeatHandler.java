package org.demo.netty.hear;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHeartBeatHandler extends ChannelHandlerAdapter {

 

    private static Map<String, String>AUTH_IP_MAP = new HashMap<>();

    private static final String SUCCESS_KEY ="auth_success_key";

 

    static {

        AUTH_IP_MAP.put("192.168.3.176","1234");

    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("active:"+ctx.name());
//    	ctx.writeAndFlush(SUCCESS_KEY);
    	ChannelFuture future = ctx.channel().writeAndFlush(SUCCESS_KEY);
        System.out.println(future.getNow());
//    	super.channelActive(ctx);

    }
    
    private boolean auth(ChannelHandlerContext ctx, Object msg) {
        String[] rets = ((String)msg).split(",");
        String auth = AUTH_IP_MAP.get(rets[0]);
        System.out.println(auth);
        
        if(auth != null &&auth.equals(rets[1])) {

            ctx.writeAndFlush(SUCCESS_KEY);

            return true;

        } else {

            ctx.writeAndFlush("authfailure!").addListener(ChannelFutureListener.CLOSE);

            return false;

        }

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	System.out.println("msg:"+msg);
        if(msg instanceof String) {
            auth(ctx, msg);
        } else if(msg instanceof RequestInfo) {

            RequestInfo info = (RequestInfo)msg;

            System.out.println("----------------------------------------------");

            System.out.println("当前主机ip：" +info.getIp());

            System.out.println("当前主机cpu：情况");

            Map<String, Object> cpuMap =info.getCpuPercMap();

            System.out.println("总使用率：" +  cpuMap.get("combined"));

            System.out.println("用户使用率：" +cpuMap.get("user"));

            System.out.println("系统使用率：" +cpuMap.get("sys"));

            System.out.println("等待率：" +cpuMap.get("wait"));

            System.out.println("空闲率：" +cpuMap.get("idle"));

            System.out.println("当前主机memory情况：");

            Map<String, Object> memMap =info.getMemoryMap();

            System.out.println("内存总量：" +memMap.get("total"));

            System.out.println("当前内存使用量：" +memMap.get("used"));

            System.out.println("当前内存剩余量：" +memMap.get("free"));

           System.out.println("-----------------------------------------------");

            ctx.writeAndFlush("inforeceived!");

        } else {

            ctx.writeAndFlush("connectfailure").addListener(ChannelFutureListener.CLOSE);

        }

    }

}

