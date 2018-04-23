package t66y;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpManager {
	

    private boolean check(String code) {
        try {
            String result = post(code + "&action=reginvcodeck");
            //GetResponse(code + "&action=reginvcodeck");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean registerP(String code, String name) {
        char [] cs = code.toCharArray();
        char c = 0;
        int count = 0;
        
        boolean success = false;
        for(int i = 0; i < cs.length; i ++) {
            c = cs[i];
            if( (c >= 'a' && c<='z')
                || (c >='0' && c<='9')
                || (c >='A' && c<='Z')){
                
            }else {
                if(count == 0) {
                    cs[i] = '*';
                }else if(count == 1){
                    cs[i] = '#';
                }else if(count == 2) {
                    cs[i] = '&';
                }
                count ++;
            }
        }
        code = new String(cs);
        //System.out.println(code);
        
        if(count == 0 ) {
            register(code, name);
        }else if(count == 1) {
            for(char cc: ARRAYS) {
                String temp = code.replace('*', cc);
                success = register(temp, name);
                if(success) {
                    break;
                }
            }
        }else if(count == 2) {
            for(char cc: ARRAYS) {
                String temp = code.replace('*', cc);
                for(char ccc: ARRAYS) {
                    String temp2 = temp.replace('#', ccc);
                    success = register(temp2, name);
                    if(success) {
                        break;
                    }
                }
            }
        }else if(count == 3) {
            for(char cc: ARRAYS) {
                String temp = code.replace('*', cc);
                for(char ccc: ARRAYS) {
                    String temp2 = temp.replace('#', ccc);
                    for(char cccc: ARRAYS) {
                        String temp3 = temp2.replace('&', cccc);
                        success = register(temp3, name);
                        if(success) {
                            break;
                        }
                    }
                }
            }
        }
        
        return success;
    }
    
    synchronized private boolean register(String code, String name) {
        System.out.println(code);
       // return false;
        String content = "regname="+name +"&regpwd=cjw199102&regpwdrepeat=cjw199102&regemail=vampireofchen%40gmail.com&invcode=" + code + "&forward=&step=2";
        String response = "";
        try {
            response = post(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(response.contains("邀請碼錯誤")){
            System.out.println("注册失败:" + code);
            return false;
        }else {
            System.out.println("注册成功: " + name 
                     +"_"+code );
            File f = new File("C:/main.html");
            try {
                if(!f.exists()) {
                        f.createNewFile();
             
                }
                FileOutputStream out = new FileOutputStream(f);
                out.write(response.getBytes("gbk"));
                out.close();       
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }

    }
	
	
	private String getHexString(byte b) {
	    int value = Byte.toUnsignedInt(b);
	    int high = value/16;
	    int low = value%16;
	    return ARRAYS[high] +""+ ARRAYS[low];
	}
	
	private static final char ARRAYS[] = {
	        '0','1','2','3','4','5', '6','7',
            '8','9','a','b','c','d', 'e','f'
    };
	
	static HashMap<String ,String> map = new HashMap<>() ;
	static {
        map.put("Connection","keep-alive");
        map.put("Cache-Control","max-age=0");
        map.put("Origin", "http://private70.ghuws.win");
        map.put("Upgrade-Insecure-Requests", "1");
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.1; MIX 2 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"); 
        map.put("Refere", "http://private70.ghuws.win/register.php");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,en-US;q=0.8");
        //map.put("Transfer-Encoding", "chunked");
	}
	
	private String post(String input) throws IOException {
	    OkHttpClient client = new OkHttpClient();
        Headers header = Headers.of(map);
	    byte[] buff = input.getBytes("utf-8");
	    RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), buff);
	    Request request = new Request.Builder()
	            .url("http://private70.ghuws.win/register.php?")
	            .headers(header)
	            //.addHeader("Content-Length", "" + buff.length)
	            .post(body).build();
	    Response response = client.newCall(request).execute();
	    String message = response.message();
	    Headers responseHeaders = response.headers();
        //System.out.println(message + responseHeaders);
        
        
	    ResponseBody responseBody = response.body();
	    InputStream responseInputStream = responseBody.byteStream();
	    String result = new String(unGZip(responseInputStream), "gbk");
	    //System.out.println(result);
	    return result;
	}

	/***
	  * 解压GZip
	  * 
	  * @param data
	  * @return
	  */
	 public static byte[] unGZip(InputStream input) {
	  byte[] b = null;
	  try {
	   GZIPInputStream gzip = new GZIPInputStream(input);
	   byte[] buf = new byte[1024];
	   int num = -1;
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   while ((num = gzip.read(buf, 0, buf.length)) != -1) {
	    baos.write(buf, 0, num);
	   }
	   b = baos.toByteArray();
	   baos.flush();
	   baos.close();
	   gzip.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }

}
