package freesbell.demo.bean;

import java.util.ArrayList;

public class FileItem{
	public int type;
	public String name;
	public String path;
	public long size;
	public int duration;
	public String time;
	public FileItem parent;
	public ArrayList<FileItem> list;
}