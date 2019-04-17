package com.shichen.music.sport;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shichen.music.R;
import com.shichen.music.basic.BaseActivity;
import com.shichen.music.basic.GlideApp;
import com.shichen.music.basic.Viewable;
import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.SonglistBean;
import com.shichen.music.sport.contract.SheetDetailContract;
import com.shichen.music.sport.presenter.SheetDetailActivityPresenter;
import com.shichen.music.widget.ItemSlideHelper;
import com.shichen.music.widget.SlideMenuTouchCallBack;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Viewable(layout = R.layout.activity_sheet_detail, presenter = SheetDetailActivityPresenter.class)
public class SheetDetailActivity extends BaseActivity<SheetDetailContract.View, SheetDetailActivityPresenter> implements SheetDetailContract.View {

    public static final String SHEET_ID = "sheet_id";
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.rv_song_list)
    RecyclerView rvSongList;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_replay_count)
    TextView tvReplayCount;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;
    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.tv_summary_detail)
    TextView tvSummaryDetail;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_songs_count)
    TextView tvSongsCount;
    @BindView(R.id.fab_play_list)
    FloatingActionButton fabPlayList;
    @BindView(R.id.fab_go_to_play)
    FloatingActionButton fabGotoPlay;

    private SongAdapter mSongAdapter;

    @Override
    public void init() {
        presenter.start(getIntent().getExtras());
        //appBar.addOnOffsetChangedListener((appBarLayout, i) -> Log.e("onOffsetChanged", "i = " + i));
        rvSongList.setLayoutManager(new LinearLayoutManager(this));
        mSongAdapter = new SongAdapter(this, R.layout.item_song, new ArrayList<>());
        mSongAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                presenter.playThisSong(mSongAdapter.getDatas().get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mSongAdapter.setOnFunctionClickListener(new SongAdapter.OnFunctionClickListener() {
            @Override
            public void addToPlayList(View v, int p) {
                presenter.addToPlayList(mSongAdapter.getDatas().get(p));
            }
        });
        fabPlayList.setOnClickListener(v -> presenter.playAll(mSongAdapter.getDatas()));
        fabGotoPlay.setOnClickListener(v -> gotoPlayActivity());
        rvSongList.setAdapter(mSongAdapter);
        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setOnRefreshListener(refreshLayout -> presenter.refreshData());
        setAutoRefresh();
        ItemSlideHelper itemSlideHelper = new ItemSlideHelper(new SlideMenuTouchCallBack());
        itemSlideHelper.attachToRecyclerView(rvSongList);
    }

    @Override
    public void setData(SheetSongListBean sheetDetail) {
        shortToast(sheetDetail.toString());
    }

    @Override
    public void setLogo(String url) {
        GlideApp.with(this).load(url).into(ivLogo);
    }

    @Override
    public void setFrom(String from) {
        toolbar.setTitle(from);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void setAutoRefresh() {
        if (srlRefresh != null) {
            srlRefresh.autoRefresh();
        }
    }

    @Override
    public void finishRefresh(boolean ifSuccess) {
        if (srlRefresh != null) {
            srlRefresh.finishRefresh(ifSuccess);
        }
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setReplayCount(String replayCount) {
        tvReplayCount.setText(replayCount);
    }

    @Override
    public void setCollectCunt(String collectCunt) {
        tvCollectCount.setText(collectCunt);
    }

    @Override
    public void setSummary(String summary) {
        tvSummary.setText(summary);
    }

    @Override
    public void setSummaryDetail(Spanned summaryDetail) {
        tvSummaryDetail.setText(summaryDetail);
    }

    @Override
    public void setSongsCount(String songsCount) {
        tvSongsCount.setText(songsCount);
    }

    @Override
    public void setSongList(List<SonglistBean> songList) {
        if (mSongAdapter != null) {
            if (mSongAdapter.getDatas() != null) {
                mSongAdapter.getDatas().clear();
                mSongAdapter.getDatas().addAll(songList);
                mSongAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void gotoPlayActivity() {
        Intent intent = new Intent(SheetDetailActivity.this, PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void showGotoPlay() {
        float translateY = fabGotoPlay.getTranslationY();
        if (translateY > 0) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(translateY, 0.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) animation.getAnimatedValue();
                    fabGotoPlay.setTranslationY(value);
                }
            });
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.start();
        }
    }
}
