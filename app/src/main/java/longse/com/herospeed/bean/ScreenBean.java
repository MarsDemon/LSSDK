package longse.com.herospeed.bean;

/**
 * Created by LY on 2017/12/25.
 */

public class ScreenBean {
    private String name;
    private String img;

    public ScreenBean(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ScreenBean{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
