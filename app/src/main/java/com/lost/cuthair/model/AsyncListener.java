package com.lost.cuthair.model;

/**
 * 任务完成后调用的接口
 * Created by lost on 2016/4/17.
 */
public interface AsyncListener {
    void  done(Object... objects);
}
