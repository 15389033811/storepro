package storepro.service;

import storepro.dto.ImageHolder;
import storepro.dto.ProductExecution;
import storepro.entity.Product;
import storepro.execuptions.ProductOperationException;

import java.util.List;

public interface ProductService {
    //添加商品信息以及图片处理(这里图片当上传缩略图时上传单个图片，其他图片可以单个或多个所以用list传入)
    // ImageHolder类型为缩略图
    // List<ImageHolder> 为详情图，就是多个的
     ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException;
     //通过商品id获得商品信息
    Product getProductById(long productId);
    //修改商品信息
    ProductExecution modifyProduct(Product product,ImageHolder imageHolder, List<ImageHolder> prodImgDetailList) throws ProductOperationException;
    //通过条件获得商品列表
    ProductExecution  queryProductList(Product product,int pageIndex,int pageSize);



}
