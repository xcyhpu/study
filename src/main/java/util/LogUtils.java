package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志登记工具.
 *
 * @author shitianshu on 15/12/11 上午11:14.
 */
public final class LogUtils {

    /** 日志格式. */
    private static final String LOG_FORMAT = "[{}]#[{}]#{}";

    /**
     * info级别日志登记.
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void info(final Logger LOG, String title, Object... params) {
        if(LOG.isInfoEnabled()) {
            LOG.info(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params));
        }
    }

    /**
     * debug级别日志登记
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void debug(final Logger LOG, String title, Object... params) {
        if(LOG.isDebugEnabled()) {
            LOG.debug(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params));
        }
    }

    /**
     * warn级别日志登记
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void warn(final Logger LOG, String title, Object... params) {
        if(LOG.isWarnEnabled()) {
            LOG.warn(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params));
        }
    }

    /**
     * warn级别日志登记
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param e {@link java.lang.Throwable}对象
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void warn(final Logger LOG, String title, Throwable e, Object... params) {
        if(LOG.isWarnEnabled()) {
            LOG.warn(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params), e);
        }
    }

    /**
     * error级别日志登记
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void error(final Logger LOG, String title, Object... params) {
        if(LOG.isErrorEnabled()) {
            LOG.error(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params));
        }
    }

    /**
     * error级别日志登记
     *
     * @param LOG {@link org.slf4j.Logger}对象
     * @param title 日志登记标题，描述日志意图
     * @param e {@link java.lang.Throwable}对象
     * @param params 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.<BR/>
     *               [param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    public static void error(final Logger LOG, String title, Throwable e, Object... params) {
        if(LOG.isErrorEnabled()) {
            LOG.error(LOG_FORMAT, TraceUtils.getTrace(), title, formatParam(params), e);
        }
    }

    /**
     * 参数格式化.
     *
     * @param params 参数名+参数内容集合.
     * @return 格式化后的参数列表 日志参数名+参数内容,2位一组,第一位是参数名，第二位是参数内容.[param1name,param1value,param2name,param2value.....paramXname,paramXvalue]
     */
    private static StringBuilder formatParam(Object... params) {
        StringBuilder param = new StringBuilder();
        if(null == params || params.length <= 0) {
            return param;
        }
        for(int index = 0;index < params.length; index += 2) {
            Object paramName = "";
            Object paramValue = "";
            try {
                paramName = params[index];
                paramValue = params[index+1];
            } catch(IndexOutOfBoundsException e) {
                /** 数组越界什么都不做，继续打印. */
                error(LOG,"日志参数格式化时数组越界.", e);
            }
            param.append(paramName).append(":[").append(paramValue).append("].");
        }
        return param;
    }

    /** 日志组件. */
    private static final Logger LOG = LoggerFactory.getLogger(LogUtils.class);

}
