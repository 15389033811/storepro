package storepro.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import storepro.BaseTest;
import storepro.entity.HeadLine;

import java.util.List;

public class HeadLineServiceTest extends BaseTest {

    @Autowired
    private HeadLineService headLineService;

    @Test
    public void testQueryHeadLineList() {
        HeadLine headLineConditon = new HeadLine();
        // 状态 0 不可用 1 可用
        headLineConditon.setEnableStatus(1);

        // 查询不可用的头条信息
        List<HeadLine> headLineList = headLineService.queryHeadLineList(headLineConditon);
      System.out.println(headLineList.size());

    }
}