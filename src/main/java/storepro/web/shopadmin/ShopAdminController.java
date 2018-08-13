package storepro.web.shopadmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/shopadmin")
public class ShopAdminController {
//转发到修改商铺页面
    @RequestMapping(value = "/shopoperation", method = RequestMethod.GET)
    public String shopOperation() {
        return "shop/shopoperation";
    }
//转发到用户的拥有的店铺列表
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    public String shopmanagement() {
        return "shop/shoplist";
    }
    //转发到商品管理页面
    @RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
    public String shopManagement() {
        return "shop/shopmanagement";
    }
    //转发到商铺的商品类表
    @RequestMapping(value = "/productcategorymanage",method = RequestMethod.GET)
    public String productcategoryManage(){ return  "shop/productcategorymanage";}

@RequestMapping(value = "/productoperation")
    public String productOperation(){
        return "shop/productoperation";
}

        @RequestMapping(value = "/productmanagement", method = RequestMethod.GET)
    public String productManagement() {
        return "shop/productmanagement";
    }
}

