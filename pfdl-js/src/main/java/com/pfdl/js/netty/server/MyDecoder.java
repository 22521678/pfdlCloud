package com.pfdl.js.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author zhaoyt
 * @Date 2020/12/8 8:26
 * @Version 1.0
 */

public class MyDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(MyDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        String HEXES = "0123456789ABCDEF";
        byte[] req = new byte[buffer.readableBytes()];
        buffer.readBytes(req);
        final StringBuilder hex = new StringBuilder(2 * req.length);

        for (int i = 0; i < req.length; i++) {
            byte b = req[i];
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        String[] splitHex = hex.toString().split("4747");
        if(splitHex.length > 2){
            //说明粘包
            for (int i = 1; i < splitHex.length; i++) {
                out.add("4747"+splitHex[i]);
                System.out.println("-----------粘包处理   : 4747"+splitHex[i]);
            }
        }else{
            out.add(hex.toString());
        }
//        out.add(hex.toString());
    }

    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }
}
