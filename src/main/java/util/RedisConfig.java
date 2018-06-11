package util;

/**
 * Created by xuchunyang on 2018/6/11 16点42分
 */
public class RedisConfig {


    public static final int redisPoolMaxActive=256;

    public static final int redisPoolMaxIdle=256;

    public static final int redisPoolMaxWait=1000;

    public static final boolean redisPoolTestOnBorrow=false;

    public static final boolean redisPoolTestOnReturn=false;

    public static final boolean redisPoolTestWhileIdle=true;

    public static final int redisPoolNumTestsPerEvictionRun = -1;

    public static final int redisPoolTimeBetweenEvictionRunsMillis = 30000;

    public static final int redisPoolMinEvictableIdleTimeMillis = 2*360000;

    public static final int redisPoolSoftMinEvictableIdleTimeMillis =  360000;


}
