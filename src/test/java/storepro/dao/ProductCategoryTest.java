package storepro.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.ProductCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");

    @Test
    public void testQueryShopList(){
        List<ProductCategory> listProduct= productCategoryDao.queryProductCategoryList(1L);
          System.out.println(listProduct.get(0).getProductCategoryId());
    }

    @Test
    public  void testBatchInsertProduct() throws ParseException {
        ProductCategory pc1= new ProductCategory();
        ProductCategory pc2= new ProductCategory();
        pc1.setShopId(1L);
        pc2.setShopId(1L);
        pc1.setCreateTime(new Date());
        pc2.setCreateTime(new Date());
        pc1.setPriority(1)  ;
        pc2.setPriority(1)  ;
        pc1.setProductCategoryName("卡布奇诺");
        pc2.setProductCategoryName("奶茶");
        pc1.setProductCategoryId(1L);
        pc2.setProductCategoryId(1L);
        List<ProductCategory> list=new ArrayList<ProductCategory>();
        list.add(pc1);
        list.add(pc2);
        productCategoryDao.batchInsertProductCategory(list);

 }

 @Test
    //删除商品类别
    public void deleteProductCategory(){
        ProductCategory pc=new ProductCategory();
        pc.setShopId(1L);
        pc.setProductCategoryId(4l);
        productCategoryDao.deleteProductCategory(pc.getProductCategoryId(),pc.getShopId());
 }
}