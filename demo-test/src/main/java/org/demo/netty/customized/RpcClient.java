package org.demo.netty.customized;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.demo.netty.customized.config.NettyClientConfig;
import org.demo.netty.customized.config.NettyServerConfig;
import org.demo.netty.customized.v1.RpcMessage;
import org.demo.netty.customized.v1.V1Decoder;
import org.demo.netty.customized.v1.V1Encoder;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 17:56:00
 */
@Slf4j
public class RpcClient extends RpcHandler{

    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private EventExecutorGroup defaultEventExecutorGroup;
    private AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool> clientChannelPool;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    public RpcClient(){
        this.eventLoopGroupWorker = new NioEventLoopGroup(10,
                new NamedThreadFactory("client_",
                        10));
        this.defaultEventExecutorGroup = null;
    }

    public void start(){

        if (this.defaultEventExecutorGroup == null) {
//            this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(nettyClientConfig.getClientWorkerThreads(),
//                    new NamedThreadFactory(getThreadPrefix(nettyClientConfig.getClientWorkerThreadPrefix()),
//                            nettyClientConfig.getClientWorkerThreads()));
        }
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        this.bootstrap.group(this.eventLoopGroupWorker).channel(
                NioSocketChannel.class).option(
                ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS,  nettyClientConfig.getConnectTimeoutMillis()).option(
                ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize()).option(ChannelOption.SO_RCVBUF,
                nettyClientConfig.getClientSocketRcvBufSize());


        bootstrap.handler(
                new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new IdleStateHandler(10,
                                        0,
                                        0))
                                .addLast(new V1Decoder())
                                .addLast(new V1Encoder()).addLast(new ClientHandler());
//                        if (null != channelHandlers) {
//                            addChannelPipelineLast(ch, channelHandlers);
//                        }
                    }
                });
    }

    @ChannelHandler.Sharable
    class ClientHandler extends AbstractHandler {

        @Override
        public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
            log.info("客户端接收数据:{}", JSON.toJSONString(request));
//            if (clientMessageListener != null) {
//                String remoteAddress = NetUtil.toStringAddress(ctx.channel().remoteAddress());
//                clientMessageListener.onMessage(request, remoteAddress);
//            }
        }

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!(msg instanceof RpcMessage)) {
                return;
            }
            RpcMessage rpcMessage = (RpcMessage) msg;
//            if (rpcMessage.getBody() == HeartbeatMessage.PONG) {
//                if (LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("received PONG from {}", ctx.channel().remoteAddress());
//                }
//                return;
//            }
//            if (rpcMessage.getBody() instanceof MergeResultMessage) {
//                MergeResultMessage results = (MergeResultMessage) rpcMessage.getBody();
//                MergedWarpMessage mergeMessage = (MergedWarpMessage) mergeMsgMap.remove(rpcMessage.getId());
//                for (int i = 0; i < mergeMessage.msgs.size(); i++) {
//                    int msgId = mergeMessage.msgIds.get(i);
//                    MessageFuture future = futures.remove(msgId);
//                    if (future == null) {
//                        if (LOGGER.isInfoEnabled()) {
//                            LOGGER.info("msg: {} is not found in futures.", msgId);
//                        }
//                    } else {
//                        future.setResultMessage(results.getMsgs()[i]);
//                    }
//                }
//                return;
//            }
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (messageExecutor.isShutdown()) {
                return;
            }
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("channel inactive: {}", ctx.channel());
//            }
//            clientChannelManager.releaseChannel(ctx.channel(), NetUtil.toStringAddress(ctx.channel().remoteAddress()));
            super.channelInactive(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent) {
                log.info("idle事件:{}",evt);
                IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
                if (idleStateEvent.state() == IdleState.READER_IDLE) {
                    if (log.isInfoEnabled()) {
                        log.info("channel {} read idle.", ctx.channel());
                    }
//                    try {
//                        String serverAddress = NetUtil.toStringAddress(ctx.channel().remoteAddress());
//                        clientChannelManager.invalidateObject(serverAddress, ctx.channel());
//                    } catch (Exception exx) {
//                        LOGGER.error(exx.getMessage());
//                    } finally {
//                        clientChannelManager.releaseChannel(ctx.channel(), getAddressFromContext(ctx));
//                    }
                }
                if (idleStateEvent == IdleStateEvent.WRITER_IDLE_STATE_EVENT) {
                    try {
//                        if (LOGGER.isDebugEnabled()) {
//                            LOGGER.debug("will send ping msg,channel {}", ctx.channel());
//                        }
//                        AbstractRpcRemotingClient.super.defaultSendRequest(ctx.channel(), HeartbeatMessage.PING);
                    } catch (Throwable throwable) {
//                        LOGGER.error("send request error: {}", throwable.getMessage(), throwable);
                    }
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            LOGGER.error(FrameworkErrorCode.ExceptionCaught.getErrCode(),
//                    NetUtil.toStringAddress(ctx.channel().remoteAddress()) + "connect exception. " + cause.getMessage(), cause);
//            clientChannelManager.releaseChannel(ctx.channel(), getAddressFromChannel(ctx.channel()));
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("remove exception rm channel:{}", ctx.channel());
//            }
            super.exceptionCaught(ctx, cause);
        }
    }

    public static void main(String[] args) throws Exception{
        RpcClient rpcClient = new RpcClient();
        rpcClient.start();
        ChannelFuture future = rpcClient.bootstrap.connect(new InetSocketAddress("127.0.0.1", 9092)).sync();
        RpcMessage response = new RpcMessage();
        response.setId(11).setMessageType((byte)1);
        Map<String,String> headMap = new HashMap<>();
        headMap.put("key","ss");
        headMap.put("bb","ss");
        response.setHeadMap(headMap);
        future.channel().writeAndFlush(response.setCodec((byte)1).setCompressor((byte)1));
        future.channel().closeFuture().sync();
    }
}
