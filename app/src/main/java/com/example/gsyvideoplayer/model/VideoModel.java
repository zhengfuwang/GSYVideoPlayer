package com.example.gsyvideoplayer.model;

import java.util.List;

/**
 * Created by xiyangyuge on 2017/8/8.
 */

public class VideoModel {

    public String desc_text;
    public String duration;
    public String file_size;
    public String hide_userinfo;
    public int is_like;
    public String media_source;
    public String media_vid;
    public String object;
    public String object_id;
    public int pausePosition;
    public int recycle_position;
    public String target;
    public String target_val;
    public String title;
    public String total_comment;
    public String total_like;
    public String total_play;
    public String tpl_type;
    public String modelType;
    public List<ImgsBean> imgs;

    public static class ImgsBean {
        public String cellular_img;
        public String small_img;
        public String modelType;
    }
    public int index;
}
