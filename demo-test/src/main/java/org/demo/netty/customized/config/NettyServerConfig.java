package org.demo.netty.customized.config;

import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 10:52:00
 */
public class NettyServerConfig {

    private int serverSelectorThreads = 10;
    private int serverSocketSendBufSize = 153600;
    private int serverSocketResvBufSize = 153600;
    private int serverWorkerThreads = 10;
    private int soBackLogSize = 1024;
    private int writeBufferHighWaterMark = 67108864;
    private int writeBufferLowWaterMark = 1048576;
    private static final int DEFAULT_LISTEN_PORT = 8091;
    private static final int RPC_REQUEST_TIMEOUT = 30 * 1000;
    private int serverChannelMaxIdleTimeSeconds = 30;
    private static final String EPOLL_WORKER_THREAD_PREFIX = "NettyServerEPollWorker";

    /**
     * The Server channel clazz.
     */
//    public static final Class<? extends ServerChannel> SERVER_CHANNEL_CLAZZ = NettyBaseConfig.SERVER_CHANNEL_CLAZZ;


    /**
     * Gets server selector threads.
     *
     * @return the server selector threads
     */
    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    /**
     * Sets server selector threads.
     *
     * @param serverSelectorThreads the server selector threads
     */
    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    /**
     * Enable epoll boolean.
     *
     * @return the boolean
     */
//    public static boolean enableEpoll() {
//        return NettyBaseConfig.SERVER_CHANNEL_CLAZZ.equals(EpollServerSocketChannel.class)
//                && Epoll.isAvailable();
//
//    }

    /**
     * Gets server socket send buf size.
     *
     * @return the server socket send buf size
     */
    public int getServerSocketSendBufSize() {
        return serverSocketSendBufSize;
    }

    /**
     * Sets server socket send buf size.
     *
     * @param serverSocketSendBufSize the server socket send buf size
     */
    public void setServerSocketSendBufSize(int serverSocketSendBufSize) {
        this.serverSocketSendBufSize = serverSocketSendBufSize;
    }

    /**
     * Gets server socket resv buf size.
     *
     * @return the server socket resv buf size
     */
    public int getServerSocketResvBufSize() {
        return serverSocketResvBufSize;
    }

    /**
     * Sets server socket resv buf size.
     *
     * @param serverSocketResvBufSize the server socket resv buf size
     */
    public void setServerSocketResvBufSize(int serverSocketResvBufSize) {
        this.serverSocketResvBufSize = serverSocketResvBufSize;
    }

    /**
     * Gets server worker threads.
     *
     * @return the server worker threads
     */
    public int getServerWorkerThreads() {
        return serverWorkerThreads;
    }

    /**
     * Sets server worker threads.
     *
     * @param serverWorkerThreads the server worker threads
     */
    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    /**
     * Gets so back log size.
     *
     * @return the so back log size
     */
    public int getSoBackLogSize() {
        return soBackLogSize;
    }

    /**
     * Sets so back log size.
     *
     * @param soBackLogSize the so back log size
     */
    public void setSoBackLogSize(int soBackLogSize) {
        this.soBackLogSize = soBackLogSize;
    }

    /**
     * Gets write buffer high water mark.
     *
     * @return the write buffer high water mark
     */
    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    /**
     * Sets write buffer high water mark.
     *
     * @param writeBufferHighWaterMark the write buffer high water mark
     */
    public void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    /**
     * Gets write buffer low water mark.
     *
     * @return the write buffer low water mark
     */
    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    /**
     * Sets write buffer low water mark.
     *
     * @param writeBufferLowWaterMark the write buffer low water mark
     */
    public void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }

    /**
     * Gets listen port.
     *
     * @return the listen port
     */
    public int getDefaultListenPort() {
        return DEFAULT_LISTEN_PORT;
    }


    /**
     * Gets server channel max idle time seconds.
     *
     * @return the server channel max idle time seconds
     */
    public int getServerChannelMaxIdleTimeSeconds() {
        return serverChannelMaxIdleTimeSeconds;
    }

    /**
     * Gets rpc request timeout.
     *
     * @return the rpc request timeout
     */
    public static int getRpcRequestTimeout() {
        return RPC_REQUEST_TIMEOUT;
    }

}