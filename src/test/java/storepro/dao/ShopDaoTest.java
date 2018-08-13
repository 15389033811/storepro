package storepro.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import storepro.BaseTest;
import storepro.entity.*;

import java.security.acl.Owner;
import java.util.Date;
import java.util.List;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopListAndCount(){
        Shop shopConditon=new Shop();

shopConditon.setShopName("小龙坎");
       List<Shop> shopList= shopDao.queryShopList(shopConditon,0,5);
      System.out.println(shopList.size()) ;

//       int i=shopDao.queryShopCount(shopConditon);
//       System.out.println(shopList.size());
//       System.out.println(shopList.get(0).getShopName()+" 啦啦啦 "+shopList.get(1).getShopName());
//        System.out.println(i);
    }

    @Test

    public void testQueryShop(){
        long shopId=1;
        Shop shop=shopDao.queryByShopId(8l);
        System.out.println(shop);
    }

    @Test
    @Ignore
    public void testInsertShop(){
        Shop shop=new Shop();
        PersonInfo personInfo=new PersonInfo();
        ShopCategory shopCategory=new ShopCategory();
        Area area =new Area();
        personInfo.setUserId(1L);//long型后面加L
       shopCategory.setShopCategoryId(1L);
        area.setAreaId(2);
        shop.setOwner(personInfo);
        shop.setArea(area);
        shop.setShopName("测试的店铺");
        shop.setShopCategory(shopCategory);
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopDesc("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setAdvice("审核中");
        shop.setEnableStatus(1);
      int effect =shopDao.insertShop(shop);
        Assert.assertEquals(1,effect);
    }

    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop=new Shop();
        shop.setShopId(6L);
        shop.setShopName("测试更新的店铺");
        shop.setShopAddr("test更新");
        shop.setPhone("test更新");
        shop.setShopDesc("test更新");
        shop.setShopImg("test更新");
        shop.setLastEditTime(new Date());
        shop.setAdvice("test更新审核中");

        int effect =shopDao.updateShop(shop);
        Assert.assertEquals(1,effect);
    }
}
