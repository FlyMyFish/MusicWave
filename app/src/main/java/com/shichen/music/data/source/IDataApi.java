package com.shichen.music.data.source;

import com.shichen.music.data.CategoryBean;
import com.shichen.music.data.SheetSongListBean;
import com.shichen.music.data.SongTokenBean;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface IDataApi {
    /**
     * 菜谱分类标签查询
     *
     * @param param {@link com.shichen.music.data.source.param.CategoryParam}.makeMap();
     * @return
     */
    @GET("https://u.y.qq.com/cgi-bin/musicu.fcg?")
    Flowable<CategoryBean> getCategory(@QueryMap Map<String, Object> param);

    /**
     * 根据tid获取歌单详细信息
     *
     * @param tid     歌单id
     * @param referer "https://y.qq.com/n/yqq/playsquare/3838219942.html"
     * @return
     */
    @GET("https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&g_tk=275979108&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0")
    Flowable<SheetSongListBean> getSheetDetail(@Query("disstid") long tid, @Header("referer") String referer);

    /**
     * 根据songmid获取歌曲token
     * @param songmid
     * @param filename "C400" + songmid + ".m4a"
     * @return
     */
    @GET("https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?format=json205361747&platform=yqq&cid=205361747&guid=126548448")
    Flowable<SongTokenBean> getSongToken(@Query("songmid") String songmid, @Query("filename") String filename);

}
