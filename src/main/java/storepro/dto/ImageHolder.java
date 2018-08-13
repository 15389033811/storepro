package storepro.dto;

import java.io.InputStream;

/*
* 如果每次都是传入文件流和名字，那么我们要传入的参数太多，我们对文件流和名字进行封装,
* 我们要对所有需要传入文件流和文件名称的方法赋值
* */
public class ImageHolder {
    private String imageName;
    private InputStream image;

    public  ImageHolder(String imageName,InputStream image){
        this.imageName=imageName;
        this.image=image;
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }
}
