package storepro;

/*
*
*配置spring和junit整合，junit启动时加载SpringIOC容器
* */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//告诉spring通过SpringJUnit4ClassRunner.class来跑
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 配置文件的位置
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml","classpath:spring/spring-redis.xml"})//不能使用通配符，测试了
public  class BaseTest {

}