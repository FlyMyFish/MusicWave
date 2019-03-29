package com.shichen.music.sport;

import android.content.Context;

import com.shichen.music.R;
import com.shichen.music.data.SingerBean;
import com.shichen.music.data.SonglistBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author by Administrator, Email xx@xx.com, Date on 2019/3/29
 */
public class SongAdapter extends CommonAdapter<SonglistBean> {
    public SongAdapter(Context context, int layoutId, List<SonglistBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SonglistBean songlistBean, int position) {
        holder.setText(R.id.tv_position, String.valueOf(position + 1));
        holder.setText(R.id.tv_song_name, songlistBean.getSongname());
        if (songlistBean.getSinger() != null) {
            if (songlistBean.getSinger().size() > 0) {
                SingerBean singerBean = songlistBean.getSinger().get(0);
                holder.setText(R.id.tv_song_singer, singerBean.getName());
            }
        }
        holder.setText(R.id.tv_song_interval, parseInterval(songlistBean.getInterval()));
    }

    private String parseInterval(int interval) {
        int min = interval / 60;
        int sec = interval % 60;
        String minStr, secStr;
        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = String.valueOf(min);
        }
        if (sec < 10) {
            secStr = "0" + sec;
        } else {
            secStr = String.valueOf(sec);
        }
        return minStr + ":" + secStr;
    }
}
