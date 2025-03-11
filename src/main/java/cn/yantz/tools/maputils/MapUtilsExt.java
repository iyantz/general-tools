package cn.yantz.tools.maputils;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map 工具类，目前只支持map 根据key和value排序
 *
 * @author Created by yantz on 2018-10-29
 */
public final class MapUtilsExt extends org.apache.commons.collections.MapUtils {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private MapUtilsExt() {
    }

    /**
     * 根据Map key 排序 升序
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        return sortByKey(map, ASC);
    }

    /**
     * 根据Map value 排序 升序
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map, ASC);
    }

    /**
     * 根据Map key 排序
     *
     * @param map
     * @param sortDirection
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, String sortDirection) {
        return sortMap(map, sortDirection, Map.Entry.<K, V>comparingByKey());
    }

    /**
     * 根据Map value 排序
     *
     * @param map
     * @param sortDirection
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, String sortDirection) {
        return sortMap(map, sortDirection, Map.Entry.<K, V>comparingByValue());
    }

    /**
     * 通用的排序方法
     *
     * @param map           待排序的Map
     * @param sortDirection 排序方向，"asc" 或 "desc"
     * @param comparator    排序比较器
     * @param <K>           键的类型
     * @param <V>           值的类型
     * @return 排序后的Map
     */
    private static <K, V> Map<K, V> sortMap(Map<K, V> map, String sortDirection, java.util.Comparator<Map.Entry<K, V>> comparator) {
        if (StringUtils.isBlank(sortDirection) || ASC.equalsIgnoreCase(sortDirection)) {
            return map.entrySet().stream()
                    .sorted(comparator)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } else if (DESC.equalsIgnoreCase(sortDirection)) {
            return map.entrySet().stream()
                    .sorted(comparator.reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }
        throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
    }
}
