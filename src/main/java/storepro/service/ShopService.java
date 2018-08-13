package storepro.service;

import storepro.dto.ImageHolder;
import storepro.dto.ShopExecution;
import storepro.entity.Shop;
import storepro.execuptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {
    //增加店铺
    ShopExecution addShop(Shop shop, ImageHolder imageHolder)throws ShopOperationException;
    //修改店铺
    ShopExecution modifyShop(Shop shop,ImageHolder imageHolder) throws ShopOperationException;
    //通过店铺id获得店铺
    public Shop getByShopId(Long shopId);
    //根据shopCondition分页相应的店铺列表
    public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
}
