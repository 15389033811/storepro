package storepro.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
//主页
    @RequestMapping(value = "/index" ,method = RequestMethod.GET)
    public String index(){
        return "frontend/index";
    }
//商铺列表页路由
    @RequestMapping(value = "/shoplist" ,method = RequestMethod.GET)
    public String showShopList(){
        return "frontend/shoplist";
    }

    //店铺详情页路由
    @RequestMapping(value = "/shopdetail" ,method = RequestMethod.GET)
    public String showShopDetail(){
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/productdetail" ,method = RequestMethod.GET)
    public String showProductDetail(){
        return "frontend/productdetail";
    }
}