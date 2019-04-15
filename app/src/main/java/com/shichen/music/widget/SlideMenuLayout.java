package com.shichen.music.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.shichen.music.R;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2019/4/11.
 */
public class SlideMenuLayout extends ConstraintLayout {
    private final String TAG = "SlideMenuLayout";
    private View contentView, menuView;
    //private OverScroller mScroller;
    private Context mContext;
    private int mTouchSlop;

    public SlideMenuLayout(Context context) {
        super(context);
        mContext = context;
        //mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SlideMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        contentView = findViewWithTag(mContext.getString(R.string.slide_menu_content));
        menuView = findViewWithTag(mContext.getString(R.string.slide_menu_menu));
        if (contentView == null) {
            Log.e(TAG, "contentView is null !!! Please check your layout!");
        }
        if (menuView == null) {
            Log.e(TAG, "menuView is null !!! Please check your layout!");
        }
    }

    private int mActivePointerId = INVALID_POINTER;
    private float mInitialLeftX;
    private float mInitialMotionX;
    private boolean mIsBeingDragged;

    /**
     * return true：将事件传递给ViewGroup自己的onTouchEvent方法处理。
     * return false/super：将事件传递给下一级View的dispatchTouchEvent()
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                //初始化手指第一次接触屏幕的点的id
                mIsBeingDragged = false;
                final float initialLeftX = getMotionEventX(ev, mActivePointerId);
                if (initialLeftX < 0) {
                    //该点不可用,交给子View
                    return false;
                }
                mInitialLeftX = initialLeftX;
                //初始化left事件的X偏移值
                Log.e(TAG, "onInterceptTouchEvent - > ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                Log.e(TAG, "onInterceptTouchEvent - > ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onInterceptTouchEvent - > ACTION_UP");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG, "onInterceptTouchEvent - > ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                Log.e(TAG, "onInterceptTouchEvent - > ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    //触摸点不可用，将事件传递给子View
                    Log.e(TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }
                final float x = getMotionEventX(ev, mActivePointerId);
                if (x == -1) {
                    return false;
                }
                if (x > 0) {
                    final float xDiff = mInitialLeftX - x;
                    Log.e(TAG, "onInterceptTouchEvent - > ACTION_MOVE xDiff = " + xDiff);
                    //y轴改变量
                    if (xDiff > mTouchSlop && !mIsBeingDragged) {
                        //当改变量大于最小判定为拖拽的值的时候，并且没有处于拖拽状态
                        mInitialMotionX = mInitialLeftX + mTouchSlop;
                        //已经拖拽的Y方向的距离
                        mIsBeingDragged = true;
                        //设置拖拽状态，并对事件进行处理
                    } else if (Math.abs(xDiff) > mTouchSlop && !mIsBeingDragged && getScrollX() > 0) {
                        //当改变量大于最小判定为拖拽的值的时候，并且没有处于拖拽状态
                        mInitialMotionX = mInitialLeftX + mTouchSlop;
                        //已经拖拽的Y方向的距离
                        mIsBeingDragged = true;
                        //设置拖拽状态，并对事件进行处理
                    }
                }
                Log.e(TAG, "onInterceptTouchEvent - > ACTION_MOVE x = " + x);
                break;
            case MotionEvent.ACTION_OUTSIDE:
                if (getScrollX() > 0) {
                    smoothMoveToRight();
                }
                break;
        }
        Log.e(TAG, "onInterceptTouchEvent -> return : " + super.onInterceptTouchEvent(ev));
        return mIsBeingDragged;
    }

    /**
     * return true：消费掉事件，终止传递。
     * return false/super：将事件传递给上一级view的onTouchEvent方法。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int pointerIndex = -1;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                Log.e(TAG, "onTouchEvent - > ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG, "onTouchEvent - > ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_UP: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                final float x = ev.getX(pointerIndex);
                final float overscrollRight = (x - mInitialMotionX);
                Log.e(TAG, "onTouchEvent - > ACTION_MOVE overscrollRight = " + overscrollRight);
                if (mIsBeingDragged) {
                    if (overscrollRight < 0) {
                        //此时处于向左拖拽的状态
                        smoothMoveToLeft();
                    } else {
                        smoothMoveToRight();
                    }
                }
                mIsBeingDragged = false;

                mActivePointerId = INVALID_POINTER;
                Log.e(TAG, "onTouchEvent - > ACTION_UP");
                return false;
            }
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG, "onTouchEvent - > ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_CANCEL: {
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                final float x = ev.getX(pointerIndex);
                final float overscrollRight = (x - mInitialMotionX);
                Log.e(TAG, "onTouchEvent - > ACTION_MOVE overscrollRight = " + overscrollRight);
                if (mIsBeingDragged) {
                    if (overscrollRight < 0) {
                        //此时处于向左拖拽的状态
                        smoothMoveToLeft();
                    } else {
                        smoothMoveToRight();
                    }
                }
                mIsBeingDragged = false;

                mActivePointerId = INVALID_POINTER;
                Log.e(TAG, "onTouchEvent - > ACTION_CANCEL");
            }
            return false;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float xE = getMotionEventX(ev, mActivePointerId);
                if (xE == -1) {
                    return false;
                }
                Log.e(TAG, "onTouchEvent - > ACTION_MOVE xE = " + xE);
                if (xE > 0) {
                    final float xDiff = mInitialLeftX - xE;
                    Log.e(TAG, "onTouchEvent - > ACTION_MOVE xDiff = " + xDiff);
                    Log.e(TAG, "onTouchEvent - > ACTION_MOVE mTouchSlop = " + mTouchSlop);
                    //y轴改变量
                    if (xDiff > mTouchSlop && !mIsBeingDragged) {
                        //当改变量大于最小判定为拖拽的值的时候，并且没有处于拖拽状态
                        mInitialMotionX = mInitialLeftX + mTouchSlop;
                        //已经拖拽的Y方向的距离
                        mIsBeingDragged = true;
                        //设置拖拽状态，并对事件进行处理
                        Log.e(TAG, "onTouchEvent - > ACTION_MOVE mIsBeingDragged = " + mIsBeingDragged);
                    } else if (Math.abs(xDiff) > mTouchSlop && !mIsBeingDragged && getScrollX() > 0) {
                        //当改变量大于最小判定为拖拽的值的时候，并且没有处于拖拽状态
                        mInitialMotionX = mInitialLeftX + mTouchSlop;
                        //已经拖拽的Y方向的距离
                        mIsBeingDragged = true;
                        //设置拖拽状态，并对事件进行处理
                        Log.e(TAG, "onTouchEvent - > ACTION_MOVE mIsBeingDragged = " + mIsBeingDragged);
                    }
                }

                final float x = ev.getX(pointerIndex);
                final float overscrollRight = (x - mInitialMotionX);
                Log.e(TAG, "onTouchEvent - > ACTION_MOVE overscrollRight = " + overscrollRight);
                if (mIsBeingDragged) {
                    if (overscrollRight < 0) {
                        //此时处于向左拖拽的状态
                        moveContentAndMenu(overscrollRight);
                    } else {
                        moveCloseMenu(overscrollRight);
                    }
                }
                Log.e(TAG, "onTouchEvent - > ACTION_MOVE");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                if (getScrollX() > 0) {
                    smoothMoveToRight();
                }
                break;
        }
        return true;
    }

    private void moveContentAndMenu(float overscrollRight) {
        int scrollRight = (int) Math.abs(overscrollRight);
        scrollTo(Math.max(Math.min(scrollRight, menuView.getWidth()), getScrollX()), 0);
    }

    public void smoothMoveToLeft() {
        int distance = menuView.getWidth() - getScrollX();
        final int startX = getScrollX();
        ValueAnimator animator = ValueAnimator.ofInt(0, distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                scrollTo(startX + value, 0);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public void smoothMoveToRight() {
        int distance = getScrollX();
        ValueAnimator animator = ValueAnimator.ofInt(distance, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                scrollTo(value, 0);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    private void moveCloseMenu(float overscrollRight) {
        int scrollRight = (int) Math.abs(overscrollRight);
        scrollTo(Math.min(Math.max(menuView.getWidth() - scrollRight, 0), getScrollX()), 0);
    }

    /**
     * 获取手指第一次触碰屏幕的X坐标位置
     *
     * @param ev
     * @param activePointerId
     * @return -1:该点不可用
     */
    private float getMotionEventX(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        //注释掉该部分，为了实现上拉加载更多
        return ev.getX(index);
    }

    /**
     * return true：消费掉事件，终止传递。
     * <p>
     * return false：将事件传递给上一级view的onTouchEvent方法，如果是Activity的dispatchTouchEvent方法，则也是消费掉事件，终止传递。
     * <p>
     * return super：如果是Activity,则传递给下一级view的dispatchTouchEvent；如果是ViewGroup，则传递给自己的onInterceptTouchEvent；如果是View，则传递给自己的onTouchEvent()。
     *
     * @return
     */
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_UP");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"dispatchTouchEvent - > ACTION_MOVE");
                break;
        }
        Log.e(TAG,"dispatchTouchEvent -> return : "+super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }*/
    @Override
    public void computeScroll() {

    }
}
