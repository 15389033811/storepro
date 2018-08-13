package storepro.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.LocalAuth;
import storepro.entity.PersonInfo;

import java.util.Date;

public class LocalAuthDaoTest extends BaseTest{
 @Autowired
    private LocalAuthDao localAuthDao;



 @Test
 //插入方法
 public void testinsertLocalAuthTest(){
       LocalAuth localAuth=new LocalAuth();
       localAuth.setUserName("yf1");
       localAuth.setPassword("jue123");
       localAuth.setCreateTime(new Date());
       localAuth.setLastEditTime(new Date());

       localAuthDao.insertLocalAuth(localAuth);
 }

 @Test
 //插入用户信息方法
 public void testinsertPersonInfo(){
 PersonInfo personInfo=new PersonInfo();
 personInfo.setName("yf");
 personInfo.setEnableStatus(1);
 personInfo.setUserType(3);
 localAuthDao.insertPersonalInfo(personInfo);
 }

    @Test//查询通过账号密码
    @Ignore
    public void testqueryLocalByUserNameAndPwdTest(){
        String username="yf";
        String password="jue123";
        System.out.println(localAuthDao.queryLocalByUserNameAndPwd(username,password).getPassword());

    }

    @Test//通过用户信息查询信账号
    @Ignore
    public void testqueryLocalByUserIdTest(){

        System.out.println(localAuthDao.queryLocalByUserId(1l).getUserName());


    }

    @Test//更改账号密码
    public void  updateLocalAuthTest(){

        System.out.println(localAuthDao.updateLocalAuth(1L,"yf",
                "jue123","jue123123",new Date()));

    }



}
