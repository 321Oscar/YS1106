package weidong.com.ys1106.Utils;

public class YangShInfo {
    public String ImgUrl;
    public String name;
    public String Type;
    public int checked;

    public YangShInfo(){

    }

    public YangShInfo(String imgUrl,  String name, int checked){
        this.checked = checked;
        this.ImgUrl = imgUrl;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
