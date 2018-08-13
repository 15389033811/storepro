package storepro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storepro.cache.JedisUtil;
import storepro.service.AreaService;
import storepro.service.CacheService;

import java.util.Set;
@Service
public class CacheServiceImpl implements CacheService{
    @Autowired
   private JedisUtil.Keys jedisKeys;
    @Autowired
    private AreaService areaService;

    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet=jedisKeys.keys(keyPrefix+"*");//找出符合条件的规则key
        for (String key:keySet){
            jedisKeys.del(key);//一一删除
        }
    }

    public void removeAreaFromCache(){
       removeFromCache(areaService.AREALISTKEY);
    }


}
