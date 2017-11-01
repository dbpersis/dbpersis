package com.dbpersis.utils;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Page<T> implements Serializable {
	private long totalCount;
	private int pageNo;
	private int pageSize;
	private List<T> data;
	public Page(long totalCount, int pageNo, int pageSize, List<T> data){
		this.totalCount=totalCount;
		this.pageNo=pageNo;
		this.pageSize=pageSize;
		this.data=data;
	}

}
