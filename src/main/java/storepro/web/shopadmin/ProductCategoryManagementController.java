package storepro.web.shopadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import storepro.dto.ProductCategoryExecution;
import storepro.dto.Result;
import storepro.entity.ProductCategory;
import storepro.entity.Shop;
import storepro.entity.ShopCategory;
import storepro.enums.ProductCategoryStateEnum;
import storepro.execuptions.ProductCategoryOperationException;
import storepro.service.ProductCategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    /*
     * 获取某个店铺下的所有商品类别
     * */
    @RequestMapping(value = "/getproductcategorybyshopid", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {

        //从ShopManagementController中的getShopManageinfo方法中已经设置里这样的信息current中获取shopid
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if (currentShop != null && currentShop.getShopId() > 0) {//如果shopid不为空，且大于0
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, list);//通过result正确的构造器将前端所需内容存入
        } else {//如果失败
            ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false, productCategoryStateEnum.getState(), productCategoryStateEnum.getStateInfo());
        }
    }

    /*批量增加商品类别*/
    @RequestMapping(value = "/addproductcategory", method = RequestMethod.POST)
    @ResponseBody
    /*
     * @RequestBody：能自动接收前台传来的内容
     * */
    private Map<String, Object> addProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryList != null && productCategoryList.size() > 0) {//如果符合条件
            //获得店铺信息
            //这里获得id是通过shopmanage的controller当进入商品管理界面时从前端传来的shopid进行操作的getshopmanageinfo方法传来的
            Shop current = (Shop) request.getSession().getAttribute("currentShop");
            for (ProductCategory pc : productCategoryList) {//给每个商品附上店铺id，这个店铺信息通过session
                // 保存
                pc.setShopId(current.getShopId());
            }

            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchInsertProductCategory(productCategoryList);//我们插入商品,交给sevice实现
                if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);//返回一个成功处理的信
                    modelMap.put("effectNum", productCategoryExecution.getCount());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
            return modelMap;
        }
        return modelMap;
    }

    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    /*
     * @RequestBody：能自动接收前台传来的内容
     * */
    private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {//如果符合条件
            //获得当前店铺信息
            //这里获得id是通过shopmanage的controller当进入商品管理界面时从前端传来的shopid进行操作的
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop != null && currentShop.getShopId() != null) {//确认商品是否为空
                try {
                    ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());//我们插入商品,交给sevice实现
                    if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);//返回一个成功处理的信
                        modelMap.put("effectNum", productCategoryExecution.getCount());
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", productCategoryExecution.getStateInfo());
                    }
                } catch (ProductCategoryOperationException e) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.toString());
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择输入一个商品类别");
            return modelMap;
        }
        return modelMap;
    }


}
