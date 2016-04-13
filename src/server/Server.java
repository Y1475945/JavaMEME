package server;

import java.util.List;

public class Server {
	public XMLReader myReader;
	public List<VideoFile> videoList;
	public Server ()
	{
		myReader = new XMLReader("videoList.xml");
	}

	public List<VideoFile> getPopulatedList() {
		this.videoList = myReader.getList();
		return videoList;
	}

}
