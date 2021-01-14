package com.pfdl.js.service;


import com.pfdl.api.jdbc.RemoteTransaction;

/**
 * 远程操作充电桩接口
 * @Author zhaoyt
 * @Date 2020/12/18 13:37
 * @Version 1.0
 */
public interface IRemoteTransactionService {
    /**
     * 远程启动充电指令组装转码
     * @param remoteTransaction
     * @return HEX16进制字符串
     */
    String RemoteStartTransactionHex(RemoteTransaction remoteTransaction);

    /**
     * 远程停止充电指令
     * @param remoteTransaction
     * @return
     */
    String RemoteStopTransaction(RemoteTransaction remoteTransaction);
}
