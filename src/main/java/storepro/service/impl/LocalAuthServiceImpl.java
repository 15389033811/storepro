//package storepro.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import storepro.dao.LocalAuthDao;
//import storepro.dto.LocalAuthExecution;
//import storepro.entity.LocalAuth;
//import storepro.enums.LocalAuthStateEnum;
//import storepro.service.LocalAuthService;
//
//public class LocalAuthServiceImpl implements LocalAuthService {
//    @Autowired
//    LocalAuthDao localAuthDao;
//    /**
//     * 插入新的账号
//     * 因为没有设置微信账号，我们直接将手动设置个人信息,
//     * 这就是注册页面实现的
//     * */
//    @Override
//    public LocalAuthExecution insertLocalAuth(LocalAuth localAuth) {
//        //判断传入条件是否为空
//        if (localAuth == null || localAuth.getPassword() == null || localAuth.getUserName() == null
//                || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
//            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
//        }
//        LocalAuth tempAuth=localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId())
//          if (tempAuth!=null);//如果用户信息之前存在，直接跳过
//
//        localAuth.
//        return null;
//    }
//
//    @Override
//    public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
//        return localAuthDao.queryLocalByUserNameAndPwd(userName,password);
//    }
//
//    @Override
//    public LocalAuth getLocalAuthByUserId(long userId) {
//        return localAuthDao.queryLocalByUserId(userId);
//    }
//
//    @Override
//    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws RuntimeException {
//        return null;
//    }
//}
