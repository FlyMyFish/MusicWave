package com.shichen.music.data.source.remote;

import com.google.common.base.Optional;
import com.shichen.music.data.SongTokenItemBean;
import com.shichen.music.data.source.ISongTokenSource;
import com.shichen.music.utils.GsonUtils;
import com.shichen.music.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.Call;
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
        List<String> songMids = new ArrayList<>();
        songMids.add(mid);
        GetMusicParams params = new GetMusicParams();
        params.req_0.param.setSongmid(songMids);
        String musicUrl = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getplaysongvkey09489699031337162&g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=" + GsonUtils.getInstance().get().toJson(params);
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
                        String bodyStr = response.body().string();
                        GetMusicResponse mGetMusicResponse = GsonUtils.getInstance().get().fromJson(bodyStr, GetMusicResponse.class);
                        Date now = Calendar.getInstance().getTime();
                        SongTokenItemBean songTokenItemBean = new SongTokenItemBean();
                        songTokenItemBean.setExpiration(80400);
                        songTokenItemBean.setUpdateTime(now.getTime());
                        songTokenItemBean.setSongmid(mGetMusicResponse.req_0.data.midurlinfo.get(0).getSongmid());
                        songTokenItemBean.setVkey(mGetMusicResponse.req.data.vkey);
                        songTokenItemBean.setRealUrl(mGetMusicResponse.req.data.freeflowsip.get(0) + mGetMusicResponse.req_0.data.midurlinfo.get(0).getPurl());
                        return Optional.of(songTokenItemBean);
                    }
                });
