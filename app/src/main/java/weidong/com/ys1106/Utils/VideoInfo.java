package weidong.com.ys1106.Utils;

/*
 * 视频信息
 * 图片URL
 * 视频URL
 * 视频标题
 * 视频简介
 * 视频类型
 * */

public class VideoInfo {
    private String ImgUrl;
    private String videoTitle;
    private String videoDes;
    private String VideoType;
    private String VideoURL;

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public VideoInfo(){}

    public VideoInfo(String imgUrl, String videodetail, String videoTitle) {
        this.ImgUrl = imgUrl;
        this.videoDes = videodetail;
        this.videoTitle = videoTitle;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getVideoDes() {
        return videoDes;
    }

    public void setVideoDes(String videoDes) {
        this.videoDes = videoDes;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoType() {
        return VideoType;
    }

    public void setVideoType(String videoType) {
        VideoType = videoType;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
