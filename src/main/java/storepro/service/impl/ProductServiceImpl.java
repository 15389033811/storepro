package storepro.service.impl;

import ch.qos.logback.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storepro.dao.ProductDao;
import storepro.dao.ProductImgDao;
import storepro.dto.ImageHolder;
import storepro.dto.ProductExecution;
import storepro.entity.Product;
import storepro.entity.ProductImg;
import storepro.enums.ProductStateEnum;
import storepro.execuptions.ProductOperationException;
import storepro.execuptions.ShopOperationException;
import storepro.service.ProductService;
import storepro.util.ImageUtil;
import storepro.util.Pagecalculator;
import storepro.util.PathUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;
    @Override
    /*
     * 首先我们获得前台传来的数据
     * 1.获取缩略图，对缩略图进行存储，存储到product的shop_img里
     * 2.将信息存入tb_product中，获取product_id
     * 3.通过product获取product_id将商铺详情图处理后写入tb_produc_img中
     * */
    @Transactional//事务处理，因为很多步，所以要事务处理
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {//如果传入的信息有效
            //设置默认的属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);//1表示商品可以显示在前端
            if (imageHolder != null) {//如果商品缩略图不为空，添加到指定目录
                addProductImg(product, imageHolder);//写入商品缩略图，并将缩略图的地址交给product
            }
            try {
                int effectnum = productDao.insertProduct(product); //将product信息写入
                if (effectnum <= 0) {
                    throw new ShopOperationException("商品创建失败");
                }
            } catch(Exception e) {
                throw new ProductOperationException("创建商品失败"+e.toString());
            }
               //如果商品添加成功，我们要继续处理详情图的内容
                if (imageHolderList!=null&&imageHolderList.size()>0){
                  addProductImgList(product,imageHolderList);//将商品详情图存入，并且防止
                }
                   return  new ProductExecution(ProductStateEnum.SUCCESS,product);
            }else{

            return  new ProductExecution(ProductStateEnum.INNER_ERROR);
        }
    }

    //根据商品id获取商品信息,修改商品界面初始化用
    @Override
    public Product getProductById(long productId) { return productDao.getProductById(productId);}


    /*
    * 根据前端传入的信息我们进行修改
    * 如果更改了product的里的属性我们就更改那些更改了的属性
    * 如果传入文件缩略图，我们就对原来的照片采取删除操作重新存储，且把地址写入product表中
    * 如果传入商品详情图，我们就把原来的商品详情图全部删除，存入新的并更新Img表
    * */
    @Override
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList) throws ProductOperationException {
        //先设置属性
        if (product != null && product.getShop()!=null&&product.getShop().getShopId() != null ) {
            product.setLastEditTime(new Date());
            //如果传入的缩略图不为空我们更新缩略图
            if (imageHolder!=null){
                //通过商品id获取商品的信息
                Product tempProduct=productDao.getProductById(product.getProductId());
                if (tempProduct.getImgAddr()!=null){//删除旧的缩略图，因为缩略图不想商品详情图存入单独一个表，所以我们不用再写一个dao
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                //添加缩略图
                addProductImg(product,imageHolder);
            }
            //接下来我们完成对商品详情图的处理
            if(prodImgDetailList!=null&&prodImgDetailList.size()>0){//如果也传入了详情图
                //删除表中详情图片
                deleteProductImgs(product.getProductId());//通过商品id删除详情图库中的记录，并且删除相应文件，还有product表中的详情图相应的属性
                addProductImgList(product,prodImgDetailList);//存入新的详情图
            }
            try {
                //更新product
                int effectNum=productDao.modifyProduct(product);
                if (effectNum<=0){
                    throw new ProductOperationException("商品更新失败");//抛出异常
                }
                return  new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw  new ProductOperationException("商品更新失败"+e.getMessage());
            }
        }
        else {
            return  new ProductExecution(ProductStateEnum.NULL_PARAMETER);
        }
    }


    //通过详情条件进行分页查询，获取商品列表
    @Override
        public ProductExecution queryProductList(Product product, int pageIndex, int pageSize) {
        List<Product> productList=null;//查到的商品列表
        int count=0;//总数
       //传入的是页数和当前页的第几个，我们通过页数转行数的转换工具来转换
        try {
            int rowIndex= Pagecalculator.calculateRowIndex(pageIndex,pageSize);
                productList=productDao.queryProductList(product,rowIndex,pageSize);
                count=productDao.queryProductCount(product);
        }catch (ProductOperationException e){
            new ProductExecution(ProductStateEnum.INNER_ERROR);
        }
        return new ProductExecution(ProductStateEnum.SUCCESS,productList,count);

    }






    private void addProductImg(Product product, ImageHolder imageHolder) {//存入图片的方法
        // 根据shopId获取图片存储的相对路径
        String relativePath = PathUtil.getProductImagePath(product.getProductId(),product.getShop().getShopId());
        // 添加图片到指定的目录
        String relativeAddr = ImageUtil.generateThumnail(imageHolder, relativePath);
        // 将relativeAddr设置给product
        product.setImgAddr(relativeAddr);
    }



    /**
     * 批量添加商品图片
     *
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        // 获取图片存储路径，这里直接存到相应店铺的文件夹下
        String desc = PathUtil.getProductDetaisImagePath(product.getProductId(),product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();

        // 遍历图片依次去处理，并添加进 productImg 实体类中
        for (ImageHolder productImageHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateNormalImg(productImageHolder, desc);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        // 如果确定是有图片需要添加的，就执行批量添加操作
        if (productImgList.size() > 0) {
            try {

                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败!");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败，" + e.toString());
            }
        }
    }

    private void deleteProductImgs(Long productId) {
        // 获取该商铺下对应的productImg信息
        List<ProductImg> productImgList = productDao.getProductById(productId).getProductImgList();
        // 遍历删除该目录下的全部文件
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        // 删除tb_product_img中该productId对应的记录
        productImgDao.deleteProductImgById(productId);

    }


}