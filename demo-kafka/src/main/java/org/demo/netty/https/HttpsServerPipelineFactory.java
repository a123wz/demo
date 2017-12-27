package org.demo.netty.https;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.demo.netty.HttpClientInboundHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpsServerPipelineFactory extends ChannelInitializer<Channel> {  
    @Override  
    protected void initChannel(Channel ch) throws Exception {  
        ChannelPipeline pipeline = ch.pipeline();  
        //=====================以下为为SSL处理新增的代码=================================  
        // Uncomment the following line if you want HTTPS  
        SSLContext sslcontext = SSLContext.getInstance("TLS");  
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");  
        KeyStore ks = KeyStore.getInstance("PKCS12");  
        String keyStorePath ="inner.pfx";  
        String keyPassword ="123456787654321";  
        ks.load(new FileInputStream(keyStorePath), keyPassword.toCharArray());  
        kmf.init(ks, keyPassword.toCharArray());  
        sslcontext.init(kmf.getKeyManagers(), null, null);  
        SSLEngine engine = sslcontext.createSSLEngine();  
        engine.setUseClientMode(false);  
        engine.setNeedClientAuth(false);  
        pipeline.addFirst("ssl", new SslHandler(engine));  
        //=====================以上为为SSL处理新增的代码=================================  
        pipeline.addLast("decoder", new HttpRequestDecoder(16 * 1024 * 1024, 8192, 8192));  
        pipeline.addLast("encoder", new HttpResponseEncoder());  
        pipeline.addLast("deflater", new HttpContentCompressor()); 
        pipeline.addLast(new HttpClientInboundHandler());
       
//        pipeline.addLast("handler", new HttpServerHandler());  
    }  
}  