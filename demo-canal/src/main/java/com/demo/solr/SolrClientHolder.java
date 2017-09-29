package com.demo.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;

public class SolrClientHolder {

		private static CloudSolrClient cloudSolrClient;
		
//		private static String SOLR_URL = PropertyUtil.getValue("solr.service.url.zkHosts");
//		private static String SOLR_URL = "172.19.10.38:2182,172.19.10.8:2182";
		private static String SOLR_URL = "172.19.10.5:2182";
		
		private static Object LOCK1 = new Object();

		public static CloudSolrClient getCloudSolrClient(){
			synchronized (LOCK1) {
				if (cloudSolrClient == null) {
					cloudSolrClient = new CloudSolrClient.Builder().withZkHost(SOLR_URL).build();
					cloudSolrClient.setZkClientTimeout(60000);
					cloudSolrClient.setZkConnectTimeout(50000);// 最大连接数
					cloudSolrClient.setSoTimeout(60000);//// 设置读数据超时时间(单位毫秒) 1000
				}
				return cloudSolrClient;
			}
		}
		public static String HTTP_SOLR_URL="http://127.0.0.1:8983/solr/";
		public static SolrClient client;
		/**
	      * 创建SolrServer对象
	      * 
	      * 该对象有两个可以使用，都是线程安全的  
	      * 1、CommonsHttpSolrServer：启动web服务器使用的，通过http请求的 
	      * 2、 EmbeddedSolrServer：内嵌式的，导入solr的jar包就可以使用了  
	      * 3、solr 4.0之后好像添加了不少东西，其中CommonsHttpSolrServer这个类改名为HttpSolrClient
	      * 
	      * @return
	      */
	     public static SolrClient getHttpSolrClient(){
	    	 HttpSolrClient solr = null;
	         solr = new HttpSolrClient(HTTP_SOLR_URL);
	         solr.setConnectionTimeout(5000);    //solr连接超时时间  
	         solr.setSoTimeout(5000);            //solr查询超时时间  
	         solr.setMaxTotalConnections(100);   //solr所有最大连接数  
	         solr.setDefaultMaxConnectionsPerHost(100);//solr最大连接数  
	         solr.setAllowCompression(false);    //solr是否允许压缩  
	         solr.setFollowRedirects(true);  
	         return solr;
	     }
	     public static void main(String[] args) {
	    	 SolrClient client = getHttpSolrClient();
	    	 System.out.println(client);
		}
	}
