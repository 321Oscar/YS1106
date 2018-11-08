package weidong.com.ys1106.Utils;

public class VideoInfo {
    public String ImgUrl;
    public String videotitle,videodetail;
    public String VideoType;
    public int checked;

    public VideoInfo(){

    }

    public VideoInfo(String imgUrl,String videodetail,String videotitle,int checked){
        this.checked = checked;
        this.ImgUrl = imgUrl;
        this.videodetail = videodetail;
        this.videotitle = videotitle;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getVideodetail() {
        return videodetail;
    }

    public void setVideodetail(String videodetail) {
        this.videodetail = videodetail;
    }

    public String getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(String videotitle) {
        this.videotitle = videotitle;
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
