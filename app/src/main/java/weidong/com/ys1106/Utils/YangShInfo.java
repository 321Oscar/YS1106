package weidong.com.ys1106.Utils;

public class YangShInfo {
    private String ImgUrl;
    private String name;
    private String Type;
    private int checked;
    private String Des;

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }



    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public YangShInfo() {

    }

    public YangShInfo(String imgUrl, String name, int checked) {
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
