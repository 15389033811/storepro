package storepro.dao;

import org.apache.ibatis.annotations.Param;
import storepro.entity.HeadLine;

import java.util.List;

 public  interface HeadLineDao {
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
