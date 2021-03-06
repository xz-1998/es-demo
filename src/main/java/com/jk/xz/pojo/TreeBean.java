package com.jk.xz.pojo;

import java.util.List;

/**
 * 
 * <pre>项目名称：users    
 * 类名称：TreeBean    
 * 类描述：    
 * 创建人：徐卓
 * 创建时间：2021年3月11日 下午2:29:33    
 * 修改人：徐卓
 * 修改时间：2021年3月11日 下午2:29:33    
 * 修改备注：       
 * @version </pre>
 */
public class TreeBean {
private  Integer id;
private  String text;
private  Integer pid;
private  String href;
private  boolean selectable;
private  List<TreeBean> nodes;


public boolean isSelectable() {
	return selectable;
}
public void setSelectable(boolean selectable) {
	this.selectable = selectable;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public Integer getPid() {
	return pid;
}
public void setPid(Integer pid) {
	this.pid = pid;
}
public String getHref() {
	return href;
}
public void setHref(String href) {
	this.href = href;
}
public List<TreeBean> getNodes() {
	return nodes;
}
public void setNodes(List<TreeBean> nodes) {
	this.nodes = nodes;
}



}
