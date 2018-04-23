package t66y;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Code {
    public String code = "";
    public String date = "";
    public String url = "";
    public Code(String code) {
        this.code = code;
    }
    public Code(String code,String date, String url) {
        this.code = code;
        this.date = date;
        this.url = url;
    }
    
    @Override
    public String toString() {
        return "Code[" + code + " :" + date + ":"+url + "]";
    }
    
    @Override
    public boolean equals(Object c) {
        if(this.code.equals( ( (Code) c) .code)) {
            return true;
        }else {
            return false;
        }
    }
    
    public static List<Code> getCode(String url,String content,  String date) {
        List<Code> list = new ArrayList<Code>();
        String dateString = formatTime(date);
        if(timeAvaliable(dateString, url)) {
            parserString(content, list, url, dateString);
        }       
        return list;
    }
    


    private static boolean parserString(String input, List<Code> list, String url,  String date){
        //System.out.println("parserString: " + input );
        String temp = "";
        boolean success = false;
        //去除磁力链接
        input = input.replaceAll("magnet\\:\\?xt=[\\w:_%-&=.]+", "");
        input = input.replaceAll("https?[/\\\\w:_%-&=.]+", "");
        
        for(int i = 0; i < input.length() - 15; i++) {
            
            temp = input.substring(i, i + 16);
            boolean iscode = isCode_1(temp);
            
            if(iscode) {
                Code c = new Code(temp, date, url);
                
                list.add(c);
                success = true;
            }
        }
        return success;
    }
    
    private static boolean isCode_1(String s) {
        char [] cs = s.toCharArray();
        int count = 0;
        
        for(char c: cs) {
            if(   ( (c>='0') && (c<='9'))
                ||( (c>='a') && (c<='f'))
                ||( (c>='A') && (c<='F'))
                ) {// 0-9, a-f, A-F
                count ++;
            }else if(c == ' ') {
                count = -19999;
            }
        }
        
        if(count>13) {//正常count = 16
            return true;
        }else {
            return false;
        }
    }
    private static String formatTime(String dateString) {
        if(dateString.contains("Posted:")) {
            dateString = dateString.substring(dateString.indexOf("Posted:") + 7);
        }
        if(dateString.contains("| 回")) {
            dateString = dateString.substring(0, dateString.indexOf("| 回"));
        }
        dateString = dateString.replace("@", "-");
        dateString = dateString.replaceAll("\\D+", "-").trim();
        dateString = dateString.replace("--", "-");
        dateString = dateString.replaceAll(" ", "");
        
        String [] list = dateString.split("-");
        
        String result = "";
        for(int i = 0; i < 6; i ++) {    //消除过多的  数字，填补缺少的数字
            if( i<list.length) {
                result += "-" + list[i];
            }else {
                result += "-01";
            }
        }
        //System.out.println(result);
        result= result.replace("--", "-");
        if(result.startsWith("-")) {
            result = result.substring(1);
        }
        
        if(result.split("-").length < 0) {
            System.out.println("\nERROR:" + dateString +"____________"+ result +  "                                                         **");
        }
        
        return result;
    }
    private static boolean timeAvaliable(String dateString, String url) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").parse(dateString);
            //System.out.println(d);
            Date now = new Date();
            long delta = ( now.getTime() - d.getTime())/1000;//相隔多少秒
            //if(url.contains("3072227.html")) {
            //    System.out.println(d + "***" + dateString + "***" + delta);
           //}
            if(delta> 60*10) {
               // System.out.println("too old: " + delta*1.0/60/60 + " hour");
                return false;
            }else {
               // System.out.println("just so so: " + delta*1.0/60/60 + " hour");
                return true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("timeAvaliable: parser failed: \"" + dateString + "\" : " + url);
            return true;
        }
    }
}
