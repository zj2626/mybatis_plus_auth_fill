package com.mybatisplus.parent.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象拷贝转换工具类
 */
public class TransformUtils {
    /**
     * 属性复制 (注意: 泛型类型的属性是不能复制的)
     *
     * @param sourse
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copy(Object sourse, Class<T> targetClass) {
        if (null == sourse) {
            return null;
        }
        T targetObj;
        try {
            targetObj = targetClass.newInstance();
            BeanUtils.copyProperties(sourse, targetObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetObj;
    }

    /**
     * list复制 (注意: 泛型类型的属性是不能复制的)
     *
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <T> List<T> listCopy(Collection<?> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObj = targetClass.newInstance();
                BeanUtils.copyProperties(source, targetObj);
                targetList.add(targetObj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetList;
    }

}
