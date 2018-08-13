package storepro.util;

import ch.qos.logback.core.util.FileUtil;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storepro.dto.ImageHolder;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


//图片处理类
public class ImageUtil {
    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");//定义日期的格式
    private static final Random r=new Random();//生成随机数的类
    //获取要当做水印的图片路径
    static String  basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();


    //接受上传文件并处理（处理缩略图的类）
    //fiel:要处理的文件
    //targetAddr:要存入的文件（子路径）
    public static String generateThumnail(ImageHolder imageHolder, String targetAddr){
       //因为上传的图片名称容易重复我们自己设定图片名称
    String realFileName=getRandomFileName();//自定义上传图片名称
    String extension=getFileExtension(imageHolder.getImageName());//获取文件扩展名如.png等
    makeDidPath(targetAddr);//创建路径不存在的目录
    String relativeAddr=targetAddr+realFileName+extension;//获得了相对路径

    File dest =new File(PathUtil.getImgBasePath()+relativeAddr);//获得图片存储的绝对路径+照片名+后缀

          try{
        Thumbnails.of(imageHolder.getImage())
                //控制大小                    水印坐标                   读取要水印的图片                                          透明度       压缩大小
                .size(200,200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/qq.jpg")),0.25f).outputQuality(0.8f)
                //   输出到某个文件夹
                .toFile(dest);
    }catch (Exception e){
        e.printStackTrace();
    }
     return relativeAddr;
}

    /**
     * 处理详情图，并返回新生成图片的相对路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {

        // 获取不重复的随机名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名，如:png，jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDidPath(targetAddr);
        // 获取文件要保存的目标路径
        String relativeAddr = targetAddr + realFileName + extension;

        // 获取文件要保存到的目录路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

        // 调用 Thumbnails 生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "qq.jpg")), 0.25f)
                    .outputQuality(0.f)
                    .toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败，" + e.toString());
        }

        // 返回图片相对路径地址
        return relativeAddr;
    }



    private static void makeDidPath(String targetAddr) {//创建目标路径所涉及到的目录
        String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;//获得文件要存储绝对路径
        File dirPath=new File(realFileParentPath);
        if (!dirPath.exists()){//不存在路径就创建出来
          dirPath.mkdirs();//创建路径
        }
    }


    private static String getFileExtension(String fileName) {//获取扩展名
        return fileName.substring(fileName.lastIndexOf("."));//因为扩展名前面总是一个'.'所以获得最后一个.后的字符串
    }

    private static String getRandomFileName() {//生成随机图片名（当前年月日+随机五位数）
            //获取随机五位数
        int rannum=r.nextInt(89999)+10000;
        String nowTimeStr=simpleDateFormat.format(new Date());//获取当前日期
         return nowTimeStr+rannum;
    }

    public static void deleteFileOrPath(String storePath){
        File fileOrPath =new File(PathUtil.getImgBasePath()+storePath);//得到文件或者目录的路径
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()) {//如果是路径，删除路径下的所有文件
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();//最后无论是文件还是目录都删除
        }
    }


}
