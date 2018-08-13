package storepro.dao;

import org.apache.ibatis.annotations.Param;
import storepro.entity.ShopCategory;

import java.util.List;

public interface    ShopCategoryDao {
    //通过传入相对的商铺类别，返回对应的查询接口
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategory);

}
