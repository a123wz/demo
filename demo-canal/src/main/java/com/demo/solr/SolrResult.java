package com.demo.solr;

import java.util.List;

public class SolrResult<T> {

	
	private Long totalNum;
	
	private List<T> datas;

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	
	
}
