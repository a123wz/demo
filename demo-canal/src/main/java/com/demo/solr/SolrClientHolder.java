package com.demo.solr;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class SolrClientHolder {

		private static CloudSolrClient cloudSolrClient;
		
//		private static String SOLR_URL = PropertyUtil.getValue("solr.service.url.zkHosts");
		private static String SOLR_URL = "172.19.10.38:2182,172.19.10.8:2182";
		
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
	}
