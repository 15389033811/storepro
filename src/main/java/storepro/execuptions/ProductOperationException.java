package storepro.execuptions;

public class ProductOperationException extends RuntimeException {
    //添加商品的异常处理
    public ProductOperationException(String msg){
        super(msg);
    }
}
