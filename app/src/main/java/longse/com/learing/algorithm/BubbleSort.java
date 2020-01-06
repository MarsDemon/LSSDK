package longse.com.learing.algorithm;

/**
 * 冒泡排序
 */
public class BubbleSort {

    /**
     *  比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     */
    private static void FromMinToMax() {
        System.out.println("BubbleSort.FromMinToMax ==start " + System.currentTimeMillis());
        int count[] = {1,3,67,34,2,9,6,3,9,35,8,4,2,36,9,1};
        for (int i = 0; i < count.length - 1; i++) {
            for (int j = 0; j < count.length - 1 - i; j++) {
                if (count[j] > count[j + 1]) {
                    int temp = count[j];
                    count[j] = count[j + 1];
                    count[j + 1] = temp;
                }
            }
            System.out.print("第" + (i + 1) + "次排序结果是：");
            for (int k : count) {
                System.out.print(k + "\t");
            }
            System.out.println();
        }
        System.out.println("BubbleSort.FromMinToMax ==end " + System.currentTimeMillis());
    }

    private static void FromMaxToMin() {
        int count[] = {89, 56, 94, 82, 12, 3};
        for (int i = 0; i < count.length - 1; i++) {
            for (int j = 0; j < count.length - 1 - i; j++) {
                if (count[j] < count[j + 1]) {
                    int temp = count[j];
                    count[j] = count[j + 1];
                    count[j + 1] = temp;
                }
            }
            System.out.print("第" + (i + 1) + "次排序结果是：");
            for (int k : count) {
                System.out.print(k + "\t");
            }
            System.out.println();
        }
    }



    public static void main(String[] args) {
        FromMinToMax();
    }
}
