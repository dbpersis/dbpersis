/**
 * @Date 2017年10月23日
 *
 * @author terry
 */
package com.dbpersis.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.dbpersis.utils.BeanHandler;
import com.dbpersis.utils.Queries;
import com.dbpersis.utils.Query;
import com.dbpersis.utils.Association;
import com.dbpersis.utils.Associations;


public class QueryService {
	//private Logger 
	public static VelocityEngine ve = null;
	public static Pattern p = Pattern.compile("#qin\\s*\\(\\s*\\:\\w+\\s+\\s*\\)");
	public static Map<String, Query> queryDefine = new HashMap<String, Query>();
	private static List<Association> associDefine = new ArrayList<Association>();
	
	public MyDataSource dataSource;
	public QueryService(){
		dataSource = new MyDataSource();
		
	}
	
	public static void init(String queryYmlPath,String associationsYmlPath){
		try {
			/*
			 * 加载查询语句
			 */
			Constructor constructor = new Constructor(Queries.class);
			TypeDescription typeDescription = new TypeDescription(Query.class);
			typeDescription.putListPropertyType("queries", Query.class);
			constructor.addTypeDescription(typeDescription);
			Yaml yaml = new Yaml(constructor);
			List<File> listFiles = new ArrayList<File>();
			File queryYml = new File(queryYmlPath);
			getAllYmalFile(queryYml,listFiles);

			for (File f : listFiles) {
				InputStream input = new FileInputStream(f);
				Queries queries = (Queries) yaml.load(input);
				for (Query query : queries.getQueries()) {
					queryDefine.put(query.getName(), query);
				}

			}
			/*
			 * 加载模板引擎，用于处理查询语句
			 */
			ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
					"org.apache.velocity.runtime.log.Log4JLogChute");
			ve.setProperty(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "");

			Constructor consMap = new Constructor(Associations.class);
			TypeDescription typeDesc = new TypeDescription(Association.class);
			typeDesc.putListPropertyType("associations", Association.class);
			consMap.addTypeDescription(typeDesc);
			Yaml yamlMap = new Yaml(consMap);
			List<File> list = new ArrayList<File>();
			File associationsYml = new File(associationsYmlPath);
			getAllYmalFile(associationsYml,list);
			for(File f: list){
				InputStream inputMap = new FileInputStream(f);
				Associations associations = (Associations) yamlMap.load(inputMap);
				for (Association ass : associations.getAssociations()) {
					associDefine.add(ass);
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void getAllYmalFile(File file,List<File> listFiles ) {
		//System.out.println(file.getPath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				getAllYmalFile(f,listFiles);
			}
		} else {
			listFiles.add(file);
		}
	}

	public static String getQueryResultType(String sqlName){
		String resultType = queryDefine.get(sqlName).getResultType();
		return resultType;
	}
	@SuppressWarnings("unchecked")
	public static String getQueryStatement(String sqlName, Map<String, Object> params) {
		try{
			queryDefine.get(sqlName).getStatement();
		}catch(NullPointerException ex){
			return null;
			
		}
		String sqlStatement = queryDefine.get(sqlName).getStatement();
		if(StringUtils.isBlank(sqlStatement)){
			return sqlName;
		}
		Pattern p = Pattern.compile("#qin\\s*\\(\\s*\\:\\w+\\s+\\s*\\)");
		Matcher vm = p.matcher(sqlStatement);
		StringBuffer sb = new StringBuffer();
		while (vm.find()) {
			String qinMacro = vm.group(0);
			for (Entry<String, Object> entry : params.entrySet()) {
				Pattern pdd = Pattern.compile("\\s*\\(\\s*\\:" + entry.getKey() + "\\s*\\)");
				if (pdd.matcher(qinMacro).find()) {
					StringBuffer buff = new StringBuffer();
					String result = "";
					buff.append(" in ");
					buff.append(" (");
					for (String item : (List<String>) entry.getValue()) {
						buff.append("\'" + item + "\',");
					}
					result = buff.substring(0, buff.length() - 1).toString();
					result += ") ";
					vm.appendReplacement(sb, result);
				}

			}
			if (sb.length() > 0) {
				sqlStatement = String.valueOf(sb) + " #end";
			}
		}
		for (Entry<String, Object> entry : params.entrySet()) {
			sqlStatement = sqlStatement.replace(":" + entry.getKey(), "'" + entry.getValue().toString() + "'");
		}
		/* 去掉所有的: 避免模板干扰 */
		sqlStatement = sqlStatement.replace(":", "");

		VelocityContext context = new VelocityContext();
		for (Entry<String, Object> entry : params.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
		StringWriter sw = new StringWriter();
		boolean evaluate = ve.evaluate(context, sw, "", sqlStatement);
		if (evaluate) {
			sw.flush();
		}
		String str = sw.toString();
		return str;
	}
	

	public <T> List<T> query(String sqlName, BeanHandler<T> beanListHandler, Map<String, Object> params, int pageIndex,int pageSize) throws Exception {
		String sql = getQueryStatement(sqlName, params);
		List<T> result=null;
		if(StringUtils.isNotBlank(sql)){
			result = new MyDataSource().query(sql, beanListHandler);
			if(getQueryResultType(sqlName).equals("Map")){
				return result;
			}
			if(beanListHandler.getType().getSimpleName().equals("Map")){
				return result;
			}
		}
		else{
			result = new MyDataSource().query(sqlName, beanListHandler);
		}
		
		if (result.size() > 0) {
			for (Association ass : associDefine) {
				
				if (ass.getOwnerClass().equals(beanListHandler.getType().getSimpleName())) {
					for (Object ob : result) {
						Class c = ob.getClass();
						Field[] fields = c.getDeclaredFields();
						for (Field f : fields) {
							f.setAccessible(true);
							if (f.getName().equals(ass.getOwnerProperty())) {
								List<Object> list = null;
								for (Field fs : fields) {
									if (fs.getName().equals(ass.getOwnerParam())) {
										String sqltem = "select * from " + ass.getTargetClass() + " where "
												+ ass.targetProperty + "='" + fs.get(ob) + "'";

										ParameterizedType pt = (ParameterizedType) f.getGenericType();
										Type[] tList = pt.getActualTypeArguments();
										if (tList.length > 0) {
											Class<?> doubleClass = Class.forName(tList[0].getTypeName());
											list = query(sqltem, new BeanHandler(doubleClass),null,0,0);
										}
									}
								}
								BeanUtils.setProperty(ob, ass.getOwnerProperty(), list);
							}

						}
					}
				}
			}
		}
		return result;
	}
}
