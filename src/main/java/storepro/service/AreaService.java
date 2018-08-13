package storepro.service;

import storepro.entity.Area;

import java.util.List;

public interface AreaService {
   public   static  String AREALISTKEY="arealist";//redis中存储区域信息的名称
    //获取Area的列表
    List<Area>  getAreaList();
}
