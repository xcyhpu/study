package com.xcy.redis;

import com.alibaba.fastjson.JSON;
import com.huitongjy.common.cached.config.Config;
import com.huitongjy.common.cached.config.ConfigManager;
import com.huitongjy.common.cached.config.RedisConfig;
import com.huitongjy.common.util.LogUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by xiayun on 16/3/26.
 */
public class JedisUtil {

    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger(JedisUtil.class);

    private static Map<String, JedisUtil> uniqueInstance = new HashMap<String, JedisUtil>();

    private JedisPool jedisPool;

    private static JedisUtil jedisUtil = null;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public static JedisUtil getInstance(String serverName) {
        jedisUtil = uniqueInstance.get(serverName);
        if (jedisUtil == null) {
            synchronized (JedisUtil.class){
                jedisUtil = uniqueInstance.get(serverName);
                if(jedisUtil == null){
                    jedisUtil = new JedisUtil(serverName);
                    uniqueInstance.put(serverName, jedisUtil);
                }
            }

        }
        return jedisUtil;
    }

    public static JedisUtil getReadJedisUtil(){
        return JedisUtil.getInstance("opt");
    }

    public static JedisUtil getWriteJedisUtil(){
        return JedisUtil.getInstance("data");
    }


    public JedisUtil(String serverName) {

        //设置主从Redis的端口和密码
        if(StringUtils.isBlank(serverName)){
            throw new RuntimeException("请指定Redis主或从库!");
        }

        //设置主从Redis的IP
        Config config = ConfigManager.getConfig("redis.properties");
        String masterIp = config.getValue("master_ip");
        if(StringUtils.isBlank(masterIp)){
            throw new RuntimeException("redis.properties下的 master_ip 参数为空，请指定!");
        }
        String masterPort = config.getValue("master_port");
        if(StringUtils.isBlank(masterPort)){
            throw new RuntimeException("redis.properties下的 master_port 参数为空，请指定!");
        }
        String masterPasswd = config.getValue("master_passwd");
        if(StringUtils.isBlank(masterPasswd)){
            throw new RuntimeException("redis.properties下的 master_passwd 参数为空，请指定!");
        }

        String slaveIp = config.getValue("slave_ip");
        if(StringUtils.isBlank(slaveIp)){
            throw new RuntimeException("redis.properties下的 slave_ip 参数为空，请指定!");
        }
        String slavePort = config.getValue("slave_port");
        if(StringUtils.isBlank(slavePort)){
            throw new RuntimeException("redis.properties下的 slave_port 参数为空，请指定!");
        }
        String slavePasswd = config.getValue("slave_passwd");
        if(StringUtils.isBlank(slavePasswd)){
            throw new RuntimeException("redis.properties下的 slave_passwd 参数为空，请指定!");
        }

        String redisPoolMaxActive = config.getValue("redis_pool_max_active");
        if("opt".equals(serverName)){
            LogUtils.info(LOG,"jedis init","slaveIp",slaveIp,"slavePort",slavePort,"slavePasswd",slavePasswd);
            initialPool(slaveIp, Integer.parseInt(slavePort), slavePasswd,redisPoolMaxActive);
        }else{
            LogUtils.info(LOG,"jedis init","masterIp",masterIp,"masterPort",masterPort,"masterPasswd",masterPasswd);
            initialPool(masterIp, Integer.parseInt(masterPort), masterPasswd,redisPoolMaxActive);
        }
    }

    public static void main(String[] args) {
        Pattern regex = Pattern.compile(".*((test[a-z]{1})\\.)?huitong.com.*$");
        System.out.println(regex.matcher("testa.huitong.com").matches());
    }

