package com.pro.common.modules.service.dependencies.util;

import cn.hutool.core.util.RandomUtil;
import com.pro.framework.api.util.AssertUtil;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathUtils {
    public static final SecureRandom random = new SecureRandom();

    /**
     * 取随机值
     *
     * @param fromInclude 例如 1
     * @param toInclude   例如 10  表示1-10任取一个数
     */
    public static Integer random(int fromInclude, int toInclude) {
        //[0,toInclude)  +  fromInclude
        return random.nextInt(toInclude) + fromInclude;
    }

    /**
     * 取随机值
     *
     * @param fromInclude 例如 1
     * @param toInclude   例如 10  表示1-10任取一个数
     */
    public static Integer randomOld(int fromInclude, int toInclude) {
        //[0,toInclude)  +  fromInclude
        return new Random().nextInt(toInclude - fromInclude + 1) + fromInclude;
    }

    /**
     * 组合计注数
     * 例如 所有选项[
     * [1,2,3,4,5,6],      (在其中选几个)
     * [11,22,33,44,55,66] (在其中选几个)
     * ]
     *
     * @param listlist  例如 {第一行选中:1,2,第二行选中:33,44}   [[1,2],[33,44]]
     * @param canRepeat 计数时号码重复的组合是否重复计数  例如 选中 {第一行选中:3,第二行选中:33}(同为第三个数)是否为一注
     * @return 注数
     */
    public static Integer countSelect(List<List<Integer>> listlist, boolean canRepeat) {
        int rs = 1;
        if (canRepeat || listlist.size() == 1) {
            for (List<Integer> selectList : listlist) {
                rs *= selectList.size();
            }
            return rs;
        } else {
            return countSelectRecursion(listlist, new HashSet<>());
        }
    }

    /**
     * 计算 组合数(排除掉部分元素后,组合的情况总数)
     *
     * @param subSelectListList 二维数组
     * @param removeSet         要排除元素的集合
     */
    private static Integer countSelectRecursion(List<List<Integer>> subSelectListList, Set<Integer> removeSet) {
        Integer sum = 0;
        List<List<Integer>> subListList = subSelectListList.subList(1, subSelectListList.size());
        List<Integer> firstList = subSelectListList.get(0);
        //第一个数组,与要排除的集合  的差集
        List<Integer> firstMinusList = removeSet(firstList, removeSet);
        if (subSelectListList.size() == 1) {
            return firstMinusList.size();
        } else {
            for (Integer value : firstMinusList) {
                HashSet<Integer> subRemoveSet = new HashSet<>(removeSet);
                subRemoveSet.add(value);
                sum += countSelectRecursion(subListList, subRemoveSet);
            }
        }
        return sum;
    }

    /**
     * list 排除掉 removeList中的每一个
     */
    private static List<Integer> removeSet(List<Integer> list, Set<Integer> removeList) {
        return list.stream().filter(o -> !removeList.contains(o)).collect(Collectors.toList());
    }

    /**
     * 取最接近的一个数
     */
    public static BigDecimal closest(BigDecimal num, Set<BigDecimal> froms) {
        Map<BigDecimal, BigDecimal> diff_num_map = new HashMap<>(256);
        for (BigDecimal from : froms) {
            diff_num_map.put(from.subtract(num).abs(), from);
        }
        BigDecimal diffMin = diff_num_map.keySet().stream().min(BigDecimal::compareTo).orElse(null);
        return diff_num_map.get(diffMin);
    }

    /**
     * BigDecimal.equals当精度不一致时不相等    例如   new BigDecimal(100.00).equals(new BigDecimal(100))  == false
     */
    public static boolean equals(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }

    /**
     * 随机返回true/false
     *
     * @param rate 例如 0.67
     * @return 67%的几率返回true
     */
    public static boolean randomBool(double rate) {
        return RandomUtil.randomDouble(1) < rate;
    }


    public static String appendZore(Integer val, int length) {
//        AssertUtil.isTrue(length > 1, "前面拼接0,至少要保留2位");
        int temp = val;
        int 几位数 = 0;
        do {
            temp = temp / 10;
            几位数++;
        } while (temp > 0);
        return IntStream.range(0, length - 几位数).mapToObj(i -> "0").collect(Collectors.joining("")) + val;
    }

    public static <T> BigDecimal sumBigDecimal(List<T> entities, Function<T, BigDecimal> numFun) {
        return sum(entities, numFun, BigDecimal.ZERO);
    }

    public static <T> Integer sumInteger(List<T> entities, Function<T, Integer> numFun) {
        return sum(entities, numFun, 0);
    }

    public static <T, NUM extends Number> NUM sum(List<T> entities, Function<T, NUM> numFun, NUM zero) {
        if (zero instanceof BigDecimal) {
            return entities.stream().map(numFun).filter(Objects::nonNull).reduce((l1, l2) -> (NUM) ((BigDecimal) (l1)).add((BigDecimal) l2)).orElse((NUM) BigDecimal.ZERO);
        }
        Double sum = entities.stream().map(numFun).filter(Objects::nonNull).mapToDouble(Number::doubleValue).reduce(Double::sum).orElse(0d);
        if (zero instanceof Integer) {
            return (NUM) new Integer(sum.intValue());
        }
        if (zero instanceof Long) {
            return (NUM) new Long(sum.longValue());
        }
        if (zero instanceof Float) {
            return (NUM) new Float(sum.floatValue());
        }
        if (zero instanceof Double) {
            return (NUM) new Double(sum.doubleValue());
        }
        throw new RuntimeException("unknown type of values");
    }

}
