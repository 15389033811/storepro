package storepro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storepro.dao.ShopDao;
import storepro.dto.ImageHolder;
import storepro.dto.ShopExecution;
import storepro.entity.Shop;
import storepro.enums.ShopStateEnum;
import storepro.execuptions.ShopOperationException;
import storepro.service.ShopService;
import storepro.util.ImageUtil;
import storepro.util.Pagecalculator;
import storepro.util.PathUtil;

import java.beans.Transient;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService{
    @Autowired
    private   ShopDao shopDao;//自动生成shopDao

  /*
  * 获取商品列表
  * 注意因为我们还要返回总数，所以返回类型是Shopexecution
  * 而且前端处理只能处理页数，而我们的dao层处理的是行（rowIndex）
  * 所以需要工具类转化
  * */


    /*
    * 根据shopid获取shop信息
    * */
    @Transactional
    public Shop getByShopId(Long shopId){
      return shopDao.queryByShopId(shopId);
    }

    /*
    * 根据页数和显示数量和shop条件得到shoplist的信息和count
    * */
    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //先通过每页容纳的数量和页数将pageIndex转为rowIndex
        int rowIndex= Pagecalculator.calculateRowIndex(pageIndex,pageSize);
        //返回shop的list集合
        List<Shop> shopList=shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        //计算符合条件的总记录数
        int count=shopDao.queryShopCount(shopCondition);
        //建立存储对象
        ShopExecution shopExecution=new ShopExecution();
        if(shopList!=null){
          shopExecution.setShopList(shopList);
          shopExecution.setCount(count);
        }else {
            shopExecution.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution; //成功返回存储shop集合的shopExecution
    }

    /*
    *  更新店铺
    * */
    @Transactional
    public ShopExecution modifyShop(Shop shop,ImageHolder imageHolder) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            //1.判断是否需要处理图片
            try {
                if (imageHolder!=null) {
                    if (imageHolder.getImage() != null && imageHolder.getImageName() != null && !"".equals(imageHolder.getImageName())) {//输入流文件名都不能为空
                        Shop tempShop = shopDao.queryByShopId(shop.getShopId());//处理图片
                        if (tempShop.getShopImg() != null) {//判断店铺是否有照片，如果有，则删除
                            ImageUtil.deleteFileOrPath(tempShop.getShopImg());

                        }
                        addShopImg(shop, imageHolder);//加入新的照片
                    }
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());//更改新更新的时间
                int effectnum = shopDao.updateShop(shop);//返回更新的行数
                if (effectnum <= 0) {//如果没操作成功
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());//返回更改成功的店铺
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error"+e.getMessage());
            }
        }
    }


    /*
    * 注册商铺的service层操作
    * */
    @Override
    @Transactional  //说明是事物操作，错误的话要回滚，只有抛出运行时异常才能回滚
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {

        if (shop==null) {//检查shop是否为空
         return new ShopExecution((ShopStateEnum.NULL_SHOP));//为空则调用创建店铺失败的构造器
        }
        try{
            shop.setEnableStatus(0);//过了非空检查只要不出错，则会创建成功，那么EnableStatus的值为 0说明此时正在审核。
            shop.setCreateTime(new Date());//存入创建时间
            shop.setLastEditTime(new Date());//此时创建时间就是最后修改时间
            int effectedNum=shopDao.insertShop(shop);//插入记录
            if(effectedNum<=0) {//说明没有插入成功,抛出异常
               throw new ShopOperationException("店铺创建失败");
            }else{//创建成功
                if(imageHolder.getImage()!=null) {
                    try{
                        addShopImg(shop,imageHolder);//存储商铺图片
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error:"+e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum=shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }

            }
        }catch (Exception e){
                throw new ShopOperationException("addrShop error"+e.getMessage());//显示错误信息
        }

        return new ShopExecution(ShopStateEnum.CHECK,shop);//操作成功
    }

    private void addShopImg(Shop shop,ImageHolder imageHolder) {//添加图片的方法
        String dest= PathUtil.getShopImagePath(shop.getShopId());//通过id得知照片应该存在哪个文件夹下
        String ShopImgAddr= ImageUtil.generateThumnail(imageHolder,dest);//得到文件的全限定名,并且创建文件
        shop.setShopImg(ShopImgAddr);//存入图片路径(这里并没有根路径，根路径是在上个方法里用来寻找照片存放位置的)
    }
}
