package storepro.dao;

import org.apache.ibatis.annotations.Param;
import storepro.entity.Shop;

import java.util.List;

public interface ShopDao {
    //新增店铺
    int insertShop(Shop shop);
    //更新店铺
    int updateShop(Shop shop);
    //通过shopid查询店铺
    Shop queryByShopId(long shopId);

    /*
    * 分页查询店铺
    * rowIndex是从第几行查询(数据库中的第一行其实是第0行，查第一行到第五行的数据就是（0,5）)
    * pageSize是从rowIndex起查询几行
    * 注意：这里必须有param，因为多个参数，而且xml文件中没办法写parameter
    * 可选条件有店铺名，店铺状态，店铺类别，区域id，owner
    * */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    //条件查询出的店铺总数
    int queryShopCount(@Param("shopCondition")Shop shopCondition);

}
