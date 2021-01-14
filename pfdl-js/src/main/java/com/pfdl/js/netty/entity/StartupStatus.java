package com.pfdl.js.netty.entity;

/**
 * @Author zhaoyt
 * @Date 2020/12/30 10:37
 * @Version 1.0
 */
public class StartupStatus {
    /** 启动形式 手动远程:remote 自动 :automatic*/
    private String startForm;
    /** 状态    0 : 未启动  1 : 启动  2 : 结束充电*/
    private String status;
    /** 停止形式 手动远程:remote 自动 :automatic*/
    private String stopForm;

    public StartupStatus(String startForm, String status, String stopForm) {
        this.startForm = startForm;
        this.status = status;
        this.stopForm = stopForm;
    }

    public String getStartForm() {
        return startForm;
    }

    public void setStartForm(String startForm) {
        this.startForm = startForm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStopForm() {
        return stopForm;
    }

    public void setStopForm(String stopForm) {
        this.stopForm = stopForm;
    }
}
