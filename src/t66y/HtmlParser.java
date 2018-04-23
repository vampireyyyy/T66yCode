package t66y;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class HtmlParser {
    private HttpManager mHttp;
    public HtmlParser() {
        mHttp = new HttpManager();
    }
    
    private static String NAMES[] = {"VampireXXXX1","VampireXXXX2", "VampireXXXX3", "VampireXXXX4", "VampireXXXX5"};
    public void workAnsy() {
        new Thread() {
            public void run() {
                while(true) {
                    List<Code> list = work();
                    int i = 0;
                    for(Code code: list) {
                        System.out.println("Get One Key: " + code);
                        boolean success = mHttp.registerP(code.code, NAMES[i]);
                        if(success) {
                            i++;
                            Player player;
                            try {
                                player = new Player(new FileInputStream(new File("D:\\EclipseWorkSpace\\t66y\\res\\audio.mp3")));
                                player.play();
                            } catch (FileNotFoundException | JavaLayerException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        }
                    }
                    try {
                        Thread.sleep(1000 * 60 * 3);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    
    public List<Code> work() {
        List<Code> list = new ArrayList();
        List<Code> temp = new ArrayList();
        System.out.print("Start****" + new Date().toString() + "");
        temp.addAll( 
                step("http://dc.ddder.us/thread0806.php?fid=7&search=&page=1")
                );
        temp.addAll(
                step("http://dc.ddder.us/thread0806.php?fid=7&search=&page=2")
                );
        
        for(Code c: temp) {
            if( !list.contains(c)) {
                list.add(c);
                System.out.println("GetCode: " + c);
            }
        }
        
        
        System.out.println("****"+ new Date().toString() + " code count:" +list.size() + "****End\n");
        return list;
    }
    
    public List<Code> step(String enter) {
        List<String> list = getBlogUrl(enter);
        List<Code> result = new ArrayList<>();
        for(String url:list) {
            //System.out.println("parser blog: " + url);
            result.addAll(getFakeCode(url));
        }
        return result;
    }
    //http://cc.ddder.us/thread0806.php?fid=7&search=&page=1
    //http://cc.ddder.us/thread0806.php?fid=7&search=&page=2
    
    public Document getDoc(String url) {
        Document d = null;
        Connection  con = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)") 
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "application/json")
                //.header("Upgrade-Insecure-Requests", "1")
                //.header("Connection", "keep-alive")
                //.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
               // .header("Accept-Encoding", "gzip, deflate")
                //.header("Accept-Language", "zh-CN,en-US;q=0.8")
                            ;
        try {
            d =con.get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
        }
        return d;
    }
    
    /**
     * 获得 blog 地址
     * @param url
     * @return
     */
    public List<String> getBlogUrl(String url) {
        ArrayList<String> list = new ArrayList<>();
        Document doc = getDoc(url);
        
        Elements es = doc.select("tbody>tr[class=tr3 t_one tac]");
        
        for(Element e: es) {
            String temp = e.select("td").get(1).select("h3>a[href]").attr("abs:href");
            if(temp == null || temp.isEmpty()) {
                //System.out.println("parser: " + url + " error:");
            }else {
                list.add(temp);
            }
        }
        
        removeBadLinks(list);
        
        return list;
    }
    
    private void removeBadLinks(List<String> list) {
        ArrayList<String> badlist = new ArrayList<String>();
        for(String s: list) {
            if(!s.contains("htm_data")) {
                badlist.add(s);
            }else if(s.contains("htm_data/7/1602/37458.html")) {
                badlist.add(s);
            }else if(s.contains("htm_data/7/1704/2353653.html")) {
                badlist.add(s);
            }else if(s.contains("htm_data/7/1709/2404767.html")) {
                badlist.add(s);
            }else if(s.contains("htm_data/7/1709/2520305.html")) {
                badlist.add(s);
            }
        }
        list.removeAll(badlist);
    }
    
    public List<Code> getFakeCode(String url){
        List<Code> list = new ArrayList<Code>();
        Document doc = getDoc(url);
        
        if( doc == null) {
            //System.out.println("getFakeCodeFailed: " + url);
            return list;
        }
        
        Elements es = doc.select("div[class=t t2]");
        for(Element e: es) {

            String content = e.select("tr[class=tr1 do_not_catch]").select("tbody").text();
            String date = e.select("div[class=tipad]").text();
            if(date.contains("[email protected]")) {
                String temp = e.select("div[class=tipad]>a[class=__cf_email__]").attr("data-cfemail");
                date = date.replace("[email protected]", Decrypt.decrypt(temp));
                //System.out.println("矫正过: " + date);
            }
            list.addAll(Code.getCode(url, content, date));
        }
        
       /* if(list.size()>0) {
            for(Code code: list) {
                System.out.println(code);
            }
        }*/
        //http://cc.ddder.us/htm_data/7/1803/3039306.html
        return list;
    }
    
    
    
}
