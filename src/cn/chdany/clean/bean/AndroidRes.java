package cn.chdany.clean.bean;

/**
 * e.g. R.drawable.foo
 * @author chdany
 *
 */
public class AndroidRes {
	private String type;
	private String name;
	private boolean used;
	public AndroidRes() {}
	public AndroidRes(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public String toJavaSearchString(){
		return "R."+type+"."+name;
	}
	public String toXmlSearchString(){
		return "@"+type+"/"+name;
	}
	
}
