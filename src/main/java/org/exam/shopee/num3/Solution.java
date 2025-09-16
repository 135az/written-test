package org.exam.shopee.num3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yjz
 * @Date 2025/9/16 16:49
 * @Description
 *
 */
public class Solution {
    /**
     * Note: 类名、方法名、参数名已经指定，请勿修改
     * <p>
     * <p>
     * if string is balanced then return true, else return false
     *
     * @param inputStr string字符串  input string
     * @return bool布尔型
     */
    public boolean isStringBalance(String inputStr) {
        if (inputStr.isEmpty()) {
            return true;
        }

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : inputStr.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int f : freq.values()) {
            countMap.put(f, countMap.getOrDefault(f, 0) + 1);
        }
        if (countMap.size() == 1) {
            return true;
        }
        if (countMap.size() > 2) {
            return false;
        }
        List<Integer> keys = new ArrayList<>(countMap.keySet());
        int f1 = keys.get(0), f2 = keys.get(1);

        int c1 = countMap.get(f1), c2 = countMap.get(f2);

        if ((f1 == 1 && c1 == 1) || (f2 == 1 && c2 == 1)) {
            return true;
        }
        if (Math.abs(f1 - f2) == 1) {
            return (f1 > f2 && c1 == 1) || (f2 > f1 && c2 == 1);
        }
        return false;
    }
}
