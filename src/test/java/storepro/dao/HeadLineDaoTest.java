package storepro.dao;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.HeadLine;

import java.util.List;

public class HeadLineDaoTest extends BaseTest {

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testSelectHeadLineList() {
        HeadLine headLineConditon = new HeadLine();
        // 状态 0 不可用 1 可用
        headLineConditon.setEnableStatus(1);

        // 查询不可用的头条信息
        List<HeadLine> headLineList = headLineDao.queryHeadLine(headLineConditon);
        System.out.println(headLineList);
    }
}