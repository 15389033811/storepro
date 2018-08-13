package storepro.service;

public interface CacheService {
    /*
    * 因为我们存入的数据都是根据条件存入不同的名称，假设商铺类别来说，一旦我们改动，那么必须更新所有信息
    * 虽然根据条件存入的key的名称不同，但是他们的前缀相同，所以根据前缀删除即可
    * */
    void removeFromCache(String keyPrefix);
    void removeAreaFromCache();
}
