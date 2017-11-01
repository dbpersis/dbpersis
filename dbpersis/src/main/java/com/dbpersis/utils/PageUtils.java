package com.dbpersis.utils;

public class PageUtils {

	public static Integer getPageSize(Integer pageSize) {
		// TODO Auto-generated method stub
		return pageSize;
	}

	public static Integer getPageNo(Integer pageNo) {
		// TODO Auto-generated method stub
		return pageNo;
	}

	public static Integer getFirst(Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		pageNo = pageNo!=null?pageNo:1;
		pageSize = pageSize!=null?pageSize:10;
		Integer first = pageSize*(pageNo-1);
		return first;
	}

}
