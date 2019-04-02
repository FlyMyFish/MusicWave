package com.shichen.music.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.shichen.music.utils.GsonUtils;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
@Entity(tableName = "song_token_item")
public class SongTokenItemBean {
    /**
     * subcode : 0
     * songmid : 0003SNcL1xzzAB
     * filename : C4000003SNcL1xzzAB.m4a
     * vkey : FD2120F13D5DCEB1756E9A7E254A16CBFF33ECFBB3DDFD25A2954520A351D29748E7DA2C3ADCB95AED8815212DB7B40447AD54C0591F17BD
     */

    @PrimaryKey
    private int id;
    private int subcode;
    private String songmid;
    private String filename;
    private String vkey;
    private long updateTime;
    private long expiration;
    private String realUrl;
    private String lyrics;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getVkey() {
        return vkey;
    }

    public void setVkey(String vkey) {
        this.vkey = vkey;
    }

    @Override
    public String toString() {
        return GsonUtils.getInstance().get().toJson(this);
    }
}
