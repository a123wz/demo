package org.demo.netty.customized.v1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 11:36:00
 */
@Slf4j
public class V1Encoder extends MessageToByteEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(V1Encoder.class);

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        try {
            if (msg instanceof RpcMessage) {
                log.info("编码:{}",msg);
                RpcMessage rpcMessage = (RpcMessage) msg;

                int fullLength = ProtocolConstants.V1_HEAD_LENGTH;
                int headLength = ProtocolConstants.V1_HEAD_LENGTH;

                byte messageType = rpcMessage.getMessageType();
                out.writeBytes(ProtocolConstants.MAGIC_CODE_BYTES);
                out.writeByte(ProtocolConstants.VERSION);
                // full Length(4B) and head length(2B) will fix in the end.
                out.writerIndex(out.writerIndex() + 6);
                out.writeByte(messageType);
                out.writeByte(rpcMessage.getCodec());
                out.writeByte(rpcMessage.getCompressor());
                out.writeInt(rpcMessage.getId());

                // direct write head with zero-copy
                Map<String, String> headMap = rpcMessage.getHeadMap();
                if (headMap != null && !headMap.isEmpty()) {
                    int headMapBytesLength = HeadMapSerializer.getInstance().encode(headMap, out);
                    headLength += headMapBytesLength;
                    fullLength += headMapBytesLength;
                }

                byte[] bodyBytes = null;
//                if (messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_REQUEST
//                        && messageType != ProtocolConstants.MSGTYPE_HEARTBEAT_RESPONSE) {
//                    // heartbeat has no body
//                    Serializer serializer = SerializerFactory.getSerializer(rpcMessage.getCodec()); //编码器
//                    bodyBytes = serializer.serialize(rpcMessage.getBody());
//                    Compressor compressor = CompressorFactory.getCompressor(rpcMessage.getCompressor()); //字节码压缩算法
//                    bodyBytes = compressor.compress(bodyBytes);
//                    fullLength += bodyBytes.length;
//                }
//
//                if (bodyBytes != null) {
//                    out.writeBytes(bodyBytes);
//                }

                // fix fullLength and headLength
                int writeIndex = out.writerIndex();
                // skip magic code(2B) + version(1B)
                out.writerIndex(writeIndex - fullLength + 3);
                out.writeInt(fullLength);
                out.writeShort(headLength);
                out.writerIndex(writeIndex);
            } else {
                throw new UnsupportedOperationException("Not support this class:" + msg.getClass());
            }
        } catch (Throwable e) {
            LOGGER.error("Encode request error!", e);
        }
    }
}