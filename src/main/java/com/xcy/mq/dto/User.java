package com.xcy.mq.dto;

import com.huitongjy.common.util.JsonUtil;

/**
 * @author xuchunyang
 * @date 2021-11-26
 */
public class User {

    private int random;

    private String name;

    public User() {
    }

    public User(int random, String name) {
        this.random = random;
        this.name = name;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSON(this);
    }
}
