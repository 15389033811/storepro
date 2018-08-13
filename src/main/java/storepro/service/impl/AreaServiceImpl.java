package storepro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storepro.cache.JedisUtil;
import storepro.dao.AreaDao;
import storepro.entity.Area;
import storepro.service.AreaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
     //这里的areaDao由mapper文件自动生成对象并注入ioc容器，所以不需要在AreaDao中注解它为bean
    private AreaDao areaDao;

   @Autowired
    private JedisUtil.Keys jedisKeys;
   @Autowired
   private  JedisUtil.Strings jedisStrings;


   @Transactional
    @Override
    public List<Area> getAreaList() {
        String key=AREALISTKEY;
        //用于接受区域信息
       List<Area> areaList=null;
        ObjectMapper mapper =new ObjectMapper();//用于转换对象和字符串，因为我们存的时候是字符串，读取的时候是对象
        if (!jedisKeys.exists(key)){//如果redis中没有相应的数据
            areaList=areaDao.queryArea();//先获取相应的数据
            String jsonString= null;
            try {
                jsonString = mapper.writeValueAsString(areaList);//转为string类型
            } catch (JsonProcessingException e) {//如果出问题抛出异常
                e.printStackTrace();
                throw new  RuntimeException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);//写入redis服务器
        }else {//如果访问时已经存在key了 ,获取返回
            String jsonString=jedisStrings.get(key);//获取redis服务器的字符串
            //转换                   获取类型的创建工厂
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try {
                areaList=mapper.readValue(jsonString,javaType);//读取相应的值
            } catch (IOException e) {
                e.printStackTrace();
                throw  new RuntimeException();
            }

        }

        return areaList;//返回数据

       // return  areaDao.queryArea();
    }
}
