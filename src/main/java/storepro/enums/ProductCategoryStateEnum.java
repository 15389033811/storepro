package storepro.enums;

public enum ProductCategoryStateEnum {

    INNER_ERROR(-1001, "内部出错"), NULL_SHOP(-1001, "操作失败"),EMPTY_LIST(-1002,"添加数少于1")
    ,SUCCESS(1, "操作成功")
    ;

    private int state ;
    private String stateInfo;

    /**
     *
     *
     * @Title:ProductCategoryStateEnum
     *
     * @Description:构造函数
     *
     * @param state
     * @param stateInfo
     */
    private ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     *
     *
     * @Title: stateOf
     *
     * @Description:
     *               通过state获取productCategoryStateEnum,从而可以调用ProductCategoryStateEnum
     *               #getStateInfo()获取stateInfo
     *
     * @param index
     * @return
     *
     * @return: ProductCategoryStateEnum
     */
    public static ProductCategoryStateEnum stateOf(int index) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == index) {
                return productCategoryStateEnum;
            }
        }
        return null;
    }

}
