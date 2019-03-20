package freesbell.demo.bean;

import java.io.Serializable;

public class AlarmLogBean implements Serializable{
  private String camName;
  private String did;
  private String content;
  private String createtime;
public String getCamName() {
	return camName;
}
public void setCamName(String camName) {
	this.camName = camName;
}
public String getDid() {
	return did;
}
public void setDid(String did) {
	this.did = did;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getCreatetime() {
	return createtime;
}
public void setCreatetime(String createtime) {
	this.createtime = createtime;
}
@Override
public String toString() {
	return "AlarmLogBean [camName=" + camName + ", did=" + did + ", content="
			+ content + ", createtime=" + createtime + "]";
}
  
}
