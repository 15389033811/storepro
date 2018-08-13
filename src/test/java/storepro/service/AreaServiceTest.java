package storepro.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.Area;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {
    @Autowired
    private AreaService areaService;
    @Test
   // @Ignore
    public void testGetArealList(){
        List<Area> areaList =areaService.getAreaList();
        Assert.assertEquals("西苑",areaList.get(0).getAreaName());
    }
}
