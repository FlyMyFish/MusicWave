package com.shichen.music.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SonglistBean {
    /**
     * albumdesc : 《路人超能100 II》TV动画片尾曲
     * albumid : 6330173
     * albummid : 00358Tms359yam
     * albumname : メモセピア (Memo Sepia)
     * alertid : 11
     * belongCD : 2
     * cdIdx : 0
     * interval : 223
     * isonly : 0
     * label : 0
     * msgid : 0
     * pay : {"payalbum":0,"payalbumprice":0,"paydownload":0,"payinfo":0,"payplay":0,"paytrackmouth":0,"paytrackprice":0,"timefree":0}
     * preview : {"trybegin":0,"tryend":0,"trysize":0}
     * rate : 0
     * singer : [{"id":2247069,"mid":"001a4Z2K3lDfov","name":"sajou no hana (サジョウノハナ)"}]
     * size128 : 3576276
     * size320 : 8940365
     * size5_1 : 0
     * sizeape : 0
     * sizeflac : 0
     * sizeogg : 4711467
     * songid : 229490874
     * songmid : 0040Qvgg1MTcbt
     * songname : グレイ (Gray)
     * songorig : グレイ
     * songtype : 0
     * strMediaMid : 0040Qvgg1MTcbt
     * stream : 3
     * switch : 81683
     * type : 0
     * vid :
     */

    private String albumdesc;
    private int albumid;
    private String albummid;
    //专辑名
    private String albumname;
    private int alertid;
    private int belongCD;
    private int cdIdx;
    //歌曲时长 单位s
    private int interval;
    private int isonly;
    private String label;
    private int msgid;
    private int rate;
    private int size128;
    private int size320;
    private int size5_1;
    private int sizeape;
    private int sizeflac;
    private int sizeogg;
    private int songid;
    private String songmid;
    //歌曲名称
    private String songname;
    private String songorig;
    private int songtype;
    private String strMediaMid;
    private int stream;
    @SerializedName("switch")
    private int switchX;
    private int type;
    private String vid;
    private List<SingerBean> singer;

    public String getAlbumdesc() {
        return albumdesc;
    }

    public void setAlbumdesc(String albumdesc) {
        this.albumdesc = albumdesc;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public int getAlertid() {
        return alertid;
    }

    public void setAlertid(int alertid) {
        this.alertid = alertid;
    }

    public int getBelongCD() {
        return belongCD;
    }

    public void setBelongCD(int belongCD) {
        this.belongCD = belongCD;
    }

    public int getCdIdx() {
        return cdIdx;
    }

    public void setCdIdx(int cdIdx) {
        this.cdIdx = cdIdx;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getIsonly() {
        return isonly;
    }

    public void setIsonly(int isonly) {
        this.isonly = isonly;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSize128() {
        return size128;
    }

    public void setSize128(int size128) {
        this.size128 = size128;
    }

    public int getSize320() {
        return size320;
    }

    public void setSize320(int size320) {
        this.size320 = size320;
    }

    public int getSize5_1() {
        return size5_1;
    }

    public void setSize5_1(int size5_1) {
        this.size5_1 = size5_1;
    }

    public int getSizeape() {
        return sizeape;
    }

    public void setSizeape(int sizeape) {
        this.sizeape = sizeape;
    }

    public int getSizeflac() {
        return sizeflac;
    }

    public void setSizeflac(int sizeflac) {
        this.sizeflac = sizeflac;
    }

    public int getSizeogg() {
        return sizeogg;
    }

    public void setSizeogg(int sizeogg) {
        this.sizeogg = sizeogg;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongorig() {
        return songorig;
    }

    public void setSongorig(String songorig) {
        this.songorig = songorig;
    }

    public int getSongtype() {
        return songtype;
    }

    public void setSongtype(int songtype) {
        this.songtype = songtype;
    }

    public String getStrMediaMid() {
        return strMediaMid;
    }

    public void setStrMediaMid(String strMediaMid) {
        this.strMediaMid = strMediaMid;
    }

    public int getStream() {
        return stream;
    }

    public void setStream(int stream) {
        this.stream = stream;
    }

    public int getSwitchX() {
        return switchX;
    }

    public void setSwitchX(int switchX) {
        this.switchX = switchX;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public List<SingerBean> getSinger() {
        return singer;
    }

    public void setSinger(List<SingerBean> singer) {
        this.singer = singer;
    }
}
