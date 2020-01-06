package longse.com.learing.algorithm;

import java.util.Scanner;

public class Test {

    public static void main(String args[]) {


        testD2B();
        //testB2D();
    }
    /**
     * 讲10 进制转化为二进制
     * @param de
     * @return
     */
    public static String Decimal2Binary(int de){
        String numstr = "";
        while (de>0){
            int res = de%2; //除2 取余数作为二进制数
            numstr = res + numstr;
            de = de/2;
        }
        return numstr;
    }

    /**
     * 将二进制转换为10进制
     * @param bi
     * @return
     */
    public  static  Integer Biannary2Decimal(int bi){

        String binStr = bi+"";

        Integer sum = 0;
        int len = binStr.length();

        for (int i=1;i<=len;i++){
            //第i位 的数字为：
            int dt = Integer.parseInt(binStr.substring(i-1,i));
            sum+=(int)Math.pow(2,len-i)*dt;
        }
        return  sum;
    }


    public static void testB2D(){
        while (true){

            System.out.println("Pleace input a Binary num:");
            Scanner sc = new Scanner(System.in);
            int binary = sc.nextInt();

            int out = Biannary2Decimal(binary);

            System.out.println("The Decimal num is :" + out);

            System.out.println("输入0 结束，输入1 继续");
            sc = new Scanner(System.in);

            if (sc.nextInt()==0){
                break;
            }
        }
    }


    public static void testD2B(){
        while (true) {

            System.out.println("Pleace input a int Decimal num:");
            Scanner sc = new Scanner(System.in);
            int num = sc.nextInt();
//            String numofBinary = Decimal2Binary(num);
            String numofBinary = Integer.toBinaryString(num);

            System.out.println("The Binary num is :" + numofBinary);

            System.out.println("输入0 结束，输入1继续");
            sc = new Scanner(System.in);

            if (sc.nextInt() == 0) {
                break;
            }
        }
    }
}
