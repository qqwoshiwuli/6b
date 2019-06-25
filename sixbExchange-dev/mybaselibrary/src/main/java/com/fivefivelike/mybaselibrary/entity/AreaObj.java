package com.fivefivelike.mybaselibrary.entity;
/**
 * 地区类
 * @author Administrator
 *
 */
public class AreaObj {
	private String name; //地名
	private String areaid; //自身id
	private String parentid;//父城市id
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	
}
