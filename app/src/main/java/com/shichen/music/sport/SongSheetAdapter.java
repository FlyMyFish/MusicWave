package com.shichen.music.sport;

import android.content.Context;
import android.widget.ImageView;

import com.shichen.music.R;
import com.shichen.music.basic.GlideApp;
import com.shichen.music.data.VPlaylistBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SongSheetAdapter extends CommonAdapter<VPlaylistBean> {
    public SongSheetAdapter(Context context, int layoutId, List<VPlaylistBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, VPlaylistBean vPlaylistBean, int position) {
        GlideApp.with(mContext).load(vPlaylistBean.getCover_url_small()).centerCrop().into((ImageView) holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, vPlaylistBean.getCreator_info().getNick());
        holder.setText(R.id.tv_from, vPlaylistBean.getTitle());
        holder.setText(R.id.tv_replay_count, "播放量:" + vPlaylistBean.getAccess_num());
    }
}
