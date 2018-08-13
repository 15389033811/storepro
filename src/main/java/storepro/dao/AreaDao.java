package storepro.dao;

import storepro.entity.Area;

import java.util.List;

public interface    AreaDao {

    //列出区域列表
    List<Area>  queryArea();
}
