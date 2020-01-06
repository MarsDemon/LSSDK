package longse.com.learing.algorithm;


/**
 * 数组右移K次，原数组<code>[1,2,3,4,5,6,7]<code/>右移三次后结果为<code>[5,6,7,1,2,3,4]<code/>
 *
 * 基本思路：不开辟新的数组控件的情况下在原数组上进行操作
 * 1 将数组倒置，这样k个元素就跑到了数组的前面，然后反转一下即可
 * 2 同理后 len-k 个元素之需要反转就完成数组K次移动
 *
 * @author LY   Date: 26/09/2018
 */
public class ArrayKShift {

    public void arrayKShift(int[] array, int k) {
        /**
         * constrictions
         */
        if (array == null || 0 == array.length) {
            return;
        }

        k = k % array.length;

        if (0 > k) {
            return;
        }

        /**
         * reverse array , e.g: [1,2,3,4] to [4,3,2,1]
         */
        for (int i = 0; i < array.length / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }

        /**
         * first k element reverse
         */
        for (int i = 0; i < k / 2; i++) {
            int tmp = array[i];
            array[i] = array[k - 1 - i];
            array[k - 1 - i] = tmp;
        }

        /**
         * last length - k element reverse
         */
        for (int i = k; i < k + (array.length - k) / 2; i++) {
            int tmp = array[i];
            array[i] = array[array.length - 1 - i - k];
            array[array.length - 1 - i - k] = tmp;
        }
    }

    public static void main(String[] args) {
        int[] array = {1,2,3,4,5,6,7};
        ArrayKShift shift = new ArrayKShift();
        shift.arrayKShift(array, 6);

        System.out.println(array.toString());
    }


}
