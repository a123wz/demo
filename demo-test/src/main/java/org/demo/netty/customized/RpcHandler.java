package org.demo.netty.customized;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.demo.netty.customized.v1.ProtocolConstants;
import org.demo.netty.customized.v1.RpcMessage;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 18:22:00
 */
@Slf4j
public class RpcHandler {

    protected final Object lock = new Object();

    protected ThreadPoolExecutor messageExecutor =  new ThreadPoolExecutor(50,
            100, 500, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(20000),
            new NamedThreadFactory("ServerHandlerThread", 500), new ThreadPoolExecutor.CallerRunsPolicy());

    abstract class AbstractHandler extends ChannelDuplexHandler {

        /**
         * Dispatch.
         *
         * @param request the request
         * @param ctx     the ctx
         */
        public abstract void dispatch(RpcMessage request, ChannelHandlerContext ctx);

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) {
            synchronized (lock) {
                if (ctx.channel().isWritable()) {
                    lock.notifyAll();
                }
            }

            ctx.fireChannelWritabilityChanged();
        }

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof RpcMessage) {
                final RpcMessage rpcMessage = (RpcMessage) msg;
                try {
                    messageExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dispatch(rpcMessage, ctx);
                            } catch (Throwable th) {
//                                log.error(FrameworkErrorCode.NetDispatch.getErrCode(), th.getMessage(), th);
                            }
                        }
                    });
                } catch (RejectedExecutionException e) {

                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            LOGGER.error(FrameworkErrorCode.ExceptionCaught.getErrCode(),
//                    ctx.channel() + " connect exception. " + cause.getMessage(),
//                    cause);
            try {
//                destroyChannel(ctx.channel());
                ctx.channel().disconnect();
                ctx.channel().close();
            } catch (Exception e) {
                log.error("failed to close channel {}: {}", ctx.channel(), e.getMessage(), e);
            }
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info(ctx + " will closed");
//            }
            super.close(ctx, future);
        }

    }
}
