package storepro.service;

import storepro.dto.ProductCategoryExecution;
import storepro.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    //通过shopid查询商品下的所有类别
    List<ProductCategory> getProductCategoryList(long shopId);
    //批量增加商品类别信息
    ProductCategoryExecution  batchInsertProductCategory(List<ProductCategory> list);

    /*
    * 删除商品类别，还要将此类别下的商品的类别id置为空
    * */
    ProductCategoryExecution  deleteProductCategory(long productCategoryId,long shopId);


}
