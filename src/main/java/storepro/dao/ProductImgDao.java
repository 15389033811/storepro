package storepro.dao;

import storepro.entity.Product;
import storepro.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    //商品图片批量添加的方法
 int batchInsertProductImg(List<ProductImg>  productImgList);
 //由于当我们新传入商品照片详情时，我们的操作是删除原有得照片所以需要实现删除商品详情图
    int deleteProductImgById(long productId);
    //通过商品id获得
}
