package storepro.dto;

import org.springframework.cglib.core.Local;
import storepro.entity.LocalAuth;
import storepro.enums.LocalAuthStateEnum;

import java.util.List;

public class LocalAuthExecution {
    //状态码
    private int state;
    //状态标识
    private String stateInfo;
    //计数器
    private  int count;

    private LocalAuth localAuth;

    private List<LocalAuth> localAuthList;

    public LocalAuthExecution(){

    }

    //失败的构造方法
    public LocalAuthExecution(LocalAuthStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    //成功的单个构造方法
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, LocalAuth localAuth){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.localAuth=localAuth;
    }

    //成功的多个构造器方法
    public LocalAuthExecution(LocalAuthStateEnum stateEnum, List<LocalAuth> localAuthList,int count){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.localAuthList=localAuthList;
        this.count=count;
    }

}
