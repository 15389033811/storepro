package storepro.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.ShopCategory;

import java.util.List;



public class ShopCategoryTest  extends BaseTest{
    @Autowired
private ShopCategoryDao shopCategoryDao;

    @Test
public void testQueryShopCategoryDao()
    {

//传入一个空的shopCategory
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
         System.out.println(shopCategoryList.size());
        //返回所有记录

//传入一个有父类的shopCategory
        ShopCategory shopCategory1=new ShopCategory();
        ShopCategory shopCategory2=new ShopCategory();
        shopCategory1.setShopCategoryId(1L);
        shopCategory2.setParent(shopCategory1);
        List<ShopCategory> shopCategoryList1 = shopCategoryDao.queryShopCategory(shopCategory2);
//只返回父类店铺类别的店铺类别号为指定店铺类别号的店铺类别


    }
    }