/*        return Flowable.just(call)
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
                });*/

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

    private static class GetMusicParams {

        /**
         * req : {"module":"CDN.SrfCdnDispatchServer","method":"GetCdnDispatch","param":{"guid":"9167391000","calltype":0,"userip":""}}
         * req_0 : {"module":"vkey.GetVkeyServer","method":"CgiGetVkey","param":{"guid":"9167391000","songmid":["001C1g8J0Tvpg1"],"songtype":[0],"uin":"0","loginflag":1,"platform":"20"}}
         * comm : {"uin":0,"format":"json","ct":24,"cv":0}
         */

        private ReqBean req;
        private Req0Bean req_0;
        private String comm = "";

        public GetMusicParams() {
            this.req=new ReqBean();
            this.req_0=new Req0Bean();
        }

        public ReqBean getReq() {
            return req;
        }

        public void setReq(ReqBean req) {
            this.req = req;
        }

        public Req0Bean getReq_0() {
            return req_0;
        }

        public void setReq_0(Req0Bean req_0) {
            this.req_0 = req_0;
        }

        public static class ReqBean {
            /**
             * module : CDN.SrfCdnDispatchServer
             * method : GetCdnDispatch
             * param : {"guid":"9167391000","calltype":0,"userip":""}
             */

            private String module = "CDN.SrfCdnDispatchServer";
            private String method = "GetCdnDispatch";
            private ParamBean param;

            public ReqBean() {
                this.param=new ParamBean();
            }

            public String getModule() {
                return module;
            }

            public void setModule(String module) {
                this.module = module;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public ParamBean getParam() {
                return param;
            }

            public void setParam(ParamBean param) {
                this.param = param;
            }

            public static class ParamBean {
                /**
                 * guid : 9167391000
                 * calltype : 0
                 * userip :
                 */

                private String guid = "9167391000";
                private int calltype = 0;
                private String userip = "";

                public String getGuid() {
                    return guid;
                }

                public void setGuid(String guid) {
                    this.guid = guid;
                }

                public int getCalltype() {
                    return calltype;
                }

                public void setCalltype(int calltype) {
                    this.calltype = calltype;
                }

                public String getUserip() {
                    return userip;
                }

                public void setUserip(String userip) {
                    this.userip = userip;
                }
            }
        }

        public static class Req0Bean {
            /**
             * module : vkey.GetVkeyServer
             * method : CgiGetVkey
             * param : {"guid":"9167391000","songmid":["001C1g8J0Tvpg1"],"songtype":[0],"uin":"0","loginflag":1,"platform":"20"}
             */

            private String module = "vkey.GetVkeyServer";
            private String method = "CgiGetVkey";
            private ParamBeanX param;

            public Req0Bean() {
                this.param=new ParamBeanX();
            }

            public String getModule() {
                return module;
            }

            public void setModule(String module) {
                this.module = module;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public ParamBeanX getParam() {
                return param;
            }

            public void setParam(ParamBeanX param) {
                this.param = param;
            }

            public static class ParamBeanX {
                /**
                 * guid : 9167391000
                 * songmid : ["001C1g8J0Tvpg1"]
                 * songtype : [0]
                 * uin : 0
                 * loginflag : 1
                 * platform : 20
                 */

                private String guid = "9167391000";
                private String uin = "0";
                private int loginflag = 1;
                private String platform = "20";
                private List<String> songmid=new ArrayList<>();

                public String getGuid() {
                    return guid;
                }

                public void setGuid(String guid) {
                    this.guid = guid;
                }

                public String getUin() {
                    return uin;
                }

                public void setUin(String uin) {
                    this.uin = uin;
                }

                public int getLoginflag() {
                    return loginflag;
                }

                public void setLoginflag(int loginflag) {
                    this.loginflag = loginflag;
                }

                public String getPlatform() {
                    return platform;
                }

                public void setPlatform(String platform) {
                    this.platform = platform;
                }

                public List<String> getSongmid() {
                    return songmid;
                }

                public void setSongmid(List<String> songmid) {
                    this.songmid = songmid;
                }
            }
        }
    }

    private static class GetMusicResponse {

        /**
         * req : {"data":{"expiration":86400,"freeflowsip":["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/"],"keepalivefile":"C400004TsFuW2mZbRR.m4a?guid=9167391000&vkey=FCB1CC29BF9C3861131DFF362283D0C1AA11B16914CB563A96BDC902DD20137DD8D4B0BF403243EA9495295E4505B0985C9757A7ED7BFA49&uin=0&fromtag=3","msg":"ok","retcode":0,"servercheck":"74772b2b781e38df5a9701309ab10f06","sip":["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://dl.stream.qqmusic.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3","uin":"","userip":"117.159.26.206","vkey":"982FC8A2644F8454949B2EBCFE83A91E0A049D5100BA87DEA08A858EA1FA5E48BF8B713EE7F9B4DF639562D1F2D5ACAB2D5C2CC2EB238E0C"},"code":0}
         * req_0 : {"data":{"expiration":80400,"login_key":"","midurlinfo":[{"common_downfromtag":0,"errtype":"","filename":"C400001C1g8J0Tvpg1.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400001C1g8J0Tvpg1.m4a?guid=9167391000&vkey=BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"001C1g8J0Tvpg1","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5","wififromtag":"","wifiurl":""}],"msg":"117.159.26.206","retcode":0,"servercheck":"7f65e7c1c087b6ab6fa02b58f685576c","sip":["http://dl.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3","thirdip":["",""],"uin":"","verify_type":0},"code":0}
         * code : 0
         * ts : 1561947609467
         */

        private ReqBean req;
        private Req0Bean req_0;
        private int code;
        private long ts;

        public ReqBean getReq() {
            return req;
        }

        public void setReq(ReqBean req) {
            this.req = req;
        }

        public Req0Bean getReq_0() {
            return req_0;
        }

        public void setReq_0(Req0Bean req_0) {
            this.req_0 = req_0;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public static class ReqBean {
            /**
             * data : {"expiration":86400,"freeflowsip":["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/"],"keepalivefile":"C400004TsFuW2mZbRR.m4a?guid=9167391000&vkey=FCB1CC29BF9C3861131DFF362283D0C1AA11B16914CB563A96BDC902DD20137DD8D4B0BF403243EA9495295E4505B0985C9757A7ED7BFA49&uin=0&fromtag=3","msg":"ok","retcode":0,"servercheck":"74772b2b781e38df5a9701309ab10f06","sip":["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://dl.stream.qqmusic.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3","uin":"","userip":"117.159.26.206","vkey":"982FC8A2644F8454949B2EBCFE83A91E0A049D5100BA87DEA08A858EA1FA5E48BF8B713EE7F9B4DF639562D1F2D5ACAB2D5C2CC2EB238E0C"}
             * code : 0
             */

            private DataBean data;
            private int code;

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public static class DataBean {
                /**
                 * expiration : 86400
                 * freeflowsip : ["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/"]
                 * keepalivefile : C400004TsFuW2mZbRR.m4a?guid=9167391000&vkey=FCB1CC29BF9C3861131DFF362283D0C1AA11B16914CB563A96BDC902DD20137DD8D4B0BF403243EA9495295E4505B0985C9757A7ED7BFA49&uin=0&fromtag=3
                 * msg : ok
                 * retcode : 0
                 * servercheck : 74772b2b781e38df5a9701309ab10f06
                 * sip : ["http://111.7.162.149/amobile.music.tc.qq.com/","http://111.7.162.150/amobile.music.tc.qq.com/","http://dl.stream.qqmusic.qq.com/","http://111.7.162.151/amobile.music.tc.qq.com/","http://isure.stream.qqmusic.qq.com/"]
                 * testfile2g : C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3
                 * testfilewifi : C400003mAan70zUy5O.m4a?guid=9167391000&vkey=0E100B7CD154C12CBC4D327FF2CED964F838E2AB301011136EF045834D02D6DDA4EE8497265FF955559927676F4675F5E8E67E95F8150A04&uin=0&fromtag=3
                 * uin :
                 * userip : 117.159.26.206
                 * vkey : 982FC8A2644F8454949B2EBCFE83A91E0A049D5100BA87DEA08A858EA1FA5E48BF8B713EE7F9B4DF639562D1F2D5ACAB2D5C2CC2EB238E0C
                 */

                private int expiration;
                private String keepalivefile;
                private String msg;
                private int retcode;
                private String servercheck;
                private String testfile2g;
                private String testfilewifi;
                private String uin;
                private String userip;
                private String vkey;
                private List<String> freeflowsip;
                private List<String> sip;

                public int getExpiration() {
                    return expiration;
                }

                public void setExpiration(int expiration) {
                    this.expiration = expiration;
                }

                public String getKeepalivefile() {
                    return keepalivefile;
                }

                public void setKeepalivefile(String keepalivefile) {
                    this.keepalivefile = keepalivefile;
                }

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public int getRetcode() {
                    return retcode;
                }

                public void setRetcode(int retcode) {
                    this.retcode = retcode;
                }

                public String getServercheck() {
                    return servercheck;
                }

                public void setServercheck(String servercheck) {
                    this.servercheck = servercheck;
                }

                public String getTestfile2g() {
                    return testfile2g;
                }

                public void setTestfile2g(String testfile2g) {
                    this.testfile2g = testfile2g;
                }

                public String getTestfilewifi() {
                    return testfilewifi;
                }

                public void setTestfilewifi(String testfilewifi) {
                    this.testfilewifi = testfilewifi;
                }

                public String getUin() {
                    return uin;
                }

                public void setUin(String uin) {
                    this.uin = uin;
                }

                public String getUserip() {
                    return userip;
                }

                public void setUserip(String userip) {
                    this.userip = userip;
                }

                public String getVkey() {
                    return vkey;
                }

                public void setVkey(String vkey) {
                    this.vkey = vkey;
                }

                public List<String> getFreeflowsip() {
                    return freeflowsip;
                }

                public void setFreeflowsip(List<String> freeflowsip) {
                    this.freeflowsip = freeflowsip;
                }

                public List<String> getSip() {
                    return sip;
                }

                public void setSip(List<String> sip) {
                    this.sip = sip;
                }
            }
        }

        public static class Req0Bean {
            /**
             * data : {"expiration":80400,"login_key":"","midurlinfo":[{"common_downfromtag":0,"errtype":"","filename":"C400001C1g8J0Tvpg1.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400001C1g8J0Tvpg1.m4a?guid=9167391000&vkey=BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"001C1g8J0Tvpg1","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5","wififromtag":"","wifiurl":""}],"msg":"117.159.26.206","retcode":0,"servercheck":"7f65e7c1c087b6ab6fa02b58f685576c","sip":["http://dl.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"],"testfile2g":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3","testfilewifi":"C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3","thirdip":["",""],"uin":"","verify_type":0}
             * code : 0
             */

            private DataBeanX data;
            private int code;

            public DataBeanX getData() {
                return data;
            }

            public void setData(DataBeanX data) {
                this.data = data;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public static class DataBeanX {
                /**
                 * expiration : 80400
                 * login_key :
                 * midurlinfo : [{"common_downfromtag":0,"errtype":"","filename":"C400001C1g8J0Tvpg1.m4a","flowfromtag":"","flowurl":"","hisbuy":0,"hisdown":0,"isbuy":0,"isonly":0,"onecan":0,"opi128kurl":"","opi192koggurl":"","opi192kurl":"","opi30surl":"","opi48kurl":"","opi96kurl":"","opiflackurl":"","p2pfromtag":0,"pdl":0,"pneed":0,"pneedbuy":0,"premain":0,"purl":"C400001C1g8J0Tvpg1.m4a?guid=9167391000&vkey=BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5&uin=0&fromtag=66","qmdlfromtag":0,"result":0,"songmid":"001C1g8J0Tvpg1","tips":"","uiAlert":0,"vip_downfromtag":0,"vkey":"BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5","wififromtag":"","wifiurl":""}]
                 * msg : 117.159.26.206
                 * retcode : 0
                 * servercheck : 7f65e7c1c087b6ab6fa02b58f685576c
                 * sip : ["http://dl.stream.qqmusic.qq.com/","http://isure.stream.qqmusic.qq.com/"]
                 * testfile2g : C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3
                 * testfilewifi : C400003mAan70zUy5O.m4a?guid=9167391000&vkey=530E77957723A7F28FB1F4CD3AB2B12588026155E8E1EA3D622360F548FE81B3A3FB7EBF1BF24E0B5B6E15004DA216A993980D77DF8A6D0E&uin=&fromtag=3
                 * thirdip : ["",""]
                 * uin :
                 * verify_type : 0
                 */

                private int expiration;
                private String login_key;
                private String msg;
                private int retcode;
                private String servercheck;
                private String testfile2g;
                private String testfilewifi;
                private String uin;
                private int verify_type;
                private List<MidurlinfoBean> midurlinfo;
                private List<String> sip;
                private List<String> thirdip;

                public int getExpiration() {
                    return expiration;
                }

                public void setExpiration(int expiration) {
                    this.expiration = expiration;
                }

                public String getLogin_key() {
                    return login_key;
                }

                public void setLogin_key(String login_key) {
                    this.login_key = login_key;
                }

                public String getMsg() {
                    return msg;
                }

                public void setMsg(String msg) {
                    this.msg = msg;
                }

                public int getRetcode() {
                    return retcode;
                }

                public void setRetcode(int retcode) {
                    this.retcode = retcode;
                }

                public String getServercheck() {
                    return servercheck;
                }

                public void setServercheck(String servercheck) {
                    this.servercheck = servercheck;
                }

                public String getTestfile2g() {
                    return testfile2g;
                }

                public void setTestfile2g(String testfile2g) {
                    this.testfile2g = testfile2g;
                }

                public String getTestfilewifi() {
                    return testfilewifi;
                }

                public void setTestfilewifi(String testfilewifi) {
                    this.testfilewifi = testfilewifi;
                }

                public String getUin() {
                    return uin;
                }

                public void setUin(String uin) {
                    this.uin = uin;
                }

                public int getVerify_type() {
                    return verify_type;
                }

                public void setVerify_type(int verify_type) {
                    this.verify_type = verify_type;
                }

                public List<MidurlinfoBean> getMidurlinfo() {
                    return midurlinfo;
                }

                public void setMidurlinfo(List<MidurlinfoBean> midurlinfo) {
                    this.midurlinfo = midurlinfo;
                }

                public List<String> getSip() {
                    return sip;
                }

                public void setSip(List<String> sip) {
                    this.sip = sip;
                }

                public List<String> getThirdip() {
                    return thirdip;
                }

                public void setThirdip(List<String> thirdip) {
                    this.thirdip = thirdip;
                }

                public static class MidurlinfoBean {
                    /**
                     * common_downfromtag : 0
                     * errtype :
                     * filename : C400001C1g8J0Tvpg1.m4a
                     * flowfromtag :
                     * flowurl :
                     * hisbuy : 0
                     * hisdown : 0
                     * isbuy : 0
                     * isonly : 0
                     * onecan : 0
                     * opi128kurl :
                     * opi192koggurl :
                     * opi192kurl :
                     * opi30surl :
                     * opi48kurl :
                     * opi96kurl :
                     * opiflackurl :
                     * p2pfromtag : 0
                     * pdl : 0
                     * pneed : 0
                     * pneedbuy : 0
                     * premain : 0
                     * purl : C400001C1g8J0Tvpg1.m4a?guid=9167391000&vkey=BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5&uin=0&fromtag=66
                     * qmdlfromtag : 0
                     * result : 0
                     * songmid : 001C1g8J0Tvpg1
                     * tips :
                     * uiAlert : 0
                     * vip_downfromtag : 0
                     * vkey : BC1B7F1C96E0C2FD9885D1CD504D82B428D360B1E65CEEA6FC0D8E0ED3035D099DAB53AD4293170963B13B3456DAB2163CF927214CBFE0B5
                     * wififromtag :
                     * wifiurl :
                     */

                    private int common_downfromtag;
                    private String errtype;
                    private String filename;
                    private String flowfromtag;
                    private String flowurl;
                    private int hisbuy;
                    private int hisdown;
                    private int isbuy;
                    private int isonly;
                    private int onecan;
                    private String opi128kurl;
                    private String opi192koggurl;
                    private String opi192kurl;
                    private String opi30surl;
                    private String opi48kurl;
                    private String opi96kurl;
                    private String opiflackurl;
                    private int p2pfromtag;
                    private int pdl;
                    private int pneed;
                    private int pneedbuy;
                    private int premain;
                    private String purl;
                    private int qmdlfromtag;
                    private int result;
                    private String songmid;
                    private String tips;
                    private int uiAlert;
                    private int vip_downfromtag;
                    private String vkey;
                    private String wififromtag;
                    private String wifiurl;

                    public int getCommon_downfromtag() {
                        return common_downfromtag;
                    }

                    public void setCommon_downfromtag(int common_downfromtag) {
                        this.common_downfromtag = common_downfromtag;
                    }

                    public String getErrtype() {
                        return errtype;
                    }

                    public void setErrtype(String errtype) {
                        this.errtype = errtype;
                    }

                    public String getFilename() {
                        return filename;
                    }

                    public void setFilename(String filename) {
                        this.filename = filename;
                    }

                    public String getFlowfromtag() {
                        return flowfromtag;
                    }

                    public void setFlowfromtag(String flowfromtag) {
                        this.flowfromtag = flowfromtag;
                    }

                    public String getFlowurl() {
                        return flowurl;
                    }

                    public void setFlowurl(String flowurl) {
                        this.flowurl = flowurl;
                    }

                    public int getHisbuy() {
                        return hisbuy;
                    }

                    public void setHisbuy(int hisbuy) {
                        this.hisbuy = hisbuy;
                    }

                    public int getHisdown() {
                        return hisdown;
                    }

                    public void setHisdown(int hisdown) {
                        this.hisdown = hisdown;
                    }

                    public int getIsbuy() {
                        return isbuy;
                    }

                    public void setIsbuy(int isbuy) {
                        this.isbuy = isbuy;
                    }

                    public int getIsonly() {
                        return isonly;
                    }

                    public void setIsonly(int isonly) {
                        this.isonly = isonly;
                    }

                    public int getOnecan() {
                        return onecan;
                    }

                    public void setOnecan(int onecan) {
                        this.onecan = onecan;
                    }

                    public String getOpi128kurl() {
                        return opi128kurl;
                    }

                    public void setOpi128kurl(String opi128kurl) {
                        this.opi128kurl = opi128kurl;
                    }

                    public String getOpi192koggurl() {
                        return opi192koggurl;
                    }

                    public void setOpi192koggurl(String opi192koggurl) {
                        this.opi192koggurl = opi192koggurl;
                    }

                    public String getOpi192kurl() {
                        return opi192kurl;
                    }

                    public void setOpi192kurl(String opi192kurl) {
                        this.opi192kurl = opi192kurl;
                    }

                    public String getOpi30surl() {
                        return opi30surl;
                    }

                    public void setOpi30surl(String opi30surl) {
                        this.opi30surl = opi30surl;
                    }

                    public String getOpi48kurl() {
                        return opi48kurl;
                    }

                    public void setOpi48kurl(String opi48kurl) {
                        this.opi48kurl = opi48kurl;
                    }

                    public String getOpi96kurl() {
                        return opi96kurl;
                    }

                    public void setOpi96kurl(String opi96kurl) {
                        this.opi96kurl = opi96kurl;
                    }

                    public String getOpiflackurl() {
                        return opiflackurl;
                    }

                    public void setOpiflackurl(String opiflackurl) {
                        this.opiflackurl = opiflackurl;
                    }

                    public int getP2pfromtag() {
                        return p2pfromtag;
                    }

                    public void setP2pfromtag(int p2pfromtag) {
                        this.p2pfromtag = p2pfromtag;
                    }

                    public int getPdl() {
                        return pdl;
                    }

                    public void setPdl(int pdl) {
                        this.pdl = pdl;
                    }

                    public int getPneed() {
                        return pneed;
                    }

                    public void setPneed(int pneed) {
                        this.pneed = pneed;
                    }

                    public int getPneedbuy() {
                        return pneedbuy;
                    }

                    public void setPneedbuy(int pneedbuy) {
                        this.pneedbuy = pneedbuy;
                    }

                    public int getPremain() {
                        return premain;
                    }

                    public void setPremain(int premain) {
                        this.premain = premain;
                    }

                    public String getPurl() {
                        return purl;
                    }

                    public void setPurl(String purl) {
                        this.purl = purl;
                    }

                    public int getQmdlfromtag() {
                        return qmdlfromtag;
                    }

                    public void setQmdlfromtag(int qmdlfromtag) {
                        this.qmdlfromtag = qmdlfromtag;
                    }

                    public int getResult() {
                        return result;
                    }

                    public void setResult(int result) {
                        this.result = result;
                    }

                    public String getSongmid() {
                        return songmid;
                    }

                    public void setSongmid(String songmid) {
                        this.songmid = songmid;
                    }

                    public String getTips() {
                        return tips;
                    }

                    public void setTips(String tips) {
                        this.tips = tips;
                    }

                    public int getUiAlert() {
                        return uiAlert;
                    }

                    public void setUiAlert(int uiAlert) {
                        this.uiAlert = uiAlert;
                    }

                    public int getVip_downfromtag() {
                        return vip_downfromtag;
                    }

                    public void setVip_downfromtag(int vip_downfromtag) {
                        this.vip_downfromtag = vip_downfromtag;
                    }

                    public String getVkey() {
                        return vkey;
                    }

                    public void setVkey(String vkey) {
                        this.vkey = vkey;
                    }

                    public String getWififromtag() {
                        return wififromtag;
                    }

                    public void setWififromtag(String wififromtag) {
                        this.wififromtag = wififromtag;
                    }

                    public String getWifiurl() {
                        return wifiurl;
                    }

                    public void setWifiurl(String wifiurl) {
                        this.wifiurl = wifiurl;
                    }
                }
            }
        }
    }
}
