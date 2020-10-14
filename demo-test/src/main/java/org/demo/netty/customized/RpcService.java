package org.demo.netty.customized;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.demo.netty.customized.config.NettyServerConfig;
import org.demo.netty.customized.v1.ProtocolConstants;
import org.demo.netty.customized.v1.RpcMessage;
import org.demo.netty.customized.v1.V1Decoder;
import org.demo.netty.customized.v1.V1Encoder;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 09:30:00
 */
@Slf4j
public class RpcService extends RpcHandler{

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private final EventLoopGroup eventLoopGroupBoss;
    private int bossThreadSize = 10;
    private int workThreadSize = 10;
    private int listenPort = 9092;


    public RpcService(){
//        this.nettyServerConfig = nettyServerConfig;
        if (false) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(bossThreadSize,
                    new NamedThreadFactory("service_boss", 10));
            this.eventLoopGroupWorker = new EpollEventLoopGroup(workThreadSize,
                    new NamedThreadFactory("service_worker",
                            10));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(bossThreadSize,
                    new NamedThreadFactory("service_boss",
                            10));
            this.eventLoopGroupWorker = new NioEventLoopGroup(workThreadSize,
                    new NamedThreadFactory("service_worker",
                            10));
        }

        // init listenPort in constructor so that getListenPort() will always get the exact port
//        setListenPort(nettyServerConfig.getDefaultListenPort());
    }


    public void start() {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupWorker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, nettyServerConfig.getSoBackLogSize())
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSendBufSize())
                .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketResvBufSize())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(nettyServerConfig.getWriteBufferLowWaterMark(),
                                nettyServerConfig.getWriteBufferHighWaterMark()))
                .localAddress(new InetSocketAddress(listenPort))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0))
                                .addLast(new V1Decoder())
                                .addLast(new V1Encoder()).addLast(new ServerHandler());
//                        if (null != channelHandlers) {
//                            addChannelPipelineLast(ch, channelHandlers);
//                        }

                    }
                });

        try {
//            this.serverBootstrap.ch
//            serverBootstrap.setChannelHandlers(handlers);
            ChannelFuture future = this.serverBootstrap.bind(listenPort).sync();
//            LOGGER.info("Server started ... ");
//            RegistryFactory.getInstance().register(new InetSocketAddress(XID.getIpAddress(), XID.getPort()));
//            initialized.set(true);
            future.channel().closeFuture().sync();
        } catch (Exception exx) {
            throw new RuntimeException(exx);
        }

    }


    private AtomicInteger count = new AtomicInteger(1);



    @ChannelHandler.Sharable
    public class ServerHandler extends AbstractHandler {

        /**
         * Dispatch.
         *
         * @param request the request
         * @param ctx     the ctx
         */
        @Override
        public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
            log.info("服务器接收数据:{}", JSON.toJSONString(request));
            RpcMessage response = new RpcMessage();
            response.setId(count.incrementAndGet()).setMessageType(((RpcMessage) request).getMessageType());
            switch (((RpcMessage) request).getMessageType()){
                case 1:
                    ctx.writeAndFlush(response.setCodec((byte)1).setCompressor((byte)1));
                    break;
                case 2:
                    ctx.writeAndFlush(response.setCodec((byte)2).setCompressor((byte)2));
                    break;
                default:
                    ctx.writeAndFlush(response.setCodec((byte)3).setCompressor((byte)3));
            }
        }

        /**
         * Channel read.
         *
         * @param ctx the ctx
         * @param msg the msg
         * @throws Exception the exception
         */
        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof RpcMessage) {
//            RpcMessage rpcMessage = (RpcMessage) msg;
//            debugLog("read:" + rpcMessage.getBody());
//            if (rpcMessage.getBody() instanceof RegisterTMRequest) {
//                serverMessageListener.onRegTmMessage(rpcMessage, ctx, checkAuthHandler);
//                return;
//            }
//            if (rpcMessage.getBody() == HeartbeatMessage.PING) {
//                serverMessageListener.onCheckMessage(rpcMessage, ctx);
//                return;
//            }
            }
            super.channelRead(ctx, msg);
        }

        /**
         * Channel inactive.
         *
         * @param ctx the ctx
         * @throws Exception the exception
         */
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        debugLog("inactive:" + ctx);
//        if (messageExecutor.isShutdown()) {
//            return;
//        }
            handleDisconnect(ctx);
            super.channelInactive(ctx);
        }

        private void handleDisconnect(ChannelHandlerContext ctx) {

        }

        /**
         * Exception caught.
         *
         * @param ctx   the ctx
         * @param cause the cause
         * @throws Exception the exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }

        /**
         * User event triggered.
         *
         * @param ctx the ctx
         * @param evt the evt
         * @throws Exception the exception
         */
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent) {
                log.info("idle事件:{}",evt);
//            debugLog("idle:" + evt);
//            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//            if (idleStateEvent.state() == IdleState.READER_IDLE) {
//                if (LOGGER.isInfoEnabled()) {
//                    LOGGER.info("channel:" + ctx.channel() + " read idle.");
//                }
//                handleDisconnect(ctx);
//                try {
//                    closeChannelHandlerContext(ctx);
//                } catch (Exception e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
            }
        }

    }

    public static void main(String[] args) {
//        String name = ManagementFactory.getRuntimeMXBean().getName();
//        String pid = name.split("@")[0];
//        int idx = new Random().nextInt(100);
//        System.out.println(name+":"+pid);
//        try {
//            Runtime.getRuntime().exec("jstack " + pid + " >D:/" + idx + ".log");
//        } catch (IOException exx) {
//            log.error(exx.getMessage());
//        }
        RpcService rpcService = new RpcService();
        rpcService.start();
//        allowDumpStack = false;
    }
}
