package storepro.web.shopadmin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import storepro.dto.ImageHolder;
import storepro.dto.ShopExecution;
import storepro.entity.Area;
import storepro.entity.PersonInfo;
import storepro.entity.Shop;
import storepro.entity.ShopCategory;
import storepro.enums.ShopStateEnum;
import storepro.service.AreaService;
import storepro.service.ShopCategoryService;
import storepro.service.ShopService;
import storepro.util.CodeUtil;
import storepro.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller//注解它是一个controller
    @RequestMapping("/shopadmin")//根url
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    /*进入店铺管理界面
    *
    *  从商铺列表页面中，点击“进入”按钮进入
     *               某个商铺的管理页面的时候，对session中的数据的校验从而进行页面的跳转，是否跳转到店铺列表页面或者可以直接操作该页面
    * */
    @RequestMapping(value = "/getshopmanageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopManageinfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 从前端获取shopId，这里我们依赖前端传来的
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        // 如果shopId不合法
        if (shopId < 0) {
            // 尝试从当前session中获取
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop == null) {
                // 如果当前session中也没有shop信息,告诉view层 重定向
                modelMap.put("redirect", true);
                modelMap.put("url", "/storepro/shopadmin/shoplist");
            }else{
                // 告诉view层 进入该页面
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else { // shopId合法的话
            Shop shop = new Shop();
            shop.setShopId(shopId);
            // 将currentShop放到session中
            request.getSession().setAttribute("currentShop", shop);
            modelMap.put("redirect", false);
        }

        return modelMap;
    }


    /*
    * 通过owner查询条件返回当前用户拥有商铺列表
    * */
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private  Map<String, Object> getShopList(HttpServletRequest request) throws JsonProcessingException
    {
                Map<String,Object> modelMap=new HashMap<String,Object>();
                PersonInfo user=new PersonInfo();
                user.setUserId(1L);//先自己手动设置
                user.setName("test");//返回名字，方便前台显示
                request.getSession().setAttribute("user",user);//先手动设置默认值
                user= (PersonInfo) request.getSession().getAttribute("user");//前面是先手动配置，目前功能未完善
                try{
                    Shop shop=new Shop();
                    shop.setOwner(user);
                    ShopExecution shopExecution=shopService.getShopList(shop,0,100);//手动设置输出100个店铺
                    modelMap.put("shopList",shopExecution.getShopList());
                    modelMap.put("user",user);
                    modelMap.put("success",true);
                }catch (Exception e){
                    modelMap.put("success",false);
                    modelMap.put("errMsg",e.getMessage());
                }
                   return modelMap;//返回正确信息

    }

    /*
    * 获取新建店铺时，区域和分类列表的信息，交给前台的下拉框使用
    * */
       @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
       @ResponseBody
   private  Map<String, Object> getShopInitInfo() throws JsonProcessingException {
       Map<String,Object> modelMap=new HashMap<String,Object>();
       List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
       List<Area> areaList=new ArrayList<Area>();
    //ObjectMapper objectMapper=new ObjectMapper();
       try {
           shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
           areaList=areaService.getAreaList();
           modelMap.put("shopCategoryList", shopCategoryList);
           modelMap.put("areaList",areaList);
           modelMap.put("success",true);

       }catch (Exception e){
           modelMap.put("success",false);
           modelMap.put("errMsg",e.getMessage());
           //String str=objectMapper.writeValueAsString(modelMap);
           //return  str;
          return modelMap;
       }
          // String str=objectMapper.writeValueAsString(modelMap);
       //return  str;
          return modelMap;
   }

    /*
    * 根据相应的店铺id获取相应的店铺信息
    * */
    @RequestMapping(value = "getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    public  Map<String,Object> getShopById(HttpServletRequest request){
          Map<String,Object> modelmap=new HashMap<String,Object>();//存放数据的格式
          Long shopId=HttpServletRequestUtil.getLong(request,"shopId");//先将前台获取的id转为Long型，才能操作
          //我们需要查询店铺信息和地域列表信息
        if (shopId>-1) {
            try {
                Shop shop = shopService.getByShopId(shopId);//查询店铺信息
                List<Area> areaList = areaService.getAreaList();//获取店铺列表
                modelmap.put("shop", shop);//存入shop
                modelmap.put("areaList", areaList);//存入区域列表
                modelmap.put("success",true);
            } catch (Exception e) {
                modelmap.put("success", false);
                modelmap.put("errMsg", e.toString());
            }
        }else {
            modelmap.put("success", false);
            modelmap.put("errMsg","emptyshopId");
        }
        return  modelmap;
    }
    @RequestMapping(value="/registershop",method = RequestMethod.POST)
    @ResponseBody//作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据
   /*
   * 注册商铺提交后转到的url
   * 对提交的数据进行处理
   * */
    private Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object>  modelMap=new HashMap<String,Object>();
        //先判断验证码是否正确
        if (!CodeUtil.checkVerifyCode(request)){//当验证码错误时
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
         //1.接受转换相应的参数,包括店铺信息和店铺图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");//通过转换工具类将前端传来的数据转为字符串
        ObjectMapper mapper=new ObjectMapper();//获取处理json的对象
        Shop shop=null;
        try{
            shop=mapper.readValue(shopStr,Shop.class);//将传入的jsonshopStr转为Shop对象并完成赋值
        }catch (Exception e){//出错后输出错误信息
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        /*
        * 操作添加图片
        * */
        CommonsMultipartFile shopImg=null;//spring自带
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );//解析request传来的文件的,通过本次会话的上下文获取相关文件上传的内容
        if (commonsMultipartResolver.isMultipart((request))) {//如果有上传的文件流
            MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;//这样就能提取出request中的文件流了
            shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");//(这个字符串"shopImg"是前端传来的),得到文件
        }else {//如果不具备图片
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop!=null&&shopImg!=null) {//如果接受完相应的参数
//            PersonInfo owner =new PersonInfo();
//            owner.setUserId(1L);
          PersonInfo owner= (PersonInfo) request.getSession().getAttribute("user");//通过session获取对象信息，session在登陆时就设置了
            shop.setOwner(owner);
            ShopExecution shopExecution= null;//不能直接传文件，因为CommonsMultipartFile和File不能直接转换
            try {
                ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());

                shopExecution = shopService.addShop(shop,imageHolder);//经过这步，返回一个商铺状态值，用于判断是否创建成功
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (shopExecution.getState()== ShopStateEnum.CHECK.getState()){//如果操作成功，这里体现了eto的作用之一
                modelMap.put("success",true);
                 List<Shop> shopList= (List<Shop>) request.getSession().getAttribute("shopList");//我们还要显示一个用户可以操作的多个用户列表
                 //操作成功后，将新店铺假如
                if (shopList==null||shopList.size()==0){//如果目前没商铺列表
                        shopList=new ArrayList<Shop>();//新建一个商铺列表
                }
               shopList.add(shop); //加入新建的商铺列表
                request.getSession().setAttribute("shopList",shopList);//将新的商铺列表存入
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",shopExecution.getState());
                return modelMap;
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
        //3.返回结果

    }


    @RequestMapping(value="/modifyshop",method = RequestMethod.POST)
    @ResponseBody//作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据
    /*
     * 注册商铺提交后转到的url
     * 对提交的数据进行处理
     * */
    private Map<String,Object> modifyShop(HttpServletRequest request) throws IOException {
        Map<String,Object>  modelMap=new HashMap<String,Object>();
        //先判断验证码是否正确
        if (!CodeUtil.checkVerifyCode(request)){//当验证码错误时
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //1.接受转换相应的参数,包括店铺信息和店铺图片信息
        String shopStr = HttpServletRequestUtil.getString(request,"shopStr");//通过转换工具类将前端传来的数据转为字符串
        ObjectMapper mapper=new ObjectMapper();//获取处理json的对象
        Shop shop=null;
        try{
            shop=mapper.readValue(shopStr,Shop.class);//将传入的jsonshopStr转为Shop对象并完成赋值
        }catch (Exception e){//出错后输出错误信息
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        /*
         * 操作添加图片
         * */
        CommonsMultipartFile shopImg=null;//spring自带
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );//解析request传来的文件的,通过本次会话的上下文获取相关文件上传的内容
        if (commonsMultipartResolver.isMultipart((request))) {//如果有上传的文件流
            MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;//这样就能提取出request中的文件流了
            shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");//(这个字符串"shopImg"是前端传来的),得到文件

        }
        // 2.修改店铺
        if (shop!=null&&shop.getShopId()!=null) {//如果接受完相应的参数
            PersonInfo owner= (PersonInfo) request.getSession().getAttribute("user");//通过session获取对象信息，session在登陆时就设置了
            shop.setOwner(owner);
            ShopExecution shopExecution= null;//不能直接传文件，因为CommonsMultipartFile和File不能直接转换
            try {
                if (shopImg!=null) {//通过是否有img上传，来确定是否需要更改图片
                    ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    shopExecution = shopService.modifyShop(shop, imageHolder);//经过这步，返回一个商铺状态值，用于判断是否创建成功
                }else {
                    shopExecution = shopService.modifyShop(shop, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (shopExecution.getState()== ShopStateEnum.SUCCESS .getState()){//如果操作成功，这里体现了eto的作用之一
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",shopExecution.getState());
                return modelMap;
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }
        //3.返回结果

    }

}
