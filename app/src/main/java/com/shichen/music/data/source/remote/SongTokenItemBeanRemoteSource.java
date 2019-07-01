package com.shichen.music.data.source.remote;

import android.util.Log;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenBean;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.IDataApi;
import com.shichen.music.data.source.ISongTokenSource;
import com.shichen.music.utils.OkHttpUtils;
import com.shichen.music.utils.RetrofitHelper;

import org.reactivestreams.Publisher;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class SongTokenItemBeanRemoteSource implements ISongTokenSource {
    private static SongTokenItemBeanRemoteSource INSTANCE;

    private SongTokenItemBeanRemoteSource() {
    }

    public static SongTokenItemBeanRemoteSource getInstance() {
        if (INSTANCE == null) {
            synchronized (SongTokenItemBeanRemoteSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SongTokenItemBeanRemoteSource();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void save(SongTokenItemBean songTokenItemBean) {

    }

    @Override
    public Flowable<Optional<SongTokenItemBean>> getTokenByMid(String mid) {
        String musicUrl = "https://api.itooi.cn/music/tencent/url?key=579621905&id=" + mid + "&br=320";
        Request request = new Request.Builder()
                .url(musicUrl)
                .get()
                .build();
        Call call = OkHttpUtils.getInstance().client().newCall(request);
        return Flowable.just(call)
                .map(new Function<Call, Optional<SongTokenItemBean>>() {
                    @Override
                    public Optional<SongTokenItemBean> apply(Call call) throws Exception {
                        Response response = call.execute();
                        HttpUrl url = response.request().url();
                        String vkey = url.queryParameter("vkey");
                        Date now = Calendar.getInstance().getTime();
                        SongTokenItemBean songTokenItemBean = new SongTokenItemBean();
                        songTokenItemBean.setExpiration(80400);
                        songTokenItemBean.setUpdateTime(now.getTime());
                        songTokenItemBean.setSongmid(mid);
                        songTokenItemBean.setVkey(vkey);
                        songTokenItemBean.setRealUrl(url.toString());
                        return Optional.of(songTokenItemBean);
                    }
                });

        /*return dataApi.getSongToken(mid, "C400" + mid + ".m4a").map(new Function<SongTokenBean, Optional<SongTokenItemBean>>() {
            @Override
            public Optional<SongTokenItemBean> apply(SongTokenBean songTokenBean) throws Exception {
                if (songTokenBean.getData() != null) {
                    int expiration = songTokenBean.getData().getExpiration();
                    Date now = Calendar.getInstance().getTime();
                    if (songTokenBean.getData().getItems() != null) {
                        if (songTokenBean.getData().getItems().size() > 0) {
                            SongTokenItemBean songTokenItemBean = songTokenBean.getData().getItems().get(0);
                            songTokenItemBean.setExpiration(expiration);
                            songTokenItemBean.setUpdateTime(now.getTime());
                            Log.e("SongTokenItemRemote", songTokenItemBean.toString());
                            return Optional.of(songTokenItemBean);
                        } else {
                            Log.e("SongTokenItemRemote", "null");
                            return null;
                        }
                    } else {
                        Log.e("SongTokenItemRemote", "null");
                        return null;
                    }
                } else {
                    Log.e("SongTokenItemRemote", "null");
                    return null;
                }
            }
        });*/
    }

    @Override
    public Flowable<Optional<String>> getLyrics(String mid) {
        String lyricsUrl = "https://api.bzqll.com/music/tencent/lrc?key=579621905&id=" + mid;
        Request request = new Request.Builder()
                .url(lyricsUrl)
                .get()
                .build();
        Call call = OkHttpUtils.getInstance().client().newCall(request);
        return Flowable.just(call).map(new Function<Call, Optional<String>>() {
            @Override
            public Optional<String> apply(Call call) throws Exception {
                Response response = call.execute();
                String bodyStr = response.body().string();
                return Optional.of(bodyStr);
            }
        });
    }

    @Override
    public void updateLyrics(String mid, String lyrics) {

    }
}
