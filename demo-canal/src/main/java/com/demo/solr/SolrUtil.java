package com.demo.solr;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.cmiot.rms.dao.model.solr.GatewayBusinessDetailJsSorl;

public class SolrUtil {

		private static Logger log = LoggerFactory.getLogger(SolrUtil.class);
		
		public static SolrClient client=SolrClientHolder.getHttpSolrClient();
//		public static SolrClient client=SolrClientHolder.getCloudSolrClient();
		/**
		 * 增加和更新索引
		 * @throws Exception
		 */
		public static void createIndexByObj(String coreName, Object obj) throws Exception {
//			CloudSolrClient client = SolrClientHolder.getCloudSolrClient();
			client.addBean(coreName,obj);// 如果不存在就新增，存在就更新
			client.commit(coreName);
		}
		/**
		 * 增加和更新索引
		 * @throws Exception
		 */
		public static void createIndexBySolrInputDocument(String coreName, SolrInputDocument doc) throws Exception {
//			CloudSolrClient client = SolrClientHolder.getCloudSolrClient();
			client.addBean(coreName,doc);// 如果不存在就新增，存在就更新
			client.commit(coreName);
		}
		
		/**
		 * 批量增加和更新索引
		 * @throws Exception
		 */
		public static void createIndexByObjs(String coreName,Collection<?> objs) throws Exception {
//			CloudSolrClient client = SolrClientHolder.getCloudSolrClient();
			client.addBeans(coreName,objs);// 如果不存在就新增，存在就更新
			client.commit(coreName);
		}
		
		/**
		 * 删除索引
		 * 
		 * @throws Exception
		 */
		public static void deleteSolrIndex(String coreName,String id) throws Exception {
//			CloudSolrClient client = SolrClientHolder.getCloudSolrClient();
			@SuppressWarnings("unused")
			UpdateResponse result = client.deleteById(coreName,id);
//			client.commit("");
			client.commit(coreName);
		}
		
		/**
		 * 批量删除索引
		 * 
		 * @throws Exception
		 */
		public static void deleteSolrIndex(String coreName,List<String> ids) throws Exception {
//			CloudSolrClient client = SolrClientHolder.getCloudSolrClient();
			@SuppressWarnings("unused")
			UpdateResponse result = client.deleteById(coreName,ids);
			client.commit();
		}

		/**
		 * 借助SolrRequestParsers解析“检索字符串”进行检索
		 * 
		 * @param fq 过滤条件 格式： （name:zzz）
		 * @param orders 支持多个排序  key是用作排序的字段， value： 升序or降序  SolrQuery.ORDER.asc   SolrQuery.ORDER.desc 
		 * @param start 查询跳过条数  
		 * @param rows 每页显示条数
		 * @return
		 * @throws Exception
		 */
		public static <T>SolrResult<T> querySolr(String coreName,String[] fq, SortedMap<String, ORDER> orders, Integer start, Integer rows, Class<T> cls){
			
			SolrResult<T> sr = new SolrResult<T>();
//			SolrClient client = SolrClientHolder.getHttpSolrClient();
			SolrQuery query = new SolrQuery();
			query.set("q", "*:*");// 参数q  查询所有   
			if(fq != null){
				for(String filter : fq ){
					query.addFilterQuery(filter);
				}
			}
			if(orders != null){
				for(Entry<String, ORDER> entry : orders.entrySet()){
					query.addSort(entry.getKey(), entry.getValue());
				}
			}
			if(start != null){
				query.set(CommonParams.START, start);
			}
			if(rows != null){
				query.set(CommonParams.ROWS, rows);
			}
			
			try {
				log.info("列表查询，查询参数："+ query.toQueryString());
				QueryResponse queryResponse = client.query(coreName,query,METHOD.POST);
//				System.out.println(JSON.toJSONString(queryResponse));
				sr.setTotalNum(queryResponse.getResults().getNumFound());
				sr.setDatas(queryResponse.getBeans(cls));
				return sr;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}