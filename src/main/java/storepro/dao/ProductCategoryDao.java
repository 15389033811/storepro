package storepro.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.entity.ProductCategory;

import java.util.List;

 public interface ProductCategoryDao {
    /*
    * 查询商品类别
    * */
   List<ProductCategory> queryProductCategoryList(Long shopId);
   /*
   * 批量增加商品
   * */
   int batchInsertProductCategory(List<ProductCategory> productCategoryList);
    /*
    * 删除商品类别
    * */
    int deleteProductCategory(@Param("productCategoryId") long productCategory,@Param("shopId")long shopId);

 }
