package server;

import java.util.ArrayList;
import java.util.List;


public class XMLReader {

	public List<VideoFile> getList(String string) {
		
		List<VideoFile> videoList;
		videoList = new ArrayList<VideoFile>();
		VideoFile videofile = new VideoFile();
		videoList.add(videofile);
		
		return videoList;
	}

}