    private void initialPool(String sIp, int sPort, String sPassword,String redisPoolMaxActive) {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(RedisConfig.redisPoolMaxActive);
        config.setMaxIdle(RedisConfig.redisPoolMaxIdle);
        if(StringUtils.isNotBlank(redisPoolMaxActive)){
            Integer redisPoolMaxActiveInt = Integer.parseInt(redisPoolMaxActive);
            if(redisPoolMaxActiveInt > 512){
                redisPoolMaxActiveInt = 512;
            }
            //maxIdle和maxTotal保持一致
            config.setMaxTotal(redisPoolMaxActiveInt);
            config.setMaxIdle(redisPoolMaxActiveInt);
        }
        config.setMaxWaitMillis(RedisConfig.redisPoolMaxWait);

        config.setTestOnBorrow(RedisConfig.redisPoolTestOnBorrow);
        config.setTestOnReturn(RedisConfig.redisPoolTestOnReturn);
        config.setTestWhileIdle(RedisConfig.redisPoolTestWhileIdle);
        config.setNumTestsPerEvictionRun(RedisConfig.redisPoolNumTestsPerEvictionRun);
        config.setTimeBetweenEvictionRunsMillis(RedisConfig.redisPoolTimeBetweenEvictionRunsMillis);
        config.setMinEvictableIdleTimeMillis(RedisConfig.redisPoolMinEvictableIdleTimeMillis);
        config.setSoftMinEvictableIdleTimeMillis(RedisConfig.redisPoolSoftMinEvictableIdleTimeMillis);

        jedisPool = new JedisPool(config, sIp, sPort, 2000,sPassword);

        LogUtils.info(LOG, "jedisPool init finish", "jedisPool", jedisPool);
    }


    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds, int index) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.expire(key, seconds);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis expire error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 功能：获取key总记录数
     */
    public Long getDataCount(String sKey) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.llen(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getDataCount error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能：获取key总记录数
     */
    public Long getDataCount(String sKey, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.llen(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getDataCount error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能：批量获取redis数据
     */
    public List<List<String>> getRedisList(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> lstValue = jedis.lrange(key, start, end);
            List<List<String>> lstRestult = new ArrayList<List<String>>();
            for (int i = 0; i < lstValue.size(); i++) {
                String sValue = lstValue.get(i);
                List<String> lstValues = JSON.parseArray(sValue, String.class);
                lstRestult.add(lstValues);
            }
            return lstRestult;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getRedisList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getHeadOfList(String key, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.lpop(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getHeadOfList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getTailOfList(String key, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.rpop(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getTailOfList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> getRedisList(String key, int count, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            List<String> lstValue = new ArrayList<String>();
            for (int i = 0; i < count; i++) {
                String v = jedis.lpop(key);
                if (StringUtils.isNotEmpty(v)) {
                    lstValue.add(v);
                }

            }
            return lstValue;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getRedisList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> getRedisFootList(String key, int count, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            List<String> lstValue = new ArrayList<String>();
            for (int i = 0; i < count; i++) {
                String v = jedis.rpop(key);
                if (StringUtils.isNotEmpty(v)) {
                    lstValue.add(v);
                }

            }
            return lstValue;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getRedisFootList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean lpush(String sKey, String sValue, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.lpush(sKey, sValue);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis lpush error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }

    public String lpop(String sKey, int index) {
        Jedis jedis = null;
        String ret = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            ret = jedis.lpop(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis lpop error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return ret;
    }

    public Boolean rpush(String sKey, String sValue, int index) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.rpush(sKey, sValue);
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis rpush error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String rpop(String sKey, int index) {
        Jedis jedis = null;
        String ret = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            ret = jedis.rpop(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis rpop error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return ret;
    }

    /**
     * 功能：批量插入redis数据
     */
    public Boolean insertDataList(String sKey, List<List<String>> lstDate) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> lstValue = new ArrayList<String>();
            for (int i = 0; i < lstDate.size(); i++) {
                lstValue = lstDate.get(i);
                String sValue = JSON.toJSONString(lstValue);
                jedis.lpush(sKey, sValue);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis insertDataList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能： 批量Redis队列追加实现
     */
    public Boolean addBatchQueue(String key, List<String> listObject, int index, Integer expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < listObject.size(); i++) {
                p.rpush(key, listObject.get(i) + "");
            }
            p.sync();
            if (expire != null && expire > 0) {
                jedis.expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis addBatchQueue error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能： 批量Redis队列追加实现
     */
    public Boolean addBatchHeadQueue(String key, List<String> listObject, int index, Integer expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < listObject.size(); i++) {
                p.lpush(key, listObject.get(i) + "");
            }
            p.sync();
            if (expire != null && expire > 0) {
                jedis.expire(key, expire);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis addBatchHeadQueue error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能：批量删除redis数据
     */
    public Boolean deleteDateList(String sKey, List<List<String>> lstDate) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (lstDate != null && lstDate.size() > 0) {
                List<String> lstValue = null;
                for (int i = 0; i < lstDate.size(); i++) {
                    lstValue = lstDate.get(i);
                    String sValue = JSON.toJSONString(lstValue);
                    jedis.lrem(sKey, 1, sValue);
                }
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis deleteDateList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    public Boolean set(String sKey, String sValue) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(sKey, sValue);
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis set error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean set(String sKey, String sValue, int index) {
        Jedis jedis = null;

        try {
            LogUtils.debug(LOG, "watch jedisPool", "jedisPool", jedisPool);
            jedis = jedisPool.getResource();
            LogUtils.debug(LOG, "fetch jedis", "jedis", jedis);
            jedis.select(index);
            jedis.set(sKey, sValue);
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis set error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean set(String sKey, String sValue,int index, int expiredSeconds) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.setex(sKey, expiredSeconds, sValue);
            return true;
        } catch (Exception e) {
            LOG.error("redis set error.", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public Boolean sadd(String sKey, String sValue, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            jedis.sadd(sKey, sValue);
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis sadd error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String srandmember(String sKey, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.srandmember(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis srandmember error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String sKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.get(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis get error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String sKey, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.get(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis get error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(String sKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.del(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis del error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(String... sKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.del(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis del error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> key(String sKey, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.keys(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis key error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(String sKey, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.del(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis del error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long del(int index, String... sKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.del(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis del error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * list分页
     *
     * @param key
     * @param start
     * @param end
     * @param index
     * @return
     */
    public List<String> getRedisList(String key, int start, int end, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            List<String> lstValue = jedis.lrange(key, start, end);
            return lstValue;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis getRedisList error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 功能：判断该key是否存在
     */
    public Boolean isKeyExists(String key, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.exists(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis isKeyExists error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 批量设置字符串
     * @param key_val
     * @param db_index
     * @return
     */
    public Boolean setBatchValue(Map<String,String> key_val,int db_index){
        if(MapUtils.isEmpty(key_val)){
            return false;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            Pipeline pipeline =  jedis.pipelined();
            for (Map.Entry<String,String> entry : key_val.entrySet()){
                String key = entry.getKey();
                String val = entry.getValue();
                pipeline.set(key,val);
            }
            pipeline.sync();
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis setBatchValue error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 批量设置过期时间
     * @param keys
     * @param expire
     * @param db_index
     * @return
     */
    public Boolean batchExpireKey(Set<String> keys,Integer expire,Integer db_index){
        if(CollectionUtils.isEmpty(keys)
                || expire == null
                || expire < 0){
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            Pipeline pipeline =  jedis.pipelined();
            keys.forEach(key->{
                pipeline.expire(key,expire);
            });
            pipeline.sync();
            return true;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis batchExpireKey error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向hash中插入单条
     * @param key
     * @param field
     * @param value
     * @param db_index
     * @return
     */
    public Boolean hset(String key, String field, String value, int db_index){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hset(key,field,value) > 0;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hset error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向hash中插入单条过期
     * @param key
     * @param field
     * @param value
     * @param db_index
     * @return
     */
    public Boolean hset(String key, String field, String value, int db_index, Integer expireTime){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            Transaction transaction = jedis.multi();
            transaction.hset(key,field,value);
            transaction.expire(key,expireTime);
            transaction.exec();
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> hkeys(String sKey, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(index);
            return jedis.hkeys(sKey);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis key error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 当且仅当field不存在时,插入数据
     * @param key
     * @param field
     * @param value
     * @param db_index
     * @return
     */
    public Boolean hsetnx(String key, String field, String value, int db_index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hsetnx(key,field,value) > 0;
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hsetnx error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向hash中批量插入多条
     * @param key
     * @param hash
     * @param db_index
     * @return
     */
    public Boolean hmset(String key, Map<String, String> hash, Integer expire, int db_index){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            String result = jedis.hmset(key, hash);
            if (expire != null && expire > 0){
                jedis.expire(key, expire);
            }
            return result.equals("OK");
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hmset error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从hash中取出单条
     * @param key
     * @param field
     * @param db_index
     * @return
     */
    public String hget(String key, String field, int db_index){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hget(key, field);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hget error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从hash中取出多条
     * @param key
     * @param db_index
     * @param fields
     * @return
     */
    public List<String> hmget(String key, int db_index, String... fields){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hmget error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从hash中取出全部
     * @param key
     * @param db_index
     * @return
     */
    public Map<String, String> hgetAll(String key, int db_index){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hgetAll(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hgetall error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从hash中移除指定的field
     * @param db_index
     * @param key
     * @param field
     * @return
     */
    public Long hdel(int db_index, String key, String... field){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hdel(key, field);
        } catch (Exception e){
            LogUtils.error(LOG, "redis hdel error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * hash中的条数
     * @param key
     * @param db_index
     * @return
     */
    public Long hlen(String key, int db_index){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.hlen(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis hlen error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long sadd(int db_index, String key, String... members){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.sadd(key, members);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis sadd error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> smembers(int db_index, String key){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.smembers(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis smembers error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean sismember(int db_index, String key, String member){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.sismember(key, member);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis sismember error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long srem(int db_index, String key, String... members){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.srem(key, members);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis srem error. ", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long zadd(int db_index, String key, String member, Double score){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zadd error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long zadd(int db_index, String key, Map<String, Double> scoreMembers){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zadd error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取有序集合的成员总数
     *
     * @param db_index
     * @param key
     * @return
     */
    public Long zcard(int db_index, String key){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zcard(key);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zcard error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     * @param db_index
     * @param key
     * @param userId
     * @return
     */
    public Long zrevrank(int db_index, String key, String userId) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zrevrank(key, userId);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zcard error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param db_index
     * @param key
     * @param max
     * @param min
     */
    public Set<String> zrevrangeByScore(int db_index, String key, Double max, Double min) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zrevrange by score error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从小到大)排序
     * @param db_index
     * @param key
     * @param member
     * @return
     */
    public Long zrank(int db_index, String key, String member) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zrank(key, member);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zcard error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回有序集 key 中，指定区间内的成员，成员的位置按 score 值递增(从小到大)来排序
     * @param db_index
     * @param key
     * @param start
     * @param end
     */
    public Set<String> zrange(int db_index, String key, long start, long end) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zrange  error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     * @param db_index
     * @param key
     * @param members
     * @return
     */
    public Long zrem(int db_index, String key, String... members) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zrem(key, members);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zrem error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    /**
     * 返回有序集中，成员的分数值
     * @param db_index
     * @param key
     * @param userId
     */
    public Double zscore(int db_index, String key, String userId) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(db_index);
            return jedis.zscore(key, userId);
        } catch (Exception e) {
            LogUtils.error(LOG, "redis zrevrange by score error", e);
            throw e;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 设置NX
     * @param key
     * @param value
     * @param expireTime
     * @return
     * @throws Exception
     */
    public Long setnx(String key, String value, int expireTime,int dbIndex) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            Long ret = jedis.setnx(key, value);
            if (ret == 1 && expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            return ret;
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis setnx error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 设置ex
     * @param key
     * @param value
     * @param expireTime
     * @return
     * @throws Exception
     */
    public String setex(String key, String value, int expireTime,int dbIndex) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            String ret = jedis.setex(key, expireTime, value);
            return ret;
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis setnx error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    /**
     * 获取nx
     * @param key
     * @return
     * @throws Exception
     */
    public String getnx(String key,int dbIndex) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            return jedis.get(key);
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis getnx error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 开启事物
     * @param dbIndex
     * @return
     */
    public Transaction multi(int dbIndex){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            return jedis.multi();
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis multi error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 执行命令
     * @param ts
     * @param dbIndex
     * @return
     */
    public List<Object> exec(Transaction ts, int dbIndex){
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.select(dbIndex);
            return ts.exec();
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis exec error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取redis的服务信息
     *
     * @return
     */
    public String info() {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.info();
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis info error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取redis的服务指定信息
     *
     * @param section
     * @return
     */
    public String info(String section) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.info(section);
        }
        catch (Exception e) {
            LogUtils.error(LOG, "redis info pointed section error. ", e);
            throw e;
        }
        finally {
            if (jedis != null) {
                jedis.close();;
            }
        }
    }
}

