package storepro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storepro.cache.JedisUtil;
import storepro.dao.HeadLineDao;
import storepro.entity.HeadLine;
import storepro.service.HeadLineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class HeadLineServiceImpl implements HeadLineService{
    @Autowired
    HeadLineDao headLineDao;
    @Autowired
    JedisUtil.Keys jedisKeys;
    @Autowired
    JedisUtil.Strings jedisStrings;


    @Transactional
    @Override
    public List<HeadLine> queryHeadLineList(HeadLine headLineCondition) {
        //定义key的前缀
        String key=HLLISTKEY;
        //定义接受对象
        List<HeadLine> headLineList=null;
        //设置一个json转换对象
        ObjectMapper objectMapper=new ObjectMapper();

      if (headLineCondition!=null&&headLineCondition.getEnableStatus()!=null){//需要根据我们传入的条件完成查询，（头条是否可用等）
          key=key+"_"+headLineCondition.getEnableStatus();//不同状态的头条查询结果存储到不同key中
      }
      //判断key是否存在
      if (!jedisKeys.exists(key)){//如果不存在
          //取出相应的数据
          headLineList=headLineDao.queryHeadLine(headLineCondition);//查询相应的结果
          String jsonString=null;
          //存入ley
          try {
              jsonString=objectMapper.writeValueAsString(headLineList);
          } catch (JsonProcessingException e) {
              e.printStackTrace();
              throw  new RuntimeException(e.getMessage());
          }
          jedisStrings.set(key,jsonString);//写入服务器
      }else {//如果存在
          String jsonString=jedisStrings.get(key);
          JavaType javaType=objectMapper.getTypeFactory().constructParametricType(ArrayList.class,HeadLine.class);
          try {
              headLineList=objectMapper.readValue(jsonString,javaType);
          } catch (IOException e) {
              e.printStackTrace();
              throw new RuntimeException(e.getMessage());
          }
      }

        return headLineList;
    }

}
