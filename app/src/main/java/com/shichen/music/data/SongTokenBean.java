package com.shichen.music.data;

import java.util.List;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenBean {

    /**
     * code : 0
     * cid : 205361747
     * userip : 117.159.26.206
     * data : {"expiration":80400,"items":[{"subcode":0,"songmid":"0003SNcL1xzzAB","filename":"C4000003SNcL1xzzAB.m4a","vkey":"FD2120F13D5DCEB1756E9A7E254A16CBFF33ECFBB3DDFD25A2954520A351D29748E7DA2C3ADCB95AED8815212DB7B40447AD54C0591F17BD"}]}
     */

    private int code;
    private int cid;
    private String userip;
    private SongTokenDataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public SongTokenDataBean getData() {
        return data;
    }

    public void setData(SongTokenDataBean data) {
        this.data = data;
    }


}
