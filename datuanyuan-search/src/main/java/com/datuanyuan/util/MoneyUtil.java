package com.datuanyuan.util;

import java.math.BigDecimal;

/**
 * <pre>
 * 金额运算工具类
 * </pre>
 * 
 * @author admin
 */
public class MoneyUtil {

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
            "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    private MoneyUtil() {
    }

    /**
     * <pre>
     * 元转换为分
     * </pre>
     * 
     * @param money
     * @return int
     */
    public static int convertYuanToFen(Double money) {
        if (money == null) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal multiply = decimal.multiply(decimal2);
        return multiply.intValue();
    }

    /**
     * <pre>
     * 元转换为分
     * </pre>
     * 
     * @param money
     * @return Long
     */
    public static long convertLongYuanToFen(Double money) {
        if (money == null) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money);
        decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal multiply = decimal.multiply(decimal2);
        return multiply.longValue();
    }

    /**
     * <pre>
     * 元转换为分
     * </pre>
     * 
     * @param money
     * @return
     */
    public static long convertLongYuanToFen(Long money) {
        if (money == null) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal multiply = decimal.multiply(decimal2);
        return multiply.longValue();
    }

    /**
     * <pre>
     * 元转换为分
     * </pre>
     * 
     * @param money
     * @return
     */
    public static Integer convertYuanToFen(Integer money) {
        if (money == null) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal multiply = decimal.multiply(decimal2);
        return multiply.intValue();
    }

    /**
     * <pre>
     * 元转换为分
     * </pre>
     * 
     * @param money
     * @return
     */
    public static Long convertYuanToFen(String money) {
        if (StringHelper.isEmpty(money)) {
            return 0L;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal multiply = decimal.multiply(decimal2);
        return multiply.longValue();

    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static String convertFenToYuan(Integer money) {
        if (money == null) {
            return "0.00";
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 2, BigDecimal.ROUND_HALF_UP);
        return divide.toString();
    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static String convertFenToYuan(Long money) {
        if (money == null) {
            return "0.00";
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 2, BigDecimal.ROUND_HALF_UP);
        return divide.toString();
    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static String convertFenToYuanDouble(Double money) {
        if (money == null) {
            return "0.00";
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 2, BigDecimal.ROUND_HALF_UP);
        return divide.toString();
    }

    /**
     * 分转元
     * @param money
     * @return
     */
    public static Integer convertFenToYuanInteger(String money) {
        if(money==null||"".equals(money)||"null".equals(money)){
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money).setScale(0, BigDecimal.ROUND_HALF_UP);
        double value = decimal.doubleValue();
        return NumberUtils.KeepDecimal(value);
    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static double convertFenToYuanDouble(Long money) {
        if (money == null) {
            return 0D;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 99, BigDecimal.ROUND_HALF_UP);
        return divide.doubleValue();
    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static Integer convertIntFenToYuan(Integer money) {
        if (money == null) {
            return 0;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 99, BigDecimal.ROUND_HALF_UP);
        return divide.intValue();
    }

    /**
     * <pre>
     * 分转换为元
     * </pre>
     * 
     * @param money
     * @return
     */
    public static Long convertLongFenToYuan(Long money) {
        if (money == null) {
            return 0L;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 99, BigDecimal.ROUND_HALF_UP);
        return divide.longValue();
    }

    public static Double convertFenToYuanNum(Integer money) {
        if (money == null) {
            return 0D;
        }
        BigDecimal decimal = new BigDecimal(money);
        BigDecimal decimal2 = new BigDecimal(100);
        BigDecimal divide = decimal.divide(decimal2, 99, BigDecimal.ROUND_HALF_UP);
        return divide.doubleValue();
    }

    /**
     * <pre>
     * 金额相加
     * </pre>
     * 
     * @param num1
     * @param num2
     * @return
     */
    public static double add(double num1, double num2) {
        BigDecimal n1 = new BigDecimal(Double.toString(num1));
        BigDecimal n2 = new BigDecimal(Double.toString(num2));
        return n1.add(n2).doubleValue();
    }

    /**
     * <pre>
     * 金额相减
     * </pre>
     * 
     * @param num1
     *            减数
     * @param num2
     *            被减数
     * @return 差值
     */
    public static double sub(double num1, double num2) {
        BigDecimal n1 = new BigDecimal(Double.toString(num1));
        BigDecimal n2 = new BigDecimal(Double.toString(num2));
        return n1.subtract(n2).doubleValue();
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     * 
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        // 这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
    /**
     * 直接舍弃小数
     * @param monthMoney
     * @return
     */
    public static long accuracy(double monthMoney) {
        BigDecimal dec = new BigDecimal(monthMoney);
        BigDecimal setScale = dec.setScale(0, BigDecimal.ROUND_DOWN);
        return setScale.longValue();
    }

    public static void main(String args[]) {
        System.out.println(convertFenToYuan(2000000l));
        System.out.println(convertLongYuanToFen(0.11d));
        System.out.println(convertYuanToFen(0.00d));
        System.out.println(convertFenToYuanNum(1));
        System.out.println((convertYuanToFen(0.10d) * 1) / 1000);
        System.out.println(convertYuanToFen("2.00"));
    }
}
