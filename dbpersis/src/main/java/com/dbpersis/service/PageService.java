package com.dbpersis.service;

import java.util.List;
import java.util.Map;

import com.dbpersis.utils.BeanHandler;
import com.dbpersis.utils.Page;
import com.dbpersis.utils.PageUtils;

public class PageService {
	public MyDataSource dataSource;
	public QueryService queryService;

	public PageService() {
		dataSource = new MyDataSource();
		queryService = new QueryService(dataSource);
	}

	public PageService(MyDataSource dataSource) {
		if (dataSource == null) {
			throw new RuntimeException("DataSource can not be null");
		}
		this.dataSource = dataSource;
		queryService = new QueryService(dataSource);
	}
	
	@SuppressWarnings("rawtypes")
	public <R> Page queryForPage(String queryName, BeanHandler<R> beanListHandler,Map<String, Object> params, Integer pageNo, Integer pageSize) throws Exception{
		pageNo = PageUtils.getPageNo(pageNo);
		pageSize = PageUtils.getPageSize(pageSize);
		Integer first = PageUtils.getFirst(pageNo, pageSize);
		List<R> list = queryService.query(queryName, beanListHandler,params, first, pageSize);
		Long totalCount = queryService.restQueryCount(queryName, params);
		int b = new Long(totalCount).intValue();
		Page<R> page = new Page<>(b, pageNo, pageSize, list);
		return page;
	}
}
