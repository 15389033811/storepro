    package storepro.enums;


    public enum ShopStateEnum {
        //枚举信息的注册
        CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),PASS(2,"通过验证"),INNER_ERROR(-1001,"内部系统错误")
        ,NULL_SHOPID(-1002,"shopID为空"),NULL_SHOP(-1003,"Shop为空");
        private int state;
        private String stateInfo;

        private ShopStateEnum(int state,String stateInfo){//构造方法来获取枚举内容。
            this.state=state;
             this.stateInfo=stateInfo;
        }
        /*
        * 根据传入的state返回相应的enum值
        * */
        public  static ShopStateEnum stateOf(int state){
            for (ShopStateEnum stateEnum:values()) {//values()自动获取枚举中的所有值
                if (stateEnum.getState() == state) {
                    return stateEnum;
                }
            }
            return null;
        }

        public int getState() {
            return state;
        }//我们希望枚举的值由我们设定，不希望其他能改动，所以没setter方法

        public String getStateInfo() {
            return stateInfo;
        }
    }


