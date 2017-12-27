package org.demo.httpclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author yan
 * @date 2016-6-9 11:03:04
 * @version V1.0
 * @desc
 */
public class HttpsUtil {

    public static final String get(final String url, final Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("");

        if (null != params && !params.isEmpty()) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key).append("=").append(params.get(key));
                i++;
            }
        }

        CloseableHttpClient httpClient = createSSLClientDefault();

        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url + sb.toString());
        String result = "";

        try {
            response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

    public static final String post(final String url, final Map<String, Object> params) {
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpPost post = new HttpPost(url);
        post.setHeader("Cookie", "tk=3dcnFQ7Sp2IsBBk3ooPzeLSvq_NSrLrksda2a0; JSESSIONID=469C40657E46CD14141EED6CBF78824E; BIGipServerotn=938476042.50210.0000; RAIL_EXPIRATION=1514504672886; RAIL_DEVICEID=mik0Zme3teAEL5LPItI6O-_rCsyyXuC6wsHoZy9dNlg3SrF_fPVN0qSIUlzUUI5A8AwAre5-ELMW_Kr2Wz4Ojyk1ksndoYl_HdeZwNYrJ37rDIHtdNx8w5Cfd-KMOAExXh3Z9HT7Wd8Ia3S86a47D4yfWeU-egr9; BIGipServerpool_passport=317522442.50215.0000; current_captcha_type=Z; _jc_save_fromStation=%u6210%u90FD%2CCDW; _jc_save_toStation=%u91CD%u5E86%2CCQW; _jc_save_fromDate=2017-12-29; _jc_save_toDate=2017-12-26; _jc_save_wfdc_flag=dc; _jc_save_detail=true; route=6f50b51faa11b987e576cdb301e545c4");
        post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        post.setHeader("X-Requested-With","XMLHttpRequest");
        post.setHeader("Referer","https://kyfw.12306.cn/otn/queryOrder/init");
        
        CloseableHttpResponse response = null;

        if (null != params && !params.isEmpty()) {
            List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue()==null?"":entry.getValue().toString());
                nvpList.add(nvp);
            }
            post.setEntity(new UrlEncodedFormEntity(nvpList, Charset.forName("UTF-8")));
        }

        String result = "";

        try {
            response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

    private static CloseableHttpClient createSSLClientDefault() {

        SSLContext sslContext;
        try {
        	String key="c:/client";  
            KeyStore keystore=KeyStore.getInstance("JKS");  //创建一个keystore来管理密钥库  
            keystore.load(new FileInputStream(key),"123456".toCharArray());  
            //创建jkd密钥访问库  
            TrustManagerFactory tmf=TrustManagerFactory.getInstance("SunX509");  
            tmf.init(keystore);                 //验证数据，可以不传入key密码  
            //创建TrustManagerFactory,管理授权证书  
            sslContext=SSLContext.getInstance("SSLv3");  
            // 构造SSL环境，指定SSL版本为3.0，也可以使用TLSv1，但是SSLv3更加常用。  
            sslContext.init(null,tmf.getTrustManagers(),null);  
        	
//            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//                //信任所有
//                public boolean isTrusted(X509Certificate[] xcs, String string){
//                    return true;
//                }
//            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyStoreException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
			// TODO: handle exception
		}

        return HttpClients.createDefault();
    }

    public static void main(String[] args) {
//        System.out.println("Result:" + get("https://kyfw.12306.cn/otn//leftTicket/init", null));
//    	test();
    	bind();
    }
    public static void bind(){
    	String url = "https://kyfw.12306.cn/otn/queryOrder/queryMyOrder";
    	
    	String paraStr="queryType=1&queryStartDate=2017-12-01&queryEndDate=2017-12-25&come_from_flag=my_order&pageSize=8&pageIndex=0&query_where=H&sequeue_train_name=";
    	Map<String, Object> para = new HashMap<>();
    	for(String str:paraStr.split("&")){
    		if (str.split("=").length==2) {
    			para.put(str.split("=")[0],str.split("=")[1]);
			}else{
				para.put(str.split("=")[0],null);
			}
    		
    	}
    	
    	
    	
    	String result = post(url, para);
    	
    	System.out.println(result);
//    	System.out.println("Result:" + get("https://kyfw.12306.cn/otn/index/initMy12306", null));
    }
    public static void test(){
    	Map<String, Object> para = new HashMap<>();
    	String url = "https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2017-12-29&leftTicketDTO.from_station=CDW&leftTicketDTO.to_station=CQW&purpose_codes=ADULT";
    	String result = get(url, null);
    	System.out.println("Result:" + result);
    	JSONObject jsonObject = JSON.parseObject(result);
    	JSONObject object = (JSONObject) jsonObject.getJSONObject("data");
    	JSONArray arr= object.getJSONArray("result");
    	for(int i=0;i<arr.size();i++){
    		String[] detail = arr.getString(i).split("\\|");
    		System.out.println(detail[0]);
    		System.out.println("i:"+i+"\t length:"+detail.length+"\t value:"+arr.getString(i));
//    		arr.get
    	}
    	System.out.println();
    	
    }
}