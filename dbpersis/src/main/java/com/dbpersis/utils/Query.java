/**
 * @Date 2017年10月23日
 * @author terry
 */
package com.dbpersis.utils;

public class Query {

  public String name;
  public String resultType;
  public String statement;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getResultType() {
    return resultType;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

}
