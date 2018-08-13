package storepro.util;

public class PathUtil {
    public static String getImgBasePath() {//图片根路径，所有图片存放的路径
        String os = System.getProperty("os.name");//获得系统姓名
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {//不同的系统路径结构不同
            basePath = "D:/store/image/";//win系统的路径
        } else {
            basePath = "/home/yf/image/";//linux之类的系统
        }
        return basePath;
    }

    public static String getShopImagePath(Long shopId) {//图片子路径(每个店铺的照片不同,所以传入一个shopid// )
        String imagePath = "/upload/item/shop/" + shopId + "/";//shopid也变成了路径中的一部分，这样每个商铺的文件夹都有不同的名称
        return imagePath;
    }

    public static String getProductDetaisImagePath(Long productId,Long shopId) {//图片子路径(详细图)
        String imagePath = "/upload/item/shop/" + shopId + "/"+productId+"/"+"details/";//productid也变成了路径中的一部分，这样每个商铺的文件夹都有不同的名称
        return imagePath;
    }

    public static String getProductImagePath(Long productId,Long shopId) {//图片子路径(缩略图 )
        String imagePath = "/upload/item/shop/" + shopId + "/"+productId+"/";//productId也变成了路径中的一部分，这样每个商铺的文件夹都有不同的名称
        return imagePath;
    }



}
