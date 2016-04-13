package server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XMLReaderTest {
	private XMLReader reader;
	private List<VideoFile> videoList;

	@Before
	public void setUp() throws Exception {
		reader = new XMLReader("videoList.xml");
		videoList = reader.getList();
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

	@Test
	public void videoFileCorrectlyRead() {
		List<VideoFile> testVideoList = new ArrayList<VideoFile>();
		testVideoList.add(new VideoFile("20120213a2", "Monsters Inc.",
				"monstersinc_high.mpg"));
		testVideoList.add(new VideoFile("20120102b7", "Avengers",
				"avengers-featurehp.mp4"));
		testVideoList.add(new VideoFile("20120102b4", "Prometheus",
				"prometheus-featureukFhp.mp4"));

		for (int i = 0; i < 3; i++) {
			assertTrue(testVideoList.get(i).getID()
					.equals(videoList.get(i).getID()));
			assertTrue(testVideoList.get(i).getTitle()
					.equals(videoList.get(i).getTitle()));
			assertTrue(testVideoList.get(i).getFilename()
					.equals(videoList.get(i).getFilename()));
		}
	}

}
