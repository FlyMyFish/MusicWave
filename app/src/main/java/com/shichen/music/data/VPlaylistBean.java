package com.shichen.music.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "v_play_list")
public class VPlaylistBean {
    /**
     * access_num : 792277
     * album_pic_mid :
     * censor_remark : []
     * censor_status : 0
     * censor_time : 0
     * commit_time : 1545993038
     * cover_mid :
     * cover_url_big : http://p.qpic.cn/music_cover/7vlTTvwBiaibKJpyXffTHicMqgazvQxsCAUxM8fKtxBTQDwSdsyzexibibA/600?n=1
     * cover_url_medium : http://p.qpic.cn/music_cover/7vlTTvwBiaibKJpyXffTHicMqgazvQxsCAUxM8fKtxBTQDwSdsyzexibibA/600?n=1
     * cover_url_small : http://p.qpic.cn/music_cover/7vlTTvwBiaibKJpyXffTHicMqgazvQxsCAUxM8fKtxBTQDwSdsyzexibibA/600?n=1
     * create_time : 1545992401
     * creator_info : {"avatar":"","is_dj":0,"nick":"QQ音乐官方歌单","taoge_avatar":"","taoge_nick":"","uin":2783429033,"vip_type":0}
     * creator_uin : 2783429033
     * desc :
     * dirid : 0
     * fav_num : 0
     * modify_time : 1546057748
     * pic_mid :
     * rcmdcontent :
     * rcmdtemplate :
     * score : 0
     * song_ids : [104839081,100915614,9182671,189929,105803750,1093476,1092077,104931978,903991,101057887,212017673,2516586,100889650,103288064,3074596,106479090,203050617,203470305,510390,6801341,4879768,106676426,100916795,1341240,5126420,203360367,200824618,203277280,105042561,101063139,108698137,108819281]
     * song_types : [3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3]
     * tag_ids : [21,223]
     * tag_names : []
     * tid : 6209100170
     * title : Bossa Nova
     * tjreport :
     */

    private int access_num;
    private String album_pic_mid;
    private int censor_status;
    private int censor_time;
    private int commit_time;
    private String cover_mid;
    private String cover_url_big;
    private String cover_url_medium;
    private String cover_url_small;
    private int create_time;
    @Embedded
    private CreatorInfoBean creator_info;
    private long creator_uin;
    private String desc;
    private int dirid;
    private int fav_num;
    private int modify_time;
    private String pic_mid;
    private String rcmdcontent;
    private String rcmdtemplate;
    private int score;
    @PrimaryKey
    private long tid;
    private String title;
    private String tjreport;
    private List<Integer> song_ids;
    private List<Integer> song_types;
    private List<Integer> tag_ids;

    public int getAccess_num() {
        return access_num;
    }

    public void setAccess_num(int access_num) {
        this.access_num = access_num;
    }

    public String getAlbum_pic_mid() {
        return album_pic_mid;
    }

    public void setAlbum_pic_mid(String album_pic_mid) {
        this.album_pic_mid = album_pic_mid;
    }

    public int getCensor_status() {
        return censor_status;
    }

    public void setCensor_status(int censor_status) {
        this.censor_status = censor_status;
    }

    public int getCensor_time() {
        return censor_time;
    }

    public void setCensor_time(int censor_time) {
        this.censor_time = censor_time;
    }

    public int getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(int commit_time) {
        this.commit_time = commit_time;
    }

    public String getCover_mid() {
        return cover_mid;
    }

    public void setCover_mid(String cover_mid) {
        this.cover_mid = cover_mid;
    }

    public String getCover_url_big() {
        return cover_url_big;
    }

    public void setCover_url_big(String cover_url_big) {
        this.cover_url_big = cover_url_big;
    }

    public String getCover_url_medium() {
        return cover_url_medium;
    }

    public void setCover_url_medium(String cover_url_medium) {
        this.cover_url_medium = cover_url_medium;
    }

    public String getCover_url_small() {
        return cover_url_small;
    }

    public void setCover_url_small(String cover_url_small) {
        this.cover_url_small = cover_url_small;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public CreatorInfoBean getCreator_info() {
        return creator_info;
    }

    public void setCreator_info(CreatorInfoBean creator_info) {
        this.creator_info = creator_info;
    }

    public long getCreator_uin() {
        return creator_uin;
    }

    public void setCreator_uin(long creator_uin) {
        this.creator_uin = creator_uin;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDirid() {
        return dirid;
    }

    public void setDirid(int dirid) {
        this.dirid = dirid;
    }

    public int getFav_num() {
        return fav_num;
    }

    public void setFav_num(int fav_num) {
        this.fav_num = fav_num;
    }

    public int getModify_time() {
        return modify_time;
    }

    public void setModify_time(int modify_time) {
        this.modify_time = modify_time;
    }

    public String getPic_mid() {
        return pic_mid;
    }

    public void setPic_mid(String pic_mid) {
        this.pic_mid = pic_mid;
    }

    public String getRcmdcontent() {
        return rcmdcontent;
    }

    public void setRcmdcontent(String rcmdcontent) {
        this.rcmdcontent = rcmdcontent;
    }

    public String getRcmdtemplate() {
        return rcmdtemplate;
    }

    public void setRcmdtemplate(String rcmdtemplate) {
        this.rcmdtemplate = rcmdtemplate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTjreport() {
        return tjreport;
    }

    public void setTjreport(String tjreport) {
        this.tjreport = tjreport;
    }

    public List<Integer> getSong_ids() {
        return song_ids;
    }

    public void setSong_ids(List<Integer> song_ids) {
        this.song_ids = song_ids;
    }

    public List<Integer> getSong_types() {
        return song_types;
    }

    public void setSong_types(List<Integer> song_types) {
        this.song_types = song_types;
    }

    public List<Integer> getTag_ids() {
        return tag_ids;
    }

    public void setTag_ids(List<Integer> tag_ids) {
        this.tag_ids = tag_ids;
    }

}
