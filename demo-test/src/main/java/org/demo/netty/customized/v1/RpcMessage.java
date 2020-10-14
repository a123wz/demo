package org.demo.netty.customized.v1;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年10月08日 14:22:00
 */
@Data
@Accessors(chain = true)
public class RpcMessage {

    private int id;
    private byte messageType;
    private byte codec;
    private byte compressor;
    private Map<String, String> headMap = new HashMap<>();
}
