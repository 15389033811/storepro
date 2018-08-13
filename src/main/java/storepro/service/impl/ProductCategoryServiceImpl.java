package storepro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storepro.dao.ProductCategoryDao;
import storepro.dao.ProductDao;
import storepro.dto.ProductCategoryExecution;
import storepro.entity.ProductCategory;
import storepro.enums.ProductCategoryStateEnum;
import storepro.execuptions.ProductCategoryOperationException;
import storepro.execuptions.ProductOperationException;
import storepro.service.ProductCategoryService;

import java.util.List;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
  private   ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;
    @Override
    //通过商铺id查询出所有商品分类
    public List<ProductCategory> getProductCategoryList(long shopId) {

        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional//事务处理
    //批量增加商品信息
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
      if(productCategoryList!=null&&productCategoryList.size()>0) {//如果传入的list不为空或者list的值大于0
         try{
            int effectNum=productCategoryDao.batchInsertProductCategory(productCategoryList);   //进行加入操作
             if (effectNum<=0){
                 throw new ProductCategoryOperationException("店铺类别创建失败");
             }else {
                 return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS,productCategoryList,effectNum );//操作成功
             }
         }catch (Exception e){
                throw new ProductCategoryOperationException("加入失败"+e.getMessage());
         }

      }else
           return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST );//操作失败
    }


    //删除商品类别，注意删除商品类别时需要先将该商品类别下的商品id置为空，因为有外键约束，所以直接删除会出错

    @Transactional
    @Override
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) {
        try {//删除商品类别下的商品
            int effectNum= productDao.updateProductCategoryToNull(productCategoryId);
            if (effectNum<0)//不能等于0，可能存在商品类别下无商品的情况
            {
                throw  new RuntimeException("商品类别更新失败");
            }
        }catch (ProductOperationException e){
                throw new RuntimeException("deleteProductCategory err"+e.getMessage());
        }

        try {
            int effectNum=productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if (effectNum<=0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);//成功操作
            }
        }catch (ProductCategoryOperationException e){
            throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
        }
    }
}
