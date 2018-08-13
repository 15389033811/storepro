package storepro.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.dto.ImageHolder;
import storepro.dto.ShopExecution;
import storepro.entity.Area;
import storepro.entity.PersonInfo;
import storepro.entity.Shop;
import storepro.entity.ShopCategory;
import storepro.enums.ShopStateEnum;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;
    @Test
    public void testGetShopList(){//获取shop列表
        Shop shop =new Shop();
        shop.setShopName("小龙坎");
         ShopExecution shopExecutio=shopService.getShopList(shop,1,4);
         System.out.println(shopExecutio.getShopList().size());
         System.out.println(shopExecutio.getCount());

    }

    @Test

    public void testmodifyShop() throws FileNotFoundException {
        Shop shop=new Shop();
        shop.setShopId(3L);
        shop.setShopName("哈哈");
        InputStream file=new FileInputStream(new File("C:\\Users\\YF\\Desktop\\yufei.jpg"));
      ImageHolder imageHolder=new ImageHolder("yufei.jpg",file);
       ShopExecution shopExecution= shopService.modifyShop(shop,imageHolder);
      System.out.println( shopExecution.getShop().getShopImg());
    }
    @Test
    public void testAddShop() throws FileNotFoundException {//测试增加店铺
        Shop shop=new Shop();
        PersonInfo personInfo=new PersonInfo();
        ShopCategory shopCategory=new ShopCategory();
        Area area =new Area();
        personInfo.setUserId(1L);//long型后面加L
        shopCategory.setShopCategoryId(1L);
        area.setAreaId(2);
        shop.setOwner(personInfo);
        shop.setArea(area);
        shop.setShopName("测试的店铺1");
        shop.setShopCategory(shopCategory);
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setShopDesc("test1");
        shop.setShopImg("test1");
        shop.setCreateTime(new Date());
        shop.setAdvice("审核中");
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        File file=new File("C:/Users/YF/Desktop/yufei.jpg");
        InputStream inputStream=new FileInputStream(file);
        ImageHolder imageHolder=new ImageHolder(file.getName(),inputStream);
        ShopExecution se=shopService.addShop(shop,imageHolder);
        Assert.assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }
}
