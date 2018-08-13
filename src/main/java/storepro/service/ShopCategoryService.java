package storepro.service;

import storepro.entity.ShopCategory;

import java.util.List;

public interface    ShopCategoryService {
    public  static  String SCLISTKEY="shopcategorylist";//redis中设置键值对的key
    //获得店铺类别的列表
    List<ShopCategory>  getShopCategoryList(ShopCategory shopCategoryCondition);
}
