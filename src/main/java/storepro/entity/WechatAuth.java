package storepro.entity;

import java.util.Date;

public class WechatAuth {//微信账号类
    //微信账号的id
    private Long wechatAuthId;
    //微信账号和项目账号绑定的标识,就是通过用户的id绑定
    private String openId;
    //创建时间
    private Date createTime;
    //用户信息
    private PersonInfo personInfo;

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
