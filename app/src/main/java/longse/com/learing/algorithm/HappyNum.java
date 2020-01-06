package longse.com.learing.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: 判断一个数字是否为快乐数字 19是快乐数字， 11就不是快乐数字
 * 19
 * 1*1+9*9 = 82
 * 8*8+2*2 = 68
 * 6*6+8*8 = 100
 * 1*1+0*0+0*0 = 1
 *
 * 11
 * 1*1+1*1 = 2
 * 2*2 = 4
 * 4*4 = 16
 * 1*1+6*6 = 37
 * 3*3+7*7 = 58
 * 5*5+8*8 = 89
 * 8*8+9*9 = 149
 * 1*1+4*4+5*5 = 42
 * 4*4+2*2 = 20
 * 2*2+0*0 = 4
 *
 *
 * A happy number is a number defined by the following process: Starting with any positive integer,
 * replace the number by the sum of the squares of its digits,
 * and repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1.
 * Those numbers for which this process ends in 1 are happy numbers.
 *
 * 这里结果 2*2 = 4 和 2*2+0*0 = 4 重复，所以不是快乐数字
 *
 * @author LY Date: 25/09/2018
 *
 * @since JDK 1.8
 *
 */
public class HappyNum {

    /**
     * 判断一个数字是否为快乐数字
     * @param number
     * @return
     */
    public boolean isHappy(int number) {
        Set<Integer> set = new HashSet<>(30);
        while (number != 1) {
            int sum = 0;
            while (number > 0) {
                sum += (number % 10) * (number % 10);
                number = number / 10;
            }

            if (set.contains(sum)) {
                return false;
            } else {
                set.add(sum);
            }
            number = sum;
        }
        return true;
    }

    public static void main(String[] args) {
        int num = 345;
        int i = num % 10;
        int i1 = num / 10;
        int i2 = i1 / 10;
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i2);
    }

}
