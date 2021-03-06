package org.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyClient {
	private int port;
    private String host;
    private SocketChannel socketChannel;

    public NettyClient(String host,int port) throws InterruptedException {
        this.port = port;
        this.host = host;
        start();
    }

    private void start() throws InterruptedException {
           EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
//            .childHandler(new HttpServletChannelInitializer());
            bootstrap.remoteAddress(host, port);
            
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NettyClientHandler());
                    socketChannel.pipeline().addLast(new HttpClientCodec());
//                    socketChannel.pipeline().addLast(new HttpServerCodec());
                    socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));//1M
                    socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                }
            });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                System.out.println("------connect server success------");
            }
            socketChannel.writeAndFlush("<xml>");
            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();    
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient("172.19.10.5",8090);
    }
}

