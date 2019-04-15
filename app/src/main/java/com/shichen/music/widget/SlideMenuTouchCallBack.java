package com.shichen.music.widget;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.shichen.music.R;
import com.shichen.music.utils.LogUtils;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/4/10.
 */
public class SlideMenuTouchCallBack extends ItemTouchHelper.Callback {
    private final String TAG = "SlideMenuTouchCallBack";
    private float itemWidth;
    private float menuWidth;
    private boolean stateOpen = false;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    /**
     * 针对swipe状态，swipe 到达滑动消失的距离回调函数,一般在这个函数里面处理删除item的逻辑
     * 确切的来讲是swipe item滑出屏幕动画结束的时候调用
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        LogUtils.Log(TAG, "onSwiped - > direction = " + i);
        LogUtils.Log(TAG, "onSwiped - > left = " + ItemTouchHelper.LEFT + " + right = " + ItemTouchHelper.RIGHT);
        stateOpen = true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            itemWidth = viewHolder.itemView.getWidth();
            menuWidth = viewHolder.itemView.findViewWithTag(recyclerView.getContext().getString(R.string.slide_menu_menu)).getWidth();
            if (stateOpen) {
                float slideRight = itemWidth + dX;//打开状态指头右滑的距离 值为正数
                float maxX = Math.max(menuWidth - slideRight, 0);
                viewHolder.itemView.scrollTo((int) maxX, 0);
                LogUtils.Log(TAG, "onChildDraw - right > dX = " + dX + " maxX = " + maxX);
            } else {
                float minX = Math.min(Math.abs(dX), menuWidth);
                viewHolder.itemView.scrollTo((int) minX, 0);
                LogUtils.Log(TAG, "onChildDraw - left > dX = " + dX + " minX = " + minX);
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        if (dX == 0.0f) {
            stateOpen = false;
        }
    }

    /**
     * 针对swipe状态，swipe的逃逸速度，换句话说就算没达到getSwipeThreshold设置的距离，达到了这个逃逸速度item也会被swipe消失掉
     */
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 10000.0f;
    }

    /**
     * 针对swipe状态，swipe滑动的位置超过了百分之多少就消失
     */
    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        if (stateOpen) {
            LogUtils.Log(TAG, "getSwipeThreshold - > " + (1 - menuWidth / itemWidth));
            return 1 - menuWidth / itemWidth;
        } else {
            LogUtils.Log(TAG, "getSwipeThreshold - > " + menuWidth / itemWidth);
            return menuWidth / itemWidth;
        }
    }

    /**
     * 针对swipe状态，swipe滑动的阻尼系数,设置最大滑动速度
     */
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * 针对swipe和drag状态，当swipe或者drag对应的ViewHolder改变的时候调用
     * 我们可以通过重写这个函数获取到swipe、drag开始和结束时机，viewHolder 不为空的时候是开始，空的时候是结束
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        LogUtils.Log(TAG, "onSelectedChanged - > viewHolder is null ?" + (viewHolder == null));
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.scrollTo(0, 0);
    }
}
