package storepro.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.dao.ProductCategoryDao;

public class ProductCategory  extends BaseTest{

    @Autowired
     private  ProductCategoryService productCategoryService;
    @Test
    public void testDeleteProductCategory(){
        storepro.entity.ProductCategory pc=new storepro.entity.ProductCategory();
        pc.setShopId(1L);
        pc.setProductCategoryId(4l);
        productCategoryService.deleteProductCategory(4l,1L);
    }
}
