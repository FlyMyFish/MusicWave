package com.shichen.music.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/2
 */
public class LyricsView extends View {
    private Paint mPaint;
    private String lyrics;
    private float densityF;
    private float lyricsSize = 12f;
    private int lyricsColor = 0xffffffff;
    private int accentLyricsColr = 0xffD81B60;
    private float lineSpace = 9f;
    private SimpleExoPlayer exoPlayer;
    //歌词最大高度
    private float maxHeight;

    public void linkPlayer(SimpleExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
//        LogUtils.Log(TAG, "setLyrics - > " + lyrics);
        parseLyrics(lyrics);
        calculateMaxHeight();
        invalidate();
    }

    /**
     * 计算字幕的总高度
     */
    public void calculateMaxHeight() {
        if (lyricsMap != null) {
            int index = lyricsMap.size();
            maxHeight = 15f * densityF + (float) (((float) 15f * densityF + lineSpace) * index);
        }
    }

    public LyricsView(Context context) {
        super(context);
    }

    public LyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        densityF = context.getResources().getDisplayMetrics().density;
        mCompositeDisposable=new CompositeDisposable();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private final String TAG = "LyricsView";

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        //canvas.drawARGB(255, 0, 0, 0);
        mPaint.setTextSize(lyricsSize * densityF);
        mPaint.setColor(lyricsColor);
        mPaint.setStyle(Paint.Style.FILL);
        for (int index = 0; index < lyricsMap.size(); index++) {
            for (Map.Entry<String, String> entry : lyricsMap.get(index).entrySet()) {
                if (index == recordTimeP) {
                    mPaint.setColor(accentLyricsColr);
                } else {
                    mPaint.setColor(lyricsColor);
                }
                String lyricsLine = entry.getValue();
                Rect rect = new Rect();
                mPaint.getTextBounds(lyricsLine, 0, lyricsLine.length(), rect);
                canvas.drawText(lyricsLine, width / 2 - rect.width() / 2, 15f * densityF + (float) (((float) 15f * densityF + lineSpace) * index) + scrollOffset, mPaint);
            }
        }
    }

    private float scrollOffset;

    private void scrollLyrics(float oldOffset, float offset) {
        if (offset == 0.0f) {
            scrollOffset = 0.0f;
            postInvalidate();
        } else {
            Disposable disposable=
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            ValueAnimator animator = ValueAnimator.ofFloat(oldOffset, offset);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    scrollOffset = (float) animation.getAnimatedValue();
                                    invalidate();
                                }
                            });
                            animator.setDuration(200);
                            animator.start();
                        }
                    });
            mCompositeDisposable.add(disposable);
        }
    }

    private CompositeDisposable mCompositeDisposable;

    private float calculateTimeOffset(int recordTimeP, float measureHeight) {
        float oldY = 15f * densityF + (float) (((float) 15f * densityF + lineSpace) * recordTimeP);
        if (measureHeight / 2 < oldY) {
            return measureHeight / 2 - oldY;
        } else {
            return 0;
        }
    }

    //歌名
    private final String TI_PATTERN = "\\[ti:(.+?)\\]";
    //歌手
    private final String AR_PATTERN = "\\[ar:(.+?)\\]";
    //专辑
    private final String AL_PATTERN = "\\[al:(.+?)\\]";
    //歌词制作者
    private final String BY_PATTERN = "\\[by:(.+?)\\]";
    //歌词偏移时间
    private final String OFFSET_PATTERN = "\\[offset:(.+?)\\]";
    //描述
    private final String KANA_PATTERN = "\\[kana:(.+?)\\]";
    //歌词时间
    private final String TIME_PATTERN = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]";

    private Pattern TIPattern = Pattern.compile(TI_PATTERN),
            ARPattern = Pattern.compile(AR_PATTERN),
            ALPattern = Pattern.compile(AL_PATTERN),
            BYPattern = Pattern.compile(BY_PATTERN),
            OFFSETPattern = Pattern.compile(OFFSET_PATTERN),
            KANAPattern = Pattern.compile(KANA_PATTERN),
            TIMEPattern = Pattern.compile(TIME_PATTERN);

    private List<Map<String, String>> lyricsMap = new ArrayList<>();

    private List<Map<String, String>> parseLyrics(String lyrics) {
        try {
            InputStream inputStream = new ByteArrayInputStream(lyrics.getBytes());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String lineStr = null;
            while ((lineStr = reader.readLine()) != null) {
                if (lineStr.equals("")) {
                    continue;
                }
                Matcher TIMatcher = TIPattern.matcher(lineStr);
                if (TIMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("歌曲", TIMatcher.group(1));
                    lyricsMap.add(map);
                    continue;
                }
                Matcher ARMatcher = ARPattern.matcher(lineStr);
                if (ARMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("歌手", ARMatcher.group(1));
                    lyricsMap.add(map);
                    continue;
                }
                Matcher ALMatcher = ALPattern.matcher(lineStr);
                if (ALMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("专辑", ALMatcher.group(1));
                    lyricsMap.add(map);
                    continue;
                }
                Matcher BYMatcher = BYPattern.matcher(lineStr);
                if (BYMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("制作者", BYMatcher.group(1));
                    lyricsMap.add(map);
                    continue;
                }
                Matcher OFFSETMatcher = OFFSETPattern.matcher(lineStr);
                if (OFFSETMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("偏移时间", "");
                    lyricsMap.add(map);
                    continue;
                }
                Matcher KANAMatcher = KANAPattern.matcher(lineStr);
                if (KANAMatcher.find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("描述", KANAMatcher.group(1));
                    lyricsMap.add(map);
                    continue;
                }
                Matcher TIMEMatcher = TIMEPattern.matcher(lineStr);
                if (TIMEMatcher.find()) {
                    // 用于存储当前时间和文字信息的容器
                    // System.out.println(m.group(0)); // 例：[02:34.94]
                    // [02:34.94] ----对应---> [分钟:秒.毫秒]
                    String min = TIMEMatcher.group(1); // 分钟
                    String sec = TIMEMatcher.group(2); // 秒
                    String mill = TIMEMatcher.group(3); // 毫秒，注意这里其实还要乘以10
                    long time = getLongTime(min, sec, mill + "0");
                    // 获取当前时间的歌词信息
                    String text = lineStr.substring(TIMEMatcher.end());
                    Map<String, String> map = new HashMap<>();
                    map.put(String.valueOf(time), text); // 添加到容器中
                    lyricsMap.add(map);
                }
            }
            reader.close();
            return lyricsMap;
        } catch (IOException e) {
            return lyricsMap;
        }
    }

    /**
     * 将以字符串形式给定的分钟、秒钟、毫秒转换成一个以毫秒为单位的long型数
     *
     * @param min   分钟
     * @param sec   秒钟
     * @param mill* 毫秒
     * @return
     */
    private long getLongTime(String min, String sec, String mill) {
        // 转成整型
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);

        if (s >= 60) {
            System.out.println("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "."
                    + mill.substring(0, 2) + "]");
        }
        // 组合成一个长整型表示的以毫秒为单位的时间
        long time = m * 60 * 1000 + s * 1000 + ms;
        return time;
    }

    Timer timer;

    public void start() {
        timer = new Timer();
        timer.schedule(new CompareTimeTask(), 0, 1);
    }

    class CompareTimeTask extends TimerTask {
        @Override
        public void run() {
            if (exoPlayer != null) {
                updateRecord(exoPlayer.getCurrentPosition());
            }
        }
    }

    public void seekTo() {
        recordTimeP = 0;
    }

    //记录当前播放到的歌词在map中的position
    private int recordTimeP;

    /**
     * 查找当前播放到了第几行歌词
     * @param currentTimeTag
     */
    private void updateRecord(long currentTimeTag) {
        boolean find = false;
        int oldRecord = recordTimeP, newRecord = recordTimeP;
        for (int index = recordTimeP; index < lyricsMap.size(); index++) {
            if (!find) {
                for (Map.Entry<String, String> entry : lyricsMap.get(index).entrySet()) {
                    try {
                        long entryKey = Long.valueOf(entry.getKey());
                        if (entryKey >= (currentTimeTag / 10) * 10 && entryKey < (currentTimeTag / 10 + 1) * 10) {
//                        LogUtils.Log(TAG, "updateRecord - > entry = " + entry.getKey());
//                        LogUtils.Log(TAG, "updateRecord - > currentTimeTag = " + ((currentTimeTag / 10) * 10));
                            newRecord = index;
                            find = true;
                            break;
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        if (oldRecord != newRecord) {
            float oldOffset = calculateTimeOffset(recordTimeP, getMeasuredHeight());
            recordTimeP = newRecord;
            //scrollLyrics(calculateTimeOffset(recordTimeP, getMeasuredHeight()));
            float newOffset = calculateTimeOffset(recordTimeP, getMeasuredHeight());
            scrollLyrics(oldOffset, newOffset);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLyricsOnTouchListener!=null){
            mLyricsOnTouchListener.onTouch();
        }
        return super.onTouchEvent(event);
    }

    private LyricsOnTouchListener mLyricsOnTouchListener;

    public void setmLyricsOnTouchListener(LyricsOnTouchListener mLyricsOnTouchListener) {
        this.mLyricsOnTouchListener = mLyricsOnTouchListener;
    }

    public interface LyricsOnTouchListener{
        void onTouch();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
        if (mCompositeDisposable!=null){
            mCompositeDisposable.dispose();
            mCompositeDisposable.clear();
        }
    }
}
