package com.xcy.mq;

import com.huitongjy.base.rocketmq.consumer.AbstractJsonMessageListener;
import com.huitongjy.base.rocketmq.consumer.annotation.RocketMQMessageTagListener;
import com.huitongjy.common.util.LogUtils;
import com.xcy.mq.dto.User;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xuchunyang
 * @date 2021-11-26
 */
@RocketMQMessageTagListener(topic = "${rocketmq.consumer.local.topic}", tag = "${rocketmq.consumer.local.tag}", maxReconsumeTimes = 3)
public class TestJsonListener extends AbstractJsonMessageListener<User> {

    private static final Logger LOG = LoggerFactory.getLogger(TestJsonListener.class);

    @Override
    protected boolean logMessage() {
        return true;
    }

    @Override
    protected void handle(User user) throws Exception {

        if (user.getRandom() == 0) {
            throw new Exception("No need to retry Exception");
        } else if (user.getRandom() == 1) {
            throw new MyRetryException("需要重试的异常");
        }

        LogUtils.info(LOG, "正常消费完成");
    }

    @Override
    protected boolean needRetry(Throwable e) {
        return e instanceof MyRetryException;
    }

    @Override
    public void consumeFailed(MessageExt messageExt) throws Exception {


    }

    class MyRetryException extends Exception {

        public MyRetryException() {
        }

        public MyRetryException(String message) {
            super(message);
        }

        public MyRetryException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyRetryException(Throwable cause) {
            super(cause);
        }

        public MyRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
