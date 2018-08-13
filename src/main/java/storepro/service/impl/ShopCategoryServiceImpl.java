package storepro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storepro.cache.JedisUtil;
import storepro.dao.ShopCategoryDao;
import storepro.entity.ShopCategory;
import storepro.service.ShopCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
//获取商铺列表
    @Autowired
    ShopCategoryDao shopCategoryDao;

    @Autowired
    JedisUtil.Keys keys;

    @Autowired
    JedisUtil.Strings strings;




    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryContidion) {
        String key=SCLISTKEY;//获取查询的key
        ObjectMapper objectMapper=new ObjectMapper();//转换json格式的对象
        List<ShopCategory> shopCategoryList=null;//获取商铺类别的信息
        //根据不同查询条件，查询内容存入不同键值对
        if (shopCategoryContidion==null){//这是查询所有一级类别的条件
            key=key+"_allfirstlevel";
        }
        else if (shopCategoryContidion!=null &&shopCategoryContidion.getParent()!=null&&shopCategoryContidion.getParent().getShopCategoryId()!=null){
            //查询同一个一级类别的所有二级类别
            key=key+"_parent"+shopCategoryContidion.getParent().getShopCategoryId();
        }else if (shopCategoryContidion!=null){
            //查询所有二级类别
            key=key+"_allsecondlevel";
        }
//判断这样的key是否存在进行相应的操作
        if (!keys.exists(key)) {//如果不存在
            shopCategoryList= shopCategoryDao.queryShopCategory(shopCategoryContidion);//从数据库取出
            String jsonStr=null;
            try {
                jsonStr=objectMapper.writeValueAsString(shopCategoryList);//存入
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            strings.set(key,jsonStr);//写入服务器
        }else {//如果存在
            String jsonStr=strings.get(key);//获取相应的字符串
            JavaType javaType=objectMapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);//将字符串转为相应的类型
            try {
                shopCategoryList=objectMapper.readValue(jsonStr,javaType);//将提出的字符串转为对象
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return shopCategoryList;
    }
}
