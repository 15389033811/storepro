package storepro.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.dto.ImageHolder;
import storepro.dto.ProductExecution;
import storepro.entity.Product;
import storepro.entity.ProductCategory;
import storepro.entity.Shop;
import storepro.enums.ProductStateEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest{
    @Autowired
    private ProductService productService;

    @Test
    public void testGetProductById(){
        long product=3l;
       Product product1=productService.getProductById(product);
        System.out.println(product1.getProductName());
    }


    @Test
    public void prodcutService() throws FileNotFoundException {
        Shop shop=new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory=new ProductCategory();
        productCategory.setProductCategoryId(3l);
        Product product=new Product();
        product.setShop(shop);
        product.setProductName("test");
        product.setProductCategory(productCategory);
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建文件缩略图

        File thu=new File("C:\\Users\\YF\\Desktop\\yufei.jpg");
        InputStream inputStream=new FileInputStream(thu);
        ImageHolder imageHolder=new ImageHolder(thu.getName(),inputStream);
        //创建文件详情图片，多个
        File thus1 =new File("C:\\Users\\YF\\Desktop\\20170108165501573.png");
        File thus2 =new File("C:\\Users\\YF\\Desktop\\shopping\\zc.png");
        InputStream inputStreams1=new FileInputStream(thus1);
        InputStream inputStreams2=new FileInputStream(thus2);
       List<ImageHolder> imageHolderList=new ArrayList<ImageHolder>();
       imageHolderList.add(new ImageHolder(thus1.getName(),inputStreams1));
        imageHolderList.add(new ImageHolder(thus2.getName(),inputStreams2));
        ProductExecution productExecution=productService.addProduct(product,imageHolder,imageHolderList);
        System.out.println(productExecution.getStateInfo());


    }
    @Test
    public void testModifyProduct() throws Exception {
//
//        // 注意表中的外键关系，确保这些数据在对应的表中的存在
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setProductCategoryId(7L);
//
//        // 注意表中的外键关系，确保这些数据在对应的表中的存在
//        Shop shop = new Shop();
//        shop.setShopId(2L);
//
//        // 构造Product
//        Product product = new Product();
//        product.setProductName("offical_product");
//        product.setProductDesc("product offical desc");
//
//        product.setNormalPrice("100");
//        product.setPromotionPrice("80");
//        product.setPriority(66);
//        product.setLastEditTime(new Date());
//        product.setProductCategory(productCategory);
//        product.setShop(shop);
//
//        product.setProductId(7L);
//        // 构造 商品图片
//        File productFile = new File("C:\\Users\\YF\\Desktop\\yufei.jpg");
//        InputStream ins = new FileInputStream(productFile);
//        ImageHolder imageHolder = new ImageHolder( productFile.getName(),ins);
//
//        // 构造商品详情图片
//        List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();
//
//        File productDetailFile1 = new File("C:\\Users\\YF\\Desktop\\yufei.jpg");
//        InputStream ins1 = new FileInputStream(productDetailFile1);
//        ImageHolder imageHolder1 = new ImageHolder( productDetailFile1.getName(),ins1);
//
//        File productDetailFile2 = new File("C:\\Users\\YF\\Desktop\\yufei.jpg");
//        InputStream ins2 = new FileInputStream(productDetailFile2);
//        ImageHolder imageHolder2 = new ImageHolder(productDetailFile2.getName(),ins2 );
//
//        prodImgDetailList.add(imageHolder1);
//        prodImgDetailList.add(imageHolder2);
       Product product=new Product();
       product.setProductId(10l);
       product.setEnableStatus(2);
       Shop shop=new Shop();
       shop.setShopId(1l);
       product.setShop(shop);
        // 调用服务
        ProductExecution pe = productService.modifyProduct(product, null, null);
       System.out.println(pe.getProduct().getProductName()+pe.getProduct().getShop().getShopId());

    }
    @Test
    public void testqueryProductList() throws Exception {
        ProductExecution pe=new ProductExecution();
        Product product=new Product();
        Shop shop=new Shop();
        shop.setShopId(2l);
        product.setShop(shop);
        pe=productService.queryProductList(product,1,30);
        System.out.println(pe.getProductList().get(0).getProductId()+" "+pe.getProductList().size());
    }

}
