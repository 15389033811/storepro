package storepro.web.superadmin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import storepro.entity.Area;
import storepro.service.AreaService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller//注解装配它
@RequestMapping("/superadmin")//注解声明这个类的url根目录
public class AreaController {
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea", method = RequestMethod.GET)//声明这个类的这个方法的子目录和请求数据方法
    @ResponseBody//通过这个注解声明返回类型为json
    private Map<String, Object> listArea() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<Area> list = new ArrayList<Area>();
        list = areaService.getAreaList();

        try {
            modelMap.put("rows", list);
            modelMap.put("totals", list.size());
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            e.printStackTrace();
        }
        return modelMap;
    }
}
