package storepro.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;

public class CacheServiceTest extends BaseTest {
    @Autowired
    private  CacheService cacheService;
@Test
    public void testCacheService(){
        cacheService.removeAreaFromCache();
    }
}
