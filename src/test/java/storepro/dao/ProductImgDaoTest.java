package storepro.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.ProductImg;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductImgDaoTest extends BaseTest {
    @Autowired
    ProductImgDao productImgDao;
    @Test
    public void testBatchInsertProductImg(){
        ProductImg productImg=new ProductImg();
        productImg.setImgAddr("test");
        productImg.setImgDesc("test");
                productImg.setPriority(1);
                productImg.setProductId(2L);
                productImg.setCreateTime(new Date());
        List<ProductImg> productImgList=new ArrayList<ProductImg>();
        productImgList.add(productImg);
       productImgDao.batchInsertProductImg(productImgList);
//        productImgDao.batchInsertProductImg(productImgList);
//        System.out.println();
    }

    @Test
    public void testDeleteProductImgByProductId(){
        long productId=22;
        productImgDao.deleteProductImgById(productId);

    }

}
