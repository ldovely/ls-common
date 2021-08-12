package com.ls.redis.util;


import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 基本操作 可扩展,基本够用了
 */

public class RedisRepository {

    private static final long REDIS_EXPIRE = 30; //默认过期时间30分钟
    private static final int REDIS_SCAN_COUNT = 100; //默认一次查100个

    /**
     * Spring Redis Template
     */
    private RedisTemplate<String, Object> redisTemplate;

    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取 RedisTemplate对象
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 是否含有key
     *
     * @param key
     * @return java.lang.Boolean
     * @author xxc
     * @time 2019-1-16 16:48
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置key的过期时间
     *
     * @param key
     * @param expire
     * @param unit   时间模式
     * @return void
     * @author xxc
     * @time 2019-1-16 16:48
     */
    public void setExpire(String key, long expire, TimeUnit unit) {
        redisTemplate.expire(key, expire, unit);
    }

    /**
     * 单个删除key
     *
     * @param key
     * @return java.lang.Boolean
     * @author xxc
     * @time 2019-1-16 16:48
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param key
     * @return java.lang.Long
     * @author xxc
     * @time 2019-1-16 16:49
     */
    public Long delete(List<String> key) {
        return redisTemplate.delete(key);
    }


    /**
     * 设置字符串键值
     *
     * @param key
     * @param value
     * @return void
     * @author xxc
     * @time 2019-1-16 16:49
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, REDIS_EXPIRE, TimeUnit.MINUTES);
    }

    /**
     * 设置字符串键值和过期时间
     *
     * @param key
     * @param value
     * @param expire
     * @param unit
     * @return void
     * @author xxc
     * @time 2019-1-16 16:50
     */
    public void set(String key, Object value, long expire, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, expire, unit);
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return java.lang.Object
     * @author xxc
     * @time 2019-1-16 16:51
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取set长度
     *
     * @param key
     * @return java.lang.Long
     * @author xxc
     * @time 2019-1-16 17:05
     */
    public Long getSetLength(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 设置单个值到list操作
     *
     * @param key
     * @param value
     * @return void
     * @author xxc
     * @time 2019-1-16 16:51
     */
    public void setList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 设置整个list操作
     *
     * @param key
     * @param list
     * @return void
     * @author xxc
     * @time 2019-1-16 16:52
     */
    public void setAllList(String key, List<Object> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * 获取list操作
     *
     * @param key
     * @return java.util.List
     * @author xxc
     * @time 2019-1-16 16:52
     */
    public List getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 删除list元素，从存储在键中的列表中删除等于值的元素的第一个计数事件。
     * count> 0：删除等于从左到右移动的值的第一个元素；
     * count< 0：删除等于从右到左移动的值的第一个元素；
     * count = 0：删除等于value的所有元素。
     *
     * @param key
     * @param count
     * @param value
     * @return java.lang.Long
     * @author xxc
     * @time 2020-1-3 16:22
     */
    public Long deleteList(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 获取list长度
     *
     * @param key
     * @return java.lang.Long
     * @author xxc
     * @time 2019-1-16 17:05
     */
    public Long getListLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 设置单个值到map操作
     *
     * @param key
     * @param hk
     * @param hv
     * @return void
     * @author xxc
     * @time 2019-1-16 16:52
     */
    public void setMap(String key, Object hk, Object hv) {
        redisTemplate.opsForHash().put(key, hk, hv);
    }

    /**
     * 设置整个map操作
     *
     * @param key
     * @param map
     * @return void
     * @author xxc
     * @time 2019-1-16 16:53
     */
    public void setAllMap(String key, Map<Object,Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取map操作
     *
     * @param key
     * @return java.util.Map
     * @author xxc
     * @time 2019-1-16 16:53
     */
    public Map getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取map长度
     *
     * @param key
     * @return java.lang.Long
     * @author xxc
     * @time 2019-1-16 17:06
     */
    public Long getMapLength(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获得过期时间
     *
     * @param key
     * @param unit 时间模式
     * @return java.lang.Long
     * @author liuxingxing
     * @since 2019/7/23 18:41
     **/
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 用scan遍历出含有某个字符串的所有key
     *
     * @param key
     * @return java.util.List<java.lang.String>
     * @author liuixngxing
     * @since 2019/7/23 20:00
     **/
    public Set<String> scan(String key) {
        return scan(key, REDIS_SCAN_COUNT);
    }

    /**
     * 用scan遍历出含有某个字符串的所有key
     *
     * @param key
     * @return java.util.List<java.lang.String>
     * @author liuixngxing
     * @since 2019/7/23 20:00
     **/
    public Set<String> scan(String key, int countNum) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder()
                    .match(key).count(countNum).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });

    }

    /**
     * 清空DB
     *
     * @param node redis 节点
     */
    public void flushDB(RedisClusterNode node) {
        this.redisTemplate.opsForCluster().flushDb(node);
    }


}
