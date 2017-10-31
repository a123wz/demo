package org.demo.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

import com.demo.solr.SolrResult;
import com.demo.solr.SolrUtil;

public class SolrUtilTest {
	//对象属性必须加上@field注解
	public static class Person{
		@Field
		private String id;
		@Field
		private String addr;
		@Field
		private String name;
		@Field
		private Integer age;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		
	}
	
	public static void querySolrTest(){
		try {
			List<String> fqList = new ArrayList<String>();
			// fqList.add("gatewayPassword:6024448246");
//			 fqList.add("addr:重庆");
			// fqList.add("status:\"-2\" or status:\"-1\" or status:\"0\" or status:\"1\"");
//			 fqList.add("orderTime:[0 TO 20170824164932]");
			SortedMap<String, ORDER> orders = new TreeMap<String, ORDER>();
//	    	orders.put("orderTime", ORDER.desc);
	    	long l1 = System.currentTimeMillis();
	    	SolrResult<Person> list = SolrUtil.querySolr("test", fqList.toArray(new String[]{}), orders, 0, 100, Person.class);
	    	System.out.println(list.getTotalNum());
	    	for(Person person:list.getDatas()){
	    		System.out.println(person.getAddr());
//	    		SolrUtil.deleteSolrIndex("test", person.getId());
	    	}
//	    	System.out.println(  System.currentTimeMillis() - l1 +":"+list.getTotalNum());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addTest(){
		Person person=new Person();
		for(int i=0;i<50;i++){
			person.setId("00"+i);
			person.setAddr("重庆");
			person.setName("b"+i);
			try {
				SolrUtil.createIndexByObj("test",person);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void addDocTest() {
		SolrInputDocument doc = new SolrInputDocument("id","addr","age","name");
	    doc.addField("id", "22");
	    doc.addField("addr", "重庆");
	    doc.addField("age", 20);
	    doc.addField("name", "bb");
	    try {
			SolrUtil.createIndexBySolrInputDocument("test",doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void deleteTest(){
		try {
			SolrUtil.deleteSolrIndex("test", "11");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		deleteTest();
//		addTest();
//		addDocTest();
//		querySolrTest();
		for(int i=0;i<10;i++){
			Thread thread = new Thread(new Runnable() {
				Integer i=0;
				public void run() {
					synchronized (i) {
						while (i<100000) {
							i++;
							String uuid=UUID.randomUUID().toString();
							Person person=new Person();
							person.setId(""+uuid);
							person.setAddr("重庆");
							person.setName("b"+uuid.substring(0, 5));
							try {
								SolrUtil.createIndexByObj("test",person);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
			thread.start();
		}
	}
}
