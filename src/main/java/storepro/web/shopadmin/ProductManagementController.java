package storepro.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import storepro.dto.ImageHolder;
import storepro.dto.ProductExecution;
import storepro.entity.Product;
import storepro.entity.ProductCategory;
import storepro.entity.Shop;
import storepro.enums.ProductStateEnum;
import storepro.execuptions.ProductOperationException;
import storepro.service.ProductCategoryService;
import storepro.service.ProductService;
import storepro.util.CodeUtil;
import storepro.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductManagementController {
 @Autowired
    private ProductService productService;
 @Autowired
    private ProductCategoryService productCategoryService;
 //支持上传商品详情图的最大数
     private  static final int IMAGEMAXCOUNT=6;
    @RequestMapping(value = "/addproduct" ,method = RequestMethod.POST)
    @ResponseBody
    //新增商品
 private Map<String,Object> addProduct(HttpServletRequest request) {
     Map<String, Object> modelMap = new HashMap<String, Object>();
     //验证码校验
     if (!CodeUtil.checkVerifyCode(request)) {//当验证码错误时
         modelMap.put("success", false);
         modelMap.put("errMsg", "输入了错误的验证码");
         return modelMap;
     }
     //接受前端传来的参数
     ObjectMapper mapper = new ObjectMapper();
     Product product = null;
     String productStr = HttpServletRequestUtil.getString(request, "productStr");
     MultipartHttpServletRequest multipartRequest = null;//用来获取request文件流的形式
     ImageHolder thumbnail = null;//存储缩略图
     List<ImageHolder> productImgList = new ArrayList<ImageHolder>();//存储详情图的集合
     CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());//从request中获取文件流
     try {
         // 若请求中存在文件流，则取出相关的文件(包括缩略图和详情图)
         if (multipartResolver.isMultipart(request)) {
             multipartRequest = (MultipartHttpServletRequest) request;
             //取出文件流构建缩略图
             CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
             thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
             //去除缩略图，最多支撑6个文件的上传
             for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                 CommonsMultipartFile thumbnailFileList = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);//从requst获得想要的数据
                 if (thumbnailFileList != null) {
                     //若去取出图片的第i个详情图片文件流不为空，则将其加入详情列表
                     ImageHolder productImg = new ImageHolder(thumbnailFileList.getOriginalFilename(), thumbnailFileList.getInputStream());
                     productImgList.add(productImg);//加入文件
                 } else {
                     //若取出第i个图片详情流为空，则终止循环
                     break;
                 }
             }

         } else {
             //没有文件流上传
             modelMap.put("success", false);
             modelMap.put("errMsg", "上传文件不能为空");
             return modelMap;

         }
     } catch (IOException e) {
         modelMap.put("success", false);
         modelMap.put("errMsg", e.toString());
         return modelMap;
     }
     try {
         //存入shp信息
         //先从前端获得传来的数据
         product = mapper.readValue(productStr, Product.class);
     } catch (Exception e) {
         modelMap.put("success", false);
         modelMap.put("errMsg", e.toString());
         return modelMap;
     }
     //若product里的重要信息不为空，且前面处理的缩略图和商品图不为空，我们开始添加product
     if (product != null && thumbnail != null && productImgList.size() > 0) {
         try {
             //从session中获取shop_id
             Shop current = (Shop) request.getSession().getAttribute("currentShop");//根据shop信息获取shop内容（这的信息是我们后台在选好店铺时设置的）
             Shop shop = new Shop();
             shop.setShopId(current.getShopId());
             product.setShop(shop);
             //执行添加操作
             ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);//加入数据库
             if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                 modelMap.put("success", true);
             } else {
                 modelMap.put("success", false);
                 modelMap.put("errMsg", pe.getStateInfo());
             }
         } catch (RuntimeException e) {
             modelMap.put("success", false);
             modelMap.put("errMsg", e.toString());
             return  modelMap;
         }
     } else {
         modelMap.put("success", false);
         modelMap.put("errMsg", "请输入商品信息");

     }
     return modelMap;

 }


