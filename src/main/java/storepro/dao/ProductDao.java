package storepro.dao;

import org.apache.ibatis.annotations.Param;
import storepro.entity.Product;
import storepro.entity.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public interface ProductDao {
    //插入商品
    int insertProduct(Product product);
    //修改商品
   int modifyProduct(Product product);
    //通过商品id获取商品信息
    Product getProductById(Long productId);
    //删除商品

    //展示商品列表
    List<Product> queryProductList(@Param("productCondition")Product productCondition,
                                   @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);//返回一个project集合

    //求queryProductList查询出来的商品列表总数
    int queryProductCount(Product productCondition);

    //将商品类别下的所有商品置为空，当我们删除商品类别下需要删除当前商品类别下的所有商品
    int updateProductCategoryToNull(long productCategoryId);
}