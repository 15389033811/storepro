package storepro.enums;

import storepro.entity.LocalAuth;

public enum LocalAuthStateEnum {
    //设置各种状态码
    LOGINFAIL(-1, "密码或帐号输入有误"), SUCCESS(0, "操作成功"), NULL_AUTH_INFO(-1006,
            "注册信息为空");
    private  int state;
    private String stateInfo;

    //私有化构造函数，禁止外部修改函数
    private LocalAuthStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static LocalAuthStateEnum stateOf(int index) {
        for (LocalAuthStateEnum localAuthStateEnum:values()) {//遍历枚举
          if (localAuthStateEnum.state==index){//找到了对应状态码的信息
              return  localAuthStateEnum;//返回对应的信息
          }
        }
        return null;
    }

}
