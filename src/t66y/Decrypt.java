package t66y;

import java.io.UnsupportedEncodingException;

public class Decrypt {
    public static String decrypt(String input) {
        String result = "";
        int die = r(input, 0);
        
        for(int i = 2;i<input.length();i +=2 ) {
            int temp = r(input, i) ^ die;
            try {
                result += new String(new byte[] {(byte)temp}, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
        return result ;
    }

    
    private static int r(String s, int index) {
        s = s.substring(index, index+2);
        return Integer.parseInt(s, 16);
    }
}
