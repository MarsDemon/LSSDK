package longse.com.learing.LRU;

import java.util.AbstractMap;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Function:
 * 1. 在做 key 生成 hashcode 时使用的 HashMap 的 hash 函数
 * 2. 在做 put get 时，如果存在 key 相等时候为了简单没有去比较 equal 和 hashcode
 * 3. 限制大小，map 的最大 size 是1024 ，超过1024 后，就淘汰掉最久没有访问的kv 键值对，当淘汰时，需要调用一个callback I如Callback(K key, V value)
 * 是利用每次 put 都将键值写入一个内部队列，这样纸要判断队列里的第一个即可。
 * 4.具备超时功能，当键值对
 */
public class LRUAbstractMap extends AbstractMap {

    @Override
    public Set<Entry> entrySet() {
        return null;
    }
}
