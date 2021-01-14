package com.pfdl.api.netty;

import io.netty.channel.ChannelId;

import java.io.Serializable;

/**
 * @Author zhaoyt
 * @Date 2020/12/17 14:21
 * @Version 1.0
 * 桩的身份对象
 */
public class PileIdentity implements Serializable {
    private static final long serialVersionUID = -4258050069772730388L;



    /**
     * netty 连接通道分配的ID
     */
    private ChannelId ChannelId;
    /**
     * 桩的入库ID
     */
    private String pileSN;
    /**
     * 桩的 dest 目的ID
     */
    private String hexDestId;
    /**
     * 桩的 dest 目的ID
     */
    private String destIdNumber;
    /**
     * 桩的数据包来源ID
     * source
     */
    private String source;

    public PileIdentity(ChannelId channelId, String pileSN, String hexDestId, String destIdNumber, String source) {
        ChannelId = channelId;
        this.pileSN = pileSN;
        this.hexDestId = hexDestId;
        this.destIdNumber = destIdNumber;
        this.source = source;
    }

    public PileIdentity() {
    }

    @Override
    public String toString() {
        return "PileIdentity{" +
                "ChannelId='" + ChannelId + '\'' +
                ", pileSN='" + pileSN + '\'' +
                ", hexDestId='" + hexDestId + '\'' +
                ", destIdNumber='" + destIdNumber + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
    public String getDestIdNumber() {
        return destIdNumber;
    }

    public void setDestIdNumber(String destIdNumber) {
        this.destIdNumber = destIdNumber;
    }

    public ChannelId getChannelId() {
        return ChannelId;
    }

    public void setChannelId(ChannelId channelId) {
        ChannelId = channelId;
    }

    public String getPileSN() {
        return pileSN;
    }

    public void setPileSN(String pileSN) {
        this.pileSN = pileSN;
    }

    public String getHexDestId() {
        return hexDestId;
    }

    public void setHexDestId(String hexDestId) {
        this.hexDestId = hexDestId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
