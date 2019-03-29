package com.shichen.music.data.source.param;

import java.util.HashMap;
import java.util.Map;

public class CategoryParam extends Param {
    private long g_tk = 1391813790;
    private int hostUin = 0;
    private String format = "json";
    private String inCharset = "utf8";
    private String outCharset = "utf-8";
    private int notice = 0;
    private String platform = "yqq.json";
    private int needNewCode = 0;
    private String data;

    @Override
    public Map<String,Object> makeMap(){
        Map<String,Object> makeMap=new HashMap<>();
        makeMap.put("g_tk",String.valueOf(g_tk));
        makeMap.put("hostUin",String.valueOf(hostUin));
        makeMap.put("format",format);
        makeMap.put("inCharset",inCharset);
        makeMap.put("outCharset",outCharset);
        makeMap.put("notice",String.valueOf(notice));
        makeMap.put("platform",platform);
        makeMap.put("needNewCode",String.valueOf(needNewCode));
        makeMap.put("data",data);
        return makeMap;
    }

    public CategoryParam(Data dataBean) {
        this.dataBean = dataBean;
        this.data = dataBean.toString();
    }

    private Data dataBean;

    public void setDataBean(Data dataBean) {
        this.dataBean = dataBean;
    }

    public static class Data extends Param {

        public Data(PlaylistBean playlist) {
            this.comm = new CommBean();
            this.playlist = playlist;
        }

        /**
         * comm : {"ct":24}
         * playlist : {"method":"get_playlist_by_category","param":{"id":3317,"curPage":1,"size":40,"order":5,"titleid":3317},"module":"playlist.PlayListPlazaServer"}
         */

        private CommBean comm;
        private PlaylistBean playlist;

        public CommBean getComm() {
            return comm;
        }

        public void setComm(CommBean comm) {
            this.comm = comm;
        }

        public PlaylistBean getPlaylist() {
            return playlist;
        }

        public void setPlaylist(PlaylistBean playlist) {
            this.playlist = playlist;
        }

        public static class CommBean {
            /**
             * ct : 24
             */

            private int ct = 24;

            public int getCt() {
                return ct;
            }

            public void setCt(int ct) {
                this.ct = ct;
            }
        }

        public static class PlaylistBean {
            public PlaylistBean(ParamBean param) {
                this.param = param;
            }

            /**
             * method : get_playlist_by_category
             * param : {"id":3317,"curPage":1,"size":40,"order":5,"titleid":3317}
             * module : playlist.PlayListPlazaServer
             */

            private String method = "get_playlist_by_category";
            private ParamBean param;
            private String module = "playlist.PlayListPlazaServer";

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

            public String getModule() {
                return module;
            }

            public void setModule(String module) {
                this.module = module;
            }

            public static class ParamBean {
                public ParamBean(int id, int curPage, int size, int titleid) {
                    this.id = id;
                    this.curPage = curPage;
                    this.size = size;
                    this.titleid = titleid;
                }

                /**
                 * id : 3317
                 * curPage : 1
                 * size : 40
                 * order : 5
                 * titleid : 3317
                 */

                private int id;
                private int curPage;
                private int size;
                private int order = 5;
                private int titleid;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCurPage() {
                    return curPage;
                }

                public void setCurPage(int curPage) {
                    this.curPage = curPage;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public int getOrder() {
                    return order;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public int getTitleid() {
                    return titleid;
                }

                public void setTitleid(int titleid) {
                    this.titleid = titleid;
                }
            }
        }
    }
}
