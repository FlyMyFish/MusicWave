package com.shichen.music.widget;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/4/15.
 */
public interface ItemSlideUIUtil {
    /**
     * The default implementation for {@link ItemSlideHelper.Callback#onChildDraw(Canvas,
     * RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)}
     */
    void onDraw(Canvas c, RecyclerView recyclerView, View view,
                float dX, float dY, int actionState, boolean isCurrentlyActive);

    /**
     * The default implementation for {@link ItemSlideHelper.Callback#onChildDrawOver(Canvas,
     * RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)}
     */
    void onDrawOver(Canvas c, RecyclerView recyclerView, View view,
                    float dX, float dY, int actionState, boolean isCurrentlyActive);

    /**
     * The default implementation for {@link ItemSlideHelper.Callback#clearView(RecyclerView,
     * RecyclerView.ViewHolder)}
     */
    void clearView(View view);

    /**
     * The default implementation for {@link ItemSlideHelper.Callback#onSelectedChanged(
     * RecyclerView.ViewHolder, int)}
     */
    void onSelected(View view);
}
