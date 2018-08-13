package storepro.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.Area;

import java.util.List;

import static org.junit.Assert.assertEquals;

//继承后每次加载都会加载spring-dao的配置文件
public class AreaDaoTest extends BaseTest{
    @Autowired
    private AreaDao areaDao;

     @Test
    public void testQueryArea(){
         List<Area> areaList=areaDao.queryArea();
         assertEquals(3,areaList.size());
     }

}
