package com.shichen.music.data;

import android.arch.persistence.room.ColumnInfo;

public class CreatorInfoBean {
    /**
     * avatar :
     * is_dj : 0
     * nick : QQ音乐官方歌单
     * taoge_avatar :
     * taoge_nick :
     * uin : 2783429033
     * vip_type : 0
     */
    @ColumnInfo(name = "uin")
    private long uin;
    private String avatar;
    private int is_dj;
    private String nick;
    private String taoge_avatar;
    private String taoge_nick;
    private int vip_type;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_dj() {
        return is_dj;
    }

    public void setIs_dj(int is_dj) {
        this.is_dj = is_dj;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTaoge_avatar() {
        return taoge_avatar;
    }

    public void setTaoge_avatar(String taoge_avatar) {
        this.taoge_avatar = taoge_avatar;
    }

    public String getTaoge_nick() {
        return taoge_nick;
    }

    public void setTaoge_nick(String taoge_nick) {
        this.taoge_nick = taoge_nick;
    }

    public long getUin() {
        return uin;
    }

    public void setUin(long uin) {
        this.uin = uin;
    }

    public int getVip_type() {
        return vip_type;
    }

    public void setVip_type(int vip_type) {
        this.vip_type = vip_type;
    }
}
