package storepro.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import storepro.dao.HeadLineDao;
import storepro.entity.HeadLine;
import storepro.entity.ShopCategory;
import storepro.service.HeadLineService;
import storepro.service.ShopCategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用于处理显示前台信息的controller
@Controller
@RequestMapping("/frontend")
public class MainPageController {
@Autowired
    ShopCategoryService shopCategoryService;
@Autowired
    HeadLineService headLineService;
//获取前端需要的头条信息和商品展示列表
@RequestMapping(value = "/listmainpage", method = RequestMethod.GET)
@ResponseBody
private Map<String,Object> listMainPageInfo(){
    Map<String,Object> modelMap=new HashMap<String,Object>();
    List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
    //先获取一级目录,当传入为空时获取一级目录
    try {
        shopCategoryList= shopCategoryService.getShopCategoryList(null);
        modelMap.put("shopCategoryList",shopCategoryList);
    }catch (Exception e){
        modelMap.put("success",false);
        modelMap.put("errMsg",e.getMessage());
    }
    //接下来存入头条
    try {
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);//获取那些状态为1的头条
        List<HeadLine> headLineList = headLineService.queryHeadLineList(headLine);
        modelMap.put("headLineList", headLineList);
    }catch (Exception e){
        e.printStackTrace();
        modelMap.put("errMsg","取出头条失败");
    }
    modelMap.put("success", true);
    return  modelMap;
}
}
