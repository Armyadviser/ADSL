package com.ge.util.string;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Storm_Falcon on 2015/10/10.
 * Some method do help to convert string.
 */
public class StringHelper {

    /**
     * 截取字符串
     *
     * @param source 源字符串
     * @param sBegin 开头
     * @param sEnd   结尾
     * @param count  第几次匹配
     * @return 第count次匹配，sBegin和sEnd中间的内容
     */
    public static String getContent(String source, String sBegin, String sEnd, int count) {
        //截取开头和结尾坐标
        int nBeginIndex = 0, nEndIndex;

        for (int i = 0; i < count; i++) {
            nBeginIndex = source.indexOf(sBegin, nBeginIndex + 1);
        }

        nBeginIndex += sBegin.length();

        nEndIndex = source.indexOf(sEnd, nBeginIndex + 1);

        try {
            return source.substring(nBeginIndex, nEndIndex);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 去掉所有空格
     */
    public static String trim(String source) {
        return source.replaceAll(" ", "");
    }

    /**
     * 将连续多个空格变成1个空格
     */
    public static String cutSpace(String source) {
        return source.replaceAll("  ", " ");
    }

    /**
     * 生成随机串，由数字和字母组合
     *
     * @param length 长度
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int charType = random.nextInt(2);

            //判断生成的数字类型
            switch (charType) {
                case 0://字母
                    //大小写
                    int caseType = random.nextInt(2);
                    int ch = random.nextInt(26);
                    if (caseType == 0) {//大写
                        ch = ch + 65;
                    } else {//小写
                        ch = ch + 97;
                    }
                    sb.append((char) ch);
                    break;
                case 1://数字
                    int num = random.nextInt(10);
                    sb.append(num);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取指定位置后一个字符
     */
    public static char getNextChar(String source, int index) {
        if (index >= source.length()) {
            return '\0';
        }
        return source.charAt(index + 1);
    }

    /**
     * 获取指定位置前一个字符
     */
    public static char getPrevChar(String source, int index) {
        if (index <= 1) {
            return '\0';
        }
        return source.charAt(index - 1);
    }

    public static String[] split(String source, String delim) {
        List<String> list = new ArrayList<>();
        int nIndex = source.indexOf(delim);
        while (nIndex != -1) {
            list.add(source.substring(0, nIndex));
            source = source.substring(nIndex + 1);
            nIndex = source.indexOf(delim);
        }
        list.add(source);

        String[] res = new String[list.size()];
        list.toArray(res);
        return res;
    }

    /**
     * Parse a string to a key-value pair.
     *
     * @param string abc=egf
     * @return Entry&#60;String&#44;&nbsp;String&#62;(abc, efg)
     */
    public static AbstractMap.SimpleEntry<String, String> parseKeyValue(String string) {
        if (string == null || string.length() == 0) {
            return new AbstractMap.SimpleEntry<>("", "");
        }

        int index = string.indexOf('=');
        if (index == -1) {
            return new AbstractMap.SimpleEntry<>("", string);
        }

        String key = string.substring(0, index);
        String value = string.substring(index + 1, string.length());
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    private StringHelper() {
    }

    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String replaceAll(String src, String old, String replacement) {
        while (src.contains(old)) {
            src = src.replace(old, replacement);
        }
        return src;
    }

    /**
     * 将MAC地址格式化成简单12位大写字符串
     */
    public static String macToSimple(String strMac) {
        if (strMac == null) {
            return null;
        }
        if (strMac.length() == 12) {
            return strMac.toUpperCase();
        }
        strMac = replaceAll(strMac, ":", "");
        strMac = replaceAll(strMac, "-", "");
        return strMac;
    }
}
