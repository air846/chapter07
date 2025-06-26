package com.restaurant.common;

/**
 * 基于ThreadLocal的工具类，用于保存和获取当前登录用户的ID
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录用户id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前登录用户id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清除当前线程的用户ID
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }
}