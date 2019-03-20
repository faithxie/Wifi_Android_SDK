package freesbell.demo.bean;

import java.io.Serializable;

public class LocalVideoPictureBean implements Serializable{
private String filePath;
private String createTime;
private String type;
private String did;
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public String getCreateTime() {
	return createTime;
}
public void setCreateTime(String createTime) {
	this.createTime = createTime;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getDid() {
	return did;
}
public void setDid(String did) {
	this.did = did;
}
@Override
public String toString() {
	return "LocalVideoPictureBean [filePath=" + filePath + ", createTime="
			+ createTime + ", type=" + type + ", did=" + did + "]";
}


}
