package com.datuanyuan.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 * 
 * @author weiyuan
 *
 */
public class CollectionHelp {

    /**
     * map根据value进行降序排序
     * 
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValueDesc(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * map根据value值进行 升序排序
     * 
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValueAsc(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }
    
    public static boolean isMapEmpty(Map<?,?> m) {
        return m == null || m.size() == 0;
    }
    
    public static boolean isMapNotEmpty(Map<?, ?> m) {
        return m != null && m.size() > 0;
    }
    
    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    
    public static boolean isArrayNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }
    
    public static void main (String[] args) {
        
    }
}
