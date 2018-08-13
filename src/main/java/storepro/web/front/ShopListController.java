package storepro.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import storepro.dto.ShopExecution;
import storepro.entity.Area;
import storepro.entity.ProductCategory;
import storepro.entity.Shop;
import storepro.entity.ShopCategory;
import storepro.enums.ShopStateEnum;
import storepro.execuptions.ShopOperationException;
import storepro.service.AreaService;
import storepro.service.ShopCategoryService;
import storepro.service.ShopService;
import storepro.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {
    @Autowired
    ShopCategoryService shopCategoryService;
    @Autowired
    AreaService areaService;
    @Autowired
    ShopService shopService;
    /*
     *    当我们点击某个一级类别时，我们进入到商铺列表的时候，上面还有一排供选择二级商铺类别，我们的任务就是显示这个
     *    还有就是直接点击全部，会显示所有商铺类别
     *    还需要展示地区，供选择
     * */

    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //设置对象传入方法
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        try {
            if (parentId != -1) {//如果获取的shopCategoryId不等于-1，-1为点击全部时的显示
                ShopCategory shopCategory = new ShopCategory();//
                ShopCategory shopCategoryParent = new ShopCategory();
                shopCategoryParent.setShopCategoryId(parentId);
                shopCategory.setParent(shopCategoryParent);
                //调用方法，查找相应的二级类别
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);//只传入一级类别的id即可,
                modelMap.put("shopCategoryList", shopCategoryList);//存入信息

            } else {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);//获取所有商品类别
                modelMap.put("shopCategoryList", shopCategoryList);//存入信息
            }
        } catch (ShopOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //获取所有地区供查询
        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);//存入信息
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        modelMap.put("success", true);//成功标识
        return modelMap;
    }


    /*
     * 根据传入的条件查询店铺
     * */
    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> modelMap = new HashMap<String, Object>();
      //先获取前台传来的数据()
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        try {
            if ((pageIndex > -1) && (pageSize > -1)){
            //获取组合查询条件
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            String shopName=HttpServletRequestUtil.getString(request, "shopName");


            // 封装查询条件

            Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
            // 调用service层提供的方法,获取条件查询的语句
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("shopList", se.getShopList());//交给前端
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
    }catch (Exception e){
        modelMap.put("success" ,false);
        modelMap.put("errMsg" ,e.getMessage());
    }
        return modelMap;
    }

    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) throws UnsupportedEncodingException {
        Shop shopCondition=new Shop();
        if (parentId>-1){
            ShopCategory shopCategory=new ShopCategory();
            ShopCategory shopCategoryParent=new ShopCategory();
            shopCategoryParent.setShopCategoryId(parentId);
            shopCategory.setParent(shopCategoryParent);
            shopCondition.setShopCategory(shopCategory);
        }
        //判断shopCategoryId如果这个参数有值，代表我们通过二级商铺类型查找店铺
        if (shopCategoryId>-1){
            ShopCategory shopCategory=new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }

        //判断areaid是否有值
        if (areaId>-1){
            Area area =new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);

        }
        //判断shopName是否有值
        if (shopName!=null){
            shopName= new String(shopName.getBytes("ISO-8859-1"), "UTF-8");
            shopCondition.setShopName(shopName);
        }
        // 查询状态为审核通过的商铺
        shopCondition.setEnableStatus(1);
        return  shopCondition;
    }
}
