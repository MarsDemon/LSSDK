package longse.com.learing.algorithm;

public class SelectSort {

    private static void selectSort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        int size = array.length; // 数组长度
        int temp = 0; // 临时值

        for (int i = 0; i < size; i++) {
            int j = i;

            for (int k = size - 1; k > i; k--) {
                if (array[k] < array[j]) {
                    j = k;
                }
            }

            //exchange
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            printArray(array);
        }
    }

    public static void printArray(int[] array) {
        System.out.print("{");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    public static void main(String[] args) {
        int array[] = {1,3,67,34,2,9,6,3,9,35,8,4,2,36,9,1};
        System.out.println("选择排序前：" + System.currentTimeMillis());
        printArray(array);

        selectSort(array);

        System.out.println("选择排序后：" + System.currentTimeMillis());

        printArray(array);
    }


}
