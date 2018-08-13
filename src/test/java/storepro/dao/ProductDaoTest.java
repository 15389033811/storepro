package storepro.dao;

import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.*;

import java.util.Date;
import java.util.List;

public class ProductDaoTest  extends BaseTest{
    @Autowired
    ProductDao productDao;

    @Test
    //列表展示
    public void testQueryProductList(){
        Product shop=new Product();
        Shop shop1=new Shop();
       shop1.setShopId(8L);
       shop.setShop(shop1);
     List<Product> list= productDao.queryProductList(shop,0,20);
     System.out.println(productDao.queryProductCount(shop));
       System.out.println(list.size());
    }


    @Test
    public void testModifyProduct(){
       Product product=new Product();
//       product.setProductId(36l);
//        product.setProductName("testmodify");
//        product.setProductDesc("TESTmodify");
//        product.setImgAddr("图片");
//        product.setNormalPrice("123");
//        product.setPromotionPrice("15");
//        product.setPriority(1);
//        product.setCreateTime(new Date());
//        product.setLastEditTime(new Date());
        product.setProductId(10l);
        product.setEnableStatus(1);
        Shop shop=new Shop();
        shop.setShopId(1l);
        product.setShop(shop);
//        ProductCategory productCategory=new ProductCategory();
//        productCategory.setProductCategoryId(7L);
//        product.setProductCategory(productCategory);
//        Shop shop=new Shop();
//        shop.setShopId(2L);
//        product.setShop(shop);
        productDao.modifyProduct(product);
    }


    @Test
    public void testgetProductById(){
     long x=2;
System.out.println(productDao.getProductById(x).getProductId());

//System.out.println(productDao.getProductById(x).getProductCategory().getProductCategoryId()+" "+productDao.getProductById(x).getShop().getShopId());

    }

    @Test
    public    void testUpdatProductCategoryToNull() {
         long productCategory=3l;

         System.out.println(productDao.updateProductCategoryToNull(productCategory));

     }


    @Test
    public    void testInsetProduct(){
        Product product=new Product();
        product.setProductName("test");
        product.setProductDesc("TEST");
        product.setImgAddr("图片");
        product.setNormalPrice("123");
        product.setPromotionPrice("15");
        product.setPriority(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
       product.setEnableStatus(1);
        ProductCategory productCategory=new ProductCategory();
        productCategory.setProductCategoryId(3L);
       product.setProductCategory(productCategory);
       Shop shop=new Shop();
       shop.setShopId(1L);
       product.setShop(shop);
      System.out.println(productDao.insertProduct(product));
      System.out.println(product.getProductId()+""+product.getCreateTime());


    }
}
