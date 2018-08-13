package storepro.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;

public class ShopCategoryTest extends BaseTest {
    @Autowired
    private  ShopCategoryService shopCategoryService;
           @Test
            public void testShopCategoryList(){


                System.out.println( shopCategoryService.getShopCategoryList(null));
            }
}
