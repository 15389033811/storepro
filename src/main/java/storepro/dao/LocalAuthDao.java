package storepro.dao;

import org.apache.ibatis.annotations.Param;
import storepro.entity.LocalAuth;
import storepro.entity.PersonInfo;

import java.util.Date;

public interface LocalAuthDao {
    /*
    * 通过账号密码查询信息，登录用
    * */
    LocalAuth queryLocalByUserNameAndPwd(@Param("username")String username,@Param("password")String password);
    /*
    * 通过用户id查询对应localauth
    * */
    LocalAuth queryLocalByUserId(@Param("userId")long userId);
    /*
    * 添加平台账号
    * */
     int insertLocalAuth(LocalAuth localAuth);
    /*
     * 添加用户信息
     * */
    int insertPersonalInfo(PersonInfo personInfo);

     /*
     * 通过userId和userName还有password更改密码
     * */
     int updateLocalAuth(@Param("userId")Long userId,@Param("username")String username,@Param("password")String password,
     @Param("newPassword")String newPassword,@Param("lastEditTime")Date lastEditTime);

}
