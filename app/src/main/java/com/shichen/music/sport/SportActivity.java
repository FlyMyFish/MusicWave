package com.shichen.music.sport;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shichen.music.R;
import com.shichen.music.basic.BaseActivity;
import com.shichen.music.basic.Viewable;
import com.shichen.music.data.VPlaylistBean;
import com.shichen.music.sport.contract.SportContract;
import com.shichen.music.sport.presenter.SportActivityPresenter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author shichen 754314442@qq.com
 * Created by Administrator on 2018/10/12.
 */
@Viewable(presenter = SportActivityPresenter.class, layout = R.layout.activity_sport, needPermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
public class SportActivity extends BaseActivity<SportContract.View, SportActivityPresenter> implements SportContract.View {

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.rv_song_sheet)
    RecyclerView rvSongSheet;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;

    SongSheetAdapter mSongSheetAdapter;

    @Override
    public void init() {
        mToolbar.setTitle(R.string.song_sheet);
        setSupportActionBar(mToolbar);
        rvSongSheet.setLayoutManager(new LinearLayoutManager(this));
        mSongSheetAdapter = new SongSheetAdapter(this, R.layout.item_song_sheet, new ArrayList<>());
        mSongSheetAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SportActivity.this, SheetDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(SheetDetailActivity.SHEET_ID, mSongSheetAdapter.getDatas().get(position).getTid());
                startActivity(intent.putExtras(bundle));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvSongSheet.setAdapter(mSongSheetAdapter);
        srlRefresh.setOnRefreshListener(refreshLayout -> presenter.refreshList());
        srlRefresh.setEnableLoadMore(false);
        presenter.start();
    }

    @Override
    public void setIsRefresh() {
        if (srlRefresh != null) {
            srlRefresh.autoRefresh();
        }
    }

    @Override
    public void refreshFinish(boolean ifSuccess) {
        if (srlRefresh != null) {
            srlRefresh.finishRefresh(ifSuccess);
        }
    }

    @Override
    public void setCategory(List<VPlaylistBean> vPlaylistBeanList) {
        if (mSongSheetAdapter != null) {
            mSongSheetAdapter.getDatas().clear();
            mSongSheetAdapter.getDatas().addAll(vPlaylistBeanList);
            mSongSheetAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadMoreFinish(List<VPlaylistBean> vPlaylistBeanList, boolean ifSuccess) {
        if (ifSuccess) {
            if (mSongSheetAdapter != null) {
                if (srlRefresh != null) {
                    srlRefresh.finishLoadMore(true);
                }
                mSongSheetAdapter.getDatas().addAll(vPlaylistBeanList);
                mSongSheetAdapter.notifyDataSetChanged();
            } else {
                if (srlRefresh != null) {
                    srlRefresh.finishLoadMore(false);
                }
            }
        } else {
            if (srlRefresh != null) {
                srlRefresh.finishLoadMore(false);
            }
        }
    }

    @Override
    public void allPermissionsOk(String[] permissions) {
        super.allPermissionsOk(permissions);
        //如果顺利获取到了所有的权限，你的逻辑
    }

    @Override
    public void permissionsDenied(List<String> deniedPermissions) {
        super.permissionsDenied(deniedPermissions);
        //如果一些权限或者所有权限被禁止，你的逻辑
    }
}
