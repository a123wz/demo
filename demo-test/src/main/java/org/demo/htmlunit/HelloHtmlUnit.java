package org.demo.htmlunit;

import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class HelloHtmlUnit{
	
	public static void addCookie(CookieManager cookieManager,String str){
		for(String s:str.split(";")){
			String[] arr = s.split("=");
			Cookie cookie = new Cookie("12306.cn", arr[0], arr[1]);
			cookieManager.addCookie(cookie);
		}
	}
	
    public static void main(String[] args) throws Exception{
        String str;
        //使用FireFox读取网页
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        
        webClient.addRequestHeader("Referer", "https://kyfw.12306.cn/otn/index/init");
        
        CookieManager cookieManager= webClient.getCookieManager();
        
//        cookie.
//		cookieManager.addCookie(cookie);
        
        addCookie(cookieManager,"JSESSIONID=C92625CF4AE5C4026386E524D4684A8E; tk=V9stMzWVaxL9mgt9qnPOAAOkEh15wuNuqra2a0; BIGipServerotn=938476042.50210.0000; RAIL_EXPIRATION=1514504672886; RAIL_DEVICEID=mik0Zme3teAEL5LPItI6O-_rCsyyXuC6wsHoZy9dNlg3SrF_fPVN0qSIUlzUUI5A8AwAre5-ELMW_Kr2Wz4Ojyk1ksndoYl_HdeZwNYrJ37rDIHtdNx8w5Cfd-KMOAExXh3Z9HT7Wd8Ia3S86a47D4yfWeU-egr9; BIGipServerpool_passport=317522442.50215.0000; _jc_save_detail=true; route=9036359bb8a8a461c164a04f8f50b252; current_captcha_type=Z; _jc_save_fromStation=%u6210%u90FD%2CCDW; _jc_save_toStation=%u91CD%u5E86%2CCQW; _jc_save_fromDate=2018-01-01; _jc_save_toDate=2017-12-27; _jc_save_wfdc_flag=dc");
        
        HtmlPage page = webClient.getPage("https://kyfw.12306.cn/otn/index/initMy12306");
        
        
//        page.s
        str = page.getTitleText();
        
        System.out.println("str:"+str);
        
//        for(Cookie cookie :webClient.getCookieManager().getCookies()){
//        	System.out.println("cookie name:"+cookie.getName()+"  value:"+cookie.getValue());
//        }
        boolean bk = true;
        while (bk) {
        	page = webClient.getPage("https://kyfw.12306.cn/otn/index/initMy12306");
        	Cookie cookie = webClient.getCookieManager().getCookie("JSESSIONID");
        	System.out.println("cookie name:"+cookie.getName()+"  value:"+cookie.getValue());
        	
        	List<FrameWindow> list = page.getFrames();
        	for(FrameWindow frameWindow:list){
            	System.out.println("frame:"+frameWindow.getName());
            	HtmlPage pageTwo = (HtmlPage) frameWindow.getEnclosedPage();
            	System.out.println(pageTwo.asXml());
            }
        	
        	Thread.sleep(20000);;
		}
        //关闭webclient
//        webClient.close();
    }
}