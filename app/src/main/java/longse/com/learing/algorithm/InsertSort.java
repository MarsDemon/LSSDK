package longse.com.learing.algorithm;

/**
 * 插入排序 O(n2) 时间
 */
public class InsertSort {

    public static void insertSort(int[] number) {
        int size = number.length;

        int temp = 0;
        int j = 0;

        for (int i = 0; i < size; i++) {
            temp = number[i];
            // 加入比前一个数值小，则将前一个数值后移
            for (j = i; j > 0 && temp < number[j - i]; j--) {
                number[j] = number[j - 1];
            }
            number[j] = temp;
        }
    }

}
