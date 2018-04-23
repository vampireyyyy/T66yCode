package t66y;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Main {
	public static void main(String [] args) throws FileNotFoundException, JavaLayerException {
	    //new HtmlParser().work();
	    new HtmlParser().workAnsy();
	    //new HtmlParser().getFakeCode("http://cc.ddder.us/htm_data/7/1803/3036076.html");
	    //Document doc = new HtmlParser().getDoc("http://cc.ddder.us/htm_data/7/1803/3041309.html");
	    //System.out.println(doc.text() );
	    //String s = "https://blog.sina.com.cn/s/blog_52531e650100lqm3.html";
	    //System.out.println(s);
	    //System.out.println(Decrypt.decrypt("1a282a2b2237372a2937372a235a2a22"));
	    //test();
	}
	
	private static void test() {
	    String a = "2018--03--09-08:15:58 08 啊啊啊啊";
	    String b = a.replaceAll("\\w+", "a");
	    System.out.println(b);
	}
}
