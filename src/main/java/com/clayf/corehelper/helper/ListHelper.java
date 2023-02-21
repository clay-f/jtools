package com.clayf.corehelper.helper;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * list工具类
 * <p>
 * updated by f at 2021-12-16 11:17
 */
public final class ListHelper {
    private ListHelper() {
    }

    /**
     * 对list的元素按照多个属性名称排序,
     * list元素的属性可以是数字（byte、short、int、long、float、double等，支持正数、负数、0）、char、String、java.util.Date
     *
     * @param list
     * @param sortnameArr list元素的属性名称
     * @param isAsc       true升序，false降序
     */
    public static <E> void sort(List<E> list, final boolean isAsc, final String... sortnameArr) {
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (String s : sortnameArr) {
                    ret = ListHelper.compareObject(s, isAsc, a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        });
    }

    /**
     * 给list的每个属性都指定是升序还是降序
     *
     * @param list
     * @param sortnameArr 参数数组
     * @param typeArr     每个属性对应的升降序数组， true升序，false降序
     */
    public static <E> void sort(List<E> list, final String[] sortnameArr, final boolean[] typeArr) {
        if (sortnameArr.length != typeArr.length) {
            throw new RuntimeException("属性数组元素个数和升降序数组元素个数不相等");
        }
        list.sort((a, b) -> {
            int ret = 0;
            try {
                for (int i = 0; i < sortnameArr.length; i++) {
                    ret = ListHelper.compareObject(sortnameArr[i], typeArr[i], a, b);
                    if (0 != ret) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        });
    }

    /**
     * 对2个对象按照指定属性名称进行排序
     *
     * @param sortname 属性名称
     * @param isAsc    true升序，false降序
     * @param a
     * @param b
     * @return
     * @throws Exception
     */
    private static <E> int compareObject(final String sortname, final boolean isAsc, E a, E b) throws Exception {
        int ret;
        Object value1 = ListHelper.forceGetFieldValue(a, sortname);
        Object value2 = ListHelper.forceGetFieldValue(b, sortname);
        String str1 = value1.toString();
        String str2 = value2.toString();
        if (value1 instanceof Number && value2 instanceof Number) {
            int maxlen = Math.max(str1.length(), str2.length());
            str1 = ListHelper.addZero2Str((Number) value1, maxlen);
            str2 = ListHelper.addZero2Str((Number) value2, maxlen);
        } else if (value1 instanceof Date && value2 instanceof Date) {
            long time1 = ((Date) value1).getTime();
            long time2 = ((Date) value2).getTime();
            int maxlen = Long.toString(Math.max(time1, time2)).length();
            str1 = ListHelper.addZero2Str(time1, maxlen);
            str2 = ListHelper.addZero2Str(time2, maxlen);
        }
        if (isAsc) {
            ret = str1.compareTo(str2);
        } else {
            ret = str2.compareTo(str1);
        }
        return ret;
    }

    /**
     * 给数字对象按照指定长度在左侧补0.
     * <p>
     * 使用案例: addZero2Str(11,4) 返回 "0011", addZero2Str(-18,6)返回 "-000018"
     *
     * @param numObj 数字对象
     * @param length 指定的长度
     * @return
     */
    public static String addZero2Str(Number numObj, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        // 设置是否使用分组
        nf.setGroupingUsed(false);
        // 设置最大整数位数
        nf.setMaximumIntegerDigits(length);
        // 设置最小整数位数
        nf.setMinimumIntegerDigits(length);
        return nf.format(numObj);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return
     * @throws Exception
     */
    public static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        Object object = null;
        boolean accessible = field.isAccessible();
        if (!accessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            object = field.get(obj);
            // 还原private,protected属性的访问性质
            field.setAccessible(accessible);
            return object;
        }
        object = field.get(obj);
        return object;
    }

    /**
     * 将 list，转化为分号逗号拼接字符串。
     * <p>
     * [abc, def, abg] -> 'abc','def',abg'
     *
     * @param strs
     * @return String 若strs为null或者大小为0，返回null
     */
    public static String replace(List<String> strs) {
        if (null != strs && strs.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder(strs.get(0).length() * strs.size());
            for (String s : strs) {
                stringBuilder.append("'").append(s).append("',");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
        return null;
    }


    /**
     * list分页。
     *
     * @param list     数据
     * @param pageNum  页号，最低为1
     * @param pageSize 每页内容个数
     * @return list 分页之后的数据，若list为空，或0，返回null
     * @throws IllegalArgumentException pageNum不能小于等于0
     */
    public static List startPage(List list, Integer pageNum, Integer pageSize) {
        if (list == null || list.size() == 0) {
            return null;
        }
        if (pageNum <= 0) {
            throw new IllegalArgumentException("页号最低为1");
        }
        Integer count = list.size(); // 记录总数
        Integer pageCount = (count + pageSize - 1) / pageSize;
        int fromIndex, toIndex;
        if (!Objects.equals(pageNum, pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        return list.subList(fromIndex, toIndex);
    }

    /**
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     *
     * @param sourceList 分页list
     * @param page       分页数     page number should start from 1
     * @param pageSize   分页大小
     * @return sublist   list的子list
     * custom error can be given instead of returning emptyList
     */
    public static <T> List<T> startPage(List<T> sourceList, int page, int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}
