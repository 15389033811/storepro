package storepro.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import storepro.dto.ProductExecution;
import storepro.entity.*;
import storepro.service.ProductCategoryService;
import storepro.service.ProductService;
import storepro.service.ShopService;
import storepro.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping(value = "/frontend")
public class shopDetailController {
    @Autowired
    ShopService shopService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductService productService;
    /*
    * 店铺信息和该店铺下面的商品类别列表
    * */
    @RequestMapping(value = "/listshopdetailpageinfo" ,method=RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        long shopId= HttpServletRequestUtil.getLong(request,"shopId");//获取店铺信息和店铺下的商品类型所用的数据
        List<ProductCategory> productCategoryList=null;//存储商品类别列表
        Shop shop=null;//存储商铺详情信息
        try{

            if (shopId>-1){
               //先求商铺的详情信息
                shop=shopService.getByShopId(shopId);
                modelMap.put("shop",shop);//存储商铺详情信息
                //求商品类型列表
                productCategoryList=productCategoryService.getProductCategoryList(shopId);
                modelMap.put("productCategoryList",productCategoryList);
                modelMap.put("success",true);
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        return modelMap;
    }


    /*
     * 通过查询条件分页获取商品信息
     * */
    @RequestMapping(value = "/listproductsbyshop" ,method=RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductsByShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        //获取分页信息
        int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
        Product productCondition;//存储查询条件用
        ProductExecution productExecution=null;//存储查出来的商品信息
        try{
            if (pageIndex>-1&&pageSize>-1){//判断页码是否符合逻辑
                //获取查询条件
                long shopId= HttpServletRequestUtil.getLong(request,"shopId");//获取店铺ID
                long productCategoryId=HttpServletRequestUtil.getLong(request,"productCategoryId");
                String productName=HttpServletRequestUtil.getString(request,"productName");
               productCondition=compactProductCondition4Search(shopId,productCategoryId,productName);//组合出通过条件查询的内容
                productExecution=productService.queryProductList(productCondition,pageIndex,pageSize);
                modelMap.put("productList",productExecution.getProductList());
                modelMap.put("count",productExecution.getCount());
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","page not corrent");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        return modelMap;
    }



//    /**
//     * 依据查询条件分页列出该店铺下面的所有商品
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
//    @ResponseBody
//    private Map<String, Object> listProductsByShop(HttpServletRequest request) throws UnsupportedEncodingException {
//        Map<String, Object> modelMap = new HashMap<String, Object>();
//        // 获取页码
//        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
//        // 获取一页需要显示的条数
//        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
//        // 获取店铺Id
//        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
//        // 空值判断
//        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
//            // 尝试获取商品类别Id
//            long productCategroyId = HttpServletRequestUtil.getLong(request, "productCategroyId");
//            // 尝试获取模糊查找的商品名
//            String productName = HttpServletRequestUtil.getString(request, "productName");
//            // 组合查询条件
//            Product productCondition = compactProductCondition4Search(shopId, productCategroyId, productName);
//            // 按照传入的查询条件以及分页信息返回相应商品列表以及总数
//            ProductExecution pe = productService.queryProductList(productCondition, pageIndex, pageSize);
//            modelMap.put("productList", pe.getProductList());
//            modelMap.put("count", pe.getCount());
//            modelMap.put("success", true);
//        } else {
//            modelMap.put("success", false);
//            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
//        }
//        return modelMap;
//    }

    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) throws UnsupportedEncodingException {
        Product productCondition=new Product();
        if (shopId>-1){
            Shop shop=new Shop();
            shop.setShopId(shopId);//设置shopid
            productCondition.setShop(shop);
        }
        //判断productCategoryId如果这个参数有值，代表我们通过二级商铺类型查找店铺
        if (productCategoryId>-1){
       ProductCategory productCategory=new ProductCategory();
       productCategory.setProductCategoryId(productCategoryId);
       productCondition.setProductCategory(productCategory);
        }
        //判断productName是否有值
        if (productName!=null){
            productName= new String(productName.getBytes("ISO-8859-1"), "UTF-8");
            productCondition.setProductName(productName);
        }
        // 查询状态为审核通过的商铺
        productCondition.setEnableStatus(1);
        return  productCondition;
    }


}
