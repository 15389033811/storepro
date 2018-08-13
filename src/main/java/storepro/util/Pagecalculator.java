package storepro.util;

public class Pagecalculator {
    public static int calculateRowIndex(int pageIndex,int pageSize){//通过传来的页数变为行数供dao操作
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
        //假如我们传入1，pageIndex为5就是第一页，那么我们要从第（1-0）*pageSize处开始，也就是1
        //假如我们传10，pageIndex为5，那么就是从第（10-1）*5=45 行开始
    }
}
