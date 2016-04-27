package server;

import java.io.*;
import java.util.regex.Pattern;

public class VideoFile implements Serializable {
	private String id, title, filename;

	public VideoFile() {
		this.id = null;
		this.title = null;
		this.filename = null;
	}

	public VideoFile(String id, String title, String filename) {
		this.id = id;
		this.title = title;
		this.filename = filename;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getFilename() {
		// TODO Auto-generated method stub
		return filename;
	}

	public void setID(String attributeValue) {
		this.id = attributeValue;
	}

	public void setTitle(String newContent) {
		this.title = newContent;
	}

	public void setFilename(String newContent) {
		this.filename = newContent;
	}

	public Boolean checkValid() {
		//Pattern validIDFormat = Pattern.compile("\d{8}\p{Lower}\d");
		//String[] validVideoFiletypes = {"*.mp4","*.mpg"};
		return true;
	}
	
	public String toString() {
		return this.getTitle();
	}
}
