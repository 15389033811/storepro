package storepro.service;

import storepro.entity.HeadLine;

import java.util.List;

public interface HeadLineService {

   public static  String HLLISTKEY="headlinelist";//redis用到的key
    /**
     *
     *
     * @Title: queryHeadLineList
     *
     * @Description: 查询headLine
     *
     *
     *
     * @return: List<HeadLine>
     */
    List<HeadLine> queryHeadLineList(HeadLine headLineCondition);
}
