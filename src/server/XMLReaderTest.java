package server;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XMLReaderTest {
	private XMLReader reader;
	private List<VideoFile> videoList;
	
	@Before
	public void setUp() throws Exception {
		reader = new XMLReader();
		videoList = reader.getList("videoList.xtml");
	}

	@Test
	public void test() {
		assertTrue(videoList instanceof List);
	}
	
	@Test
	public void listContainsVideoFiles() {
		 assertTrue(videoList.get(0) instanceof VideoFile);
		 } 
	 @Test
	 public void videoFileReturnsCorrectFields() {
		 VideoFile videoFile = videoList.get(0);
		 assertNotNull(videoFile.getID());
		 assertNotNull(videoFile.getTitle());
		 assertNotNull(videoFile.getFilename());
	 }
}
