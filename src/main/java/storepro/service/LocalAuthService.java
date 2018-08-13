package storepro.service;

import storepro.dto.LocalAuthExecution;
import storepro.entity.LocalAuth;

public interface LocalAuthService {
    /**
    * 通过账号密码新增账号
    * */
    LocalAuthExecution  insertLocalAuth(LocalAuth localAuth);

    /**
    * 通过账号密码获取平台信息
    * */
    LocalAuth   getLocalAuthByUsernameAndPwd(String userName, String password);

    /**
    *通过uesrid获取平台信息
    * */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * 修改平台帐号的登录密码
     *
     * @param localAuthId
     * @param userName
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
            throws RuntimeException;



}
