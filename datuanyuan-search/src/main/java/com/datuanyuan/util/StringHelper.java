package com.datuanyuan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author weiyuan
 * @version 1.0
 */
public class StringHelper {

    public static String replace(String text) {
        return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * js 替换'"‘’“”
     *
     * @param text
     * @return
     */
    public static String replaceAll(String text) {
        return text.replaceAll("\'", "&apos;").replaceAll("\"", "&quot;");
    }

    /**
     * <pre>
     * 获取号码的和值
     * </pre>
     *
     * @param codes
     * @return
     */
    public static int getSumCode(String[] codes) {
        int sum = 0;
        for (String string : codes) {
            sum = sum + Integer.parseInt(string);
        }
        return sum;
    }

    public static String copyStr(String str, int num) throws Exception {
        StringBuffer strBuf = new StringBuffer();
        try {
            for (int i = 0; i < num; i++) {
                strBuf.append(str);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return strBuf.toString();
    }

    /**
     * n个里面选r个的组合数
     *
     * @param n 选的个数
     * @param r
     * @return 注数
     */
    public static int getCnr(int n, int r) {
        int sum = 1;
        for (int i = 0; i < r; i++) {
            sum = sum * (n - i) / (i + 1);
        }
        return sum;
    }

    /**
     * @param str 源字符串
     * @param reg 正则表达式
     * @return int 字符串中包含的reg个数
     */
    public static int count(String str, String reg) {
        int count = 0;
        Pattern expression = Pattern.compile(reg); // 使用表达式过滤字符串string1
        Matcher matcher = expression.matcher(str);
        while (matcher.find())
            count++;
        return count;
    }

    /**
     * Get the content between beginFlag and endFlag.
     *
     * @param source      The source string which will be executed replacing.
     * @param beginFlag   The begin flag string.
     * @param endFlag     The end flag string.
     * @param includeFlag if the return string include the flag.
     * @return the content between beginFlag and endFlag.
     */
    public static String getContentByTag(String source, String beginFlag, String endFlag, boolean includeFlag) {
        if (null == source || null == beginFlag || null == endFlag) {
            throw new IllegalArgumentException("Any of (source, beginFlag, endFlag) can not be null");
        }

        int beginIndex = source.indexOf(beginFlag);
        int endIndex = source.indexOf(endFlag);

        if (-1 == beginIndex) {
            beginIndex = 0;
        } else {
            beginIndex += beginFlag.length();
        }
        if (-1 == endIndex) {
            endIndex = 0;
        }

        String innerText = source.substring(beginIndex, endIndex);
        if (includeFlag) {
            return beginFlag + innerText + endFlag;
        }
        return innerText;
    }

    /**
     * <pre>
     * 根据指定的格式的字符串创建将日期对象
     * </pre>
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期对象
     * @throws ParseException 格式错误
     */
    public static Date getDateFromString(String dateStr, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double getDouble(String str) {
        double dbNumber = 0;
        try {
            dbNumber = Double.parseDouble(str.trim());
        } catch (Exception e) {
        }
        return dbNumber;
    }

    /**
     * 获取系统换行符
     *
     * @return 系统换行符
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static long getLong(String str) {
        long lnNumber = 0;
        try {
            lnNumber = Long.parseLong(str);
        } catch (Exception e) {
        }
        return lnNumber;
    }

    public static String getRandomInt(int maxValue) {
        Random random = new Random();
        String strValue = String.valueOf(maxValue);
        String ret = "00000000000" + random.nextInt(maxValue);
        return ret.substring(ret.length() - strValue.length() + 1);
    }

    /**
     * <pre>
     * 按位包号得号码拆分----如：12,12,23拆分6注单式号码
     * </pre>
     *
     * @param anteCode
     * @return
     */
    public static String[] splitCode(String anteCode) {
        int anteCodeNum = 1;
        String[] splitCodes = null;
        String[] codes = anteCode.split(",");
        for (int i = 0; i < codes.length; i++) {
            anteCodeNum = anteCodeNum * codes[i].length();
        }
        splitCodes = new String[anteCodeNum];
        for (int i = 0; i < anteCodeNum; i++) {
            splitCodes[i] = "";
            int m = i, n = 0;
            for (int j = 0; j < codes.length; j++) {
                n = m % codes[j].length();
                m = m / codes[j].length();
                if (j == 0) {
                    splitCodes[i] = splitCodes[i] + codes[j].charAt(n);
                } else {
                    splitCodes[i] = splitCodes[i] + "," + codes[j].charAt(n);
                }
            }
        }
        return splitCodes;
    }

    /**
     * <pre>
     * 根据指定的格式将日期格式化为字符串
     * </pre>
     *
     * @param date    日期对象
     * @param pattern 格式
     * @return 日期字符串
     * @throws ParseException 格式错误
     */
    public static String getStringFromDate(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断字符串是否为空字符串或Null
     *
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否不为空字符串且不为Null
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * <pre>
     * 号码排序,即2,3,1排序成1,2,3
     * </pre>
     *
     * @param code
     * @return
     */
    public static String sortCode(String code) {
        String[] codes = code.split(",");
        Arrays.sort(codes);
        StringBuffer antecode = new StringBuffer();
        for (String string : codes) {
            antecode.append(string.trim() + ",");
        }
        antecode.deleteCharAt(antecode.toString().length() - 1);
        return antecode.toString();
    }

    /**
     * <pre>
     * 把数组组合成字符串
     * </pre>
     *
     * @param <T>
     * @param t         要组合的数组
     * @param separator 分隔符
     * @param prefix    字符串前缀
     * @param suffix    字符串后缀
     * @return 组成的新字符串
     */
    public static <T> String joinStr(T[] t, String separator, String prefix, String suffix) {
        StringBuilder __sb = new StringBuilder(prefix);
        for (int i = 0; i < t.length; i++) {
            if (i == 0)
                __sb.append(t[i]);
            else
                __sb.append(separator).append(t[i]);
        }
        __sb.append(suffix);
        return __sb.toString();
    }

    /**
     * Replaced the string between beginFlag and endFlag with 'content'.
     *
     * @param source    The source string which will be executed replacing.
     * @param beginFlag The begin flag string.
     * @param endFlag   The end flag string.
     * @param content   The content which will replace the original string.
     * @return a string which has been replaced.
     */
    public static String replace(String source, String beginFlag, String endFlag, String content) {
        if (null == content) {
            throw new IllegalArgumentException("content can not be null");
        }

        if (null == source || "".equals(source)) {
            throw new IllegalArgumentException("Argument 'source' must not be null or \"\"");
        }
        if (null == beginFlag || null == endFlag) {
            throw new IllegalArgumentException("Argument 'beginFlag','endFlag' must not be null");
        }

        String innerText = getContentByTag(source, beginFlag, endFlag, false);
        StringBuffer replaced = new StringBuffer(beginFlag).append(innerText).append(endFlag);
        StringBuffer replacing = new StringBuffer(beginFlag).append(content).append(endFlag);
        return source.replaceAll(replaced.toString(), replacing.toString());
    }

    public static String[] splitString(String srcString, String separator) {
        if (isEmpty(srcString)) {
            return null;
        }
        String[] dstString = srcString.split(separator);
        return dstString;
    }



    public static String toChinese(String strvalue) {
        try {
            if (strvalue == null) {
                return "";
            } else {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
                return strvalue;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <pre>
     * 排列组合算法(回溯算法)Cnr
     * </pre>
     *
     * @param codes
     * @param n
     * @param r
     * @return
     */
    public static List<String> combine(String codes[], int n, int r) {
        List<String> list = new ArrayList<String>();
        if (r > n) {
            return list;
        }
        int[] order = new int[r + 1];
        for (int i = 0; i <= r; i++) {
            order[i] = i - 1; // 注意这里order[0]=-1用来作为循环判断标识
        }
        int k = r;
        boolean flag = true; // 标志找到一个有效组合
        int count = 0;
        while (order[0] == -1) {
            if (flag) // 输出符合要求的组合
            {
                String code = "";
                for (int i = 1; i <= r; i++) {
                    code = code + codes[order[i]] + ",";
                }
                list.add(code.substring(0, code.length() - 1));
                count++;
                flag = false;
            }
            order[k]++; // 在当前位置选择新的数字
            if (order[k] == n) // 当前位置已无数字可选，回溯
            {
                order[k--] = 0;
                continue;
            }
            if (k < r) // 更新当前位置的下一位置的数字
            {
                order[++k] = order[k - 1];
                continue;
            }
            if (k == r) {
                flag = true;
            }
        }
        return list;
    }

    /**
     * <pre>
     * 是否存在相同的号码
     * </pre>
     *
     * @param codes
     * @return
     */
    public static boolean existSameCode(String[] codes) {
        if (codes == null) {
            return false;
        }
        Set<String> sameSet = new HashSet<String>(Arrays.asList(codes));
        if (codes.length != sameSet.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <pre>
     * 向前截取N位字符串
     * </pre>
     *
     * @param oldStr 原始字符串
     * @param len    要截取的长度
     * @return
     */
    public static String cutBefore(String oldStr, int len) {
        return oldStr.substring(len);
    }

    /**
     * <pre>
     * 向后截取N位字符串
     * </pre>
     *
     * @param oldStr 原始字符串
     * @param len    要截取的长度
     * @return
     */
    public static String cutAfter(String oldStr, int len) {
        return oldStr.substring(0, oldStr.length() - len);
    }

    /**
     * 校验字符串是否匹配
     *
     * @param value
     * @param format
     * @return
     */
    public static boolean matchParam(String value, String format) {

        if (StringHelper.isEmpty(value) || StringHelper.isEmpty(format)) {
            return true;
        }
        return Pattern.compile(format).matcher(value).matches();
    }

    public static String filterEmoji(String source) {
        if (isNotEmpty(source)) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("");
                return source;
            }
            return source;
        }
        return source;
    }

    /**
     * 去除字符串前后空格，全角空格
     *
     * @param source
     * @return
     */
    public static String removeBlank(String source) {
        if (source == null) {
            return "";
        } else {
            source = source.trim();
            while (source.startsWith("　")) {//这里判断是不是全角空格
                source = source.substring(1, source.length()).trim();
            }
            while (source.endsWith("　")) {
                source = source.substring(0, source.length() - 1).trim();
            }
        }
        return source;
    }


    public static void main(String[] args) {
        String textContent = "       abctest　";
        System.out.println("a" + StringHelper.removeBlank(textContent) + "b");
    }

}
