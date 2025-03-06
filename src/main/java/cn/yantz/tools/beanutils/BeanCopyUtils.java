package cn.yantz.tools.beanutils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bean拷贝工具类
 */
public class BeanCopyUtils {

    /**
     * 对象属性拷贝 - 使用 Apache BeanUtils
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            return;
        }
        try {
            BeanUtils.copyProperties(target, source);
        } catch (Exception e) {
            throw new RuntimeException("Bean copy error", e);
        }
    }

    /**
     * 对象属性拷贝并返回新对象
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param <T>         目标类型
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean copy error", e);
        }
    }

    /**
     * 列表对象拷贝
     *
     * @param sourceList  源列表
     * @param targetClass 目标类
     * @param <T>         目标类型
     * @return 目标列表
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            targetList.add(copy(source, targetClass));
        }
        return targetList;
    }

    /**
     * 对象属性拷贝（不拷贝null值）
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        if (source == null) {
            return;
        }
        try {
            org.apache.commons.beanutils.PropertyUtils.describe(source).entrySet().stream()
                    .filter(entry -> entry.getValue() != null && !"class".equals(entry.getKey()))
                    .forEach(entry -> {
                        try {
                            PropertyUtils.setProperty(target, entry.getKey(), entry.getValue());
                        } catch (Exception ignored) {
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Bean copy error", e);
        }
    }
}