/**
 * 通过商品id获得商品信息
 * 我们不仅要加载商品信息，还要加载当前商铺的商品所有类型，使其显示在下拉框让客户完成选择
 */
    @RequestMapping(value ="/getproductbyid" ,method = RequestMethod.GET)
@ResponseBody
private Map<String,Object> getProductById(@RequestParam long productId){
    Map<String,Object>  modelMap=new HashMap<String, Object>();//返回给前台使用
    if (productId>0){
        Product product=productService.getProductById(productId);//通过商品id获得商品数据
        List<ProductCategory> productCategoryList=productCategoryService.getProductCategoryList(product.getShop().getShopId());//通过商铺id获取商铺所有商品类别
        modelMap.put("product",product);
        modelMap.put("productCategoryList",productCategoryList);
        modelMap.put("success",true);
    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
    }

    return modelMap;

    }



    /*
    * 我们完成修改product的controller操作
    * */
    @RequestMapping(value ="/modifyproduct" ,method = RequestMethod.POST)
    @ResponseBody
    private  Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 是商品编辑时候调用还是上下架操作的时候调用
        // 若为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码检验
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误！");
            return modelMap;
        }
        Product product = null;
        // 接收前端传递过来的product
        String productStr = null;
        // 商品图片缩略图（输入流和名称的封装类）
        ImageHolder thumbnail = null;

        // 将HttpServletRequest转型为MultipartHttpServletRequest，可以很方便地得到文件名和文件内容
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        // 接收商品缩略图
        CommonsMultipartFile thumbnailFile = null;
        // 接收商品详情图片
        List<ImageHolder> productDetailImgList = new ArrayList<ImageHolder>();

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // Step2: 使用FastJson提供的api,实例化Product 构造调用service层的第一个参数
        ObjectMapper mapper = new ObjectMapper();
        // 获取前端传递过来的product,约定好使用productStr
        try {
            productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // Step3: 商品缩略图 和 商品详情图 构造调用service层的第二个参数和第三个参数
        try {
            // 判断 request 是否有文件上传,即多部分请求
            if (commonsMultipartResolver.isMultipart(request)) {
                // 将request转换成多部分request
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;

                // 得到缩略图的CommonsMultipartFile ,和前端约定好使用thumbnail 传递
                // ，并构建ImageHolder对象
                thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                // 转化为ImageHolder，使用service层的参数类型要求
                thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());

                // 得到 商品详情的列表，和前端约定使用productImg + i 传递 ,并构建ImageHolder对象
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productDetailImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productDetailImgFile != null) {
                        ImageHolder productDetailImg = new ImageHolder( productDetailImgFile.getOriginalFilename(),productDetailImgFile.getInputStream());
                        productDetailImgList.add(productDetailImg);
                    } else {
                        // 如果从请求中获取的到file为空，终止循环
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // Step4 调用Service层
        if (product != null ) {
            try {
                // 从session中获取shop信息，不依赖前端的传递更加安全
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                // 调用addProduct
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productDetailImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }


    /*
     * 通过前端传来的数据完成条件查询返回相应列表供前端展示
     *
     * */
    @RequestMapping(value ="/getproductlist" ,method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> queryProductList(HttpServletRequest request){
        Map modelMap= new HashMap();//存储结果和数据
        // 获取前端传递过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        // 获取前端传过来的每页要求返回的商品数量
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session中获取shop信息，主要是获取shopId 不依赖前台的参数，尽可能保证安全
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if ((pageIndex > -1) && (pageSize > -1) && currentShop != null && currentShop.getShopId() != null) {
            // 获取前台可能传递过来的需要检索的条件，包括是否需要从某个商品类别以及根据商品名称模糊查询某个店铺下的商品
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition=new Product();
            productCondition=compactProductConditionSearch(currentShop.getShopId(), productCategoryId, productName);
            // 调用服务
            ProductExecution pe = productService.queryProductList(productCondition, pageIndex, pageSize);
            // 将结果返回给前台
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductConditionSearch(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}