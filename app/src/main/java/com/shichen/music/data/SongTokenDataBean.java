package com.shichen.music.data;

import java.util.List;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenDataBean {
    /**
     * expiration : 80400
     * items : [{"subcode":0,"songmid":"0003SNcL1xzzAB","filename":"C4000003SNcL1xzzAB.m4a","vkey":"FD2120F13D5DCEB1756E9A7E254A16CBFF33ECFBB3DDFD25A2954520A351D29748E7DA2C3ADCB95AED8815212DB7B40447AD54C0591F17BD"}]
     */

    private int expiration;
    private List<SongTokenItemBean> items;

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public List<SongTokenItemBean> getItems() {
        return items;
    }

    public void setItems(List<SongTokenItemBean> items) {
        this.items = items;
    }


}
