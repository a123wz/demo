package org.demo.canal;

import com.demo.annotation.canal.Table;
import com.demo.annotation.canal.Field;

@Table(tableName = "person")
public class Person {
	@Field
	private String id;
	@Field
	private String name;
//	@CannalField
	private Integer age;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
