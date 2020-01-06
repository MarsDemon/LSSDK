package longse.com.learing.utils;

public class BinaryConversion {

    private static String[] binaryArray =
            {"0000", "0001", "0010", "0011",
                    "0100", "0101", "0110", "0111",
                    "1000", "1001", "1010", "1011",
                    "1100", "1101", "1110", "1111"};

    private static String hexStr = "0123456789ABCDEF";

    /**
     * @param bArray
     * @return 转换为二进制字符串
     */
    public static String bytes2BinaryStr(byte[] bArray) {
        StringBuilder outStr = new StringBuilder();
        int pos = 0;
        for (byte b : bArray) {
            // 高四位
            pos = (b & 0xF0) >> 4;
            outStr.append(binaryArray[pos]);

            // 低四位
            pos = b & 0x0F;
            outStr.append(binaryArray[pos]);
        }

        return outStr.toString();
    }


    public static String BinaryToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            // 字节高四位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            // 字节低四位
            hex += String.valueOf(hex.charAt(bytes[i] & 0x0F));
            result.append(hex);
        }
        return result.toString();
    }

    public static byte[] HexStringToBinary(String hexString) {
        // hexString 的长度对2取整，作为 bytes 的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0; // 字节高四位
        byte low = 0; // 字节低四位

        for (int i = 0; i < len; i++) {
            // 右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high|low); // 高低位做或运算
        }
        return bytes;
    }

    public static void main(String[] args) {

        String str = "二进制与十六进制互转测试";
        System.out.println("源字符串：\n"+str);

        String hexString = BinaryToHexString(str.getBytes());
        System.out.println("转换为十六进制：\n"+hexString);
        System.out.println("转换为二进制：\n"+bytes2BinaryStr(str.getBytes()));

        byte [] bArray = HexStringToBinary(hexString);
        System.out.println("将str的十六进制文件转换为二进制再转为String：\n"+new String(bArray));
    }
}
