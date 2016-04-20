package client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.VideoFile;

public class ClientTest {
	private Client client;
	
	@Before
	public void setUp() throws Exception {
		server.Server.main(null);
		client = new Client();
	}

	@Test
	public void test() {
		VideoFile videoFile = client.videoList.get(0);
		assertEquals("20120213a2", videoFile.getID());
		assertEquals("Monsters Inc.", videoFile.getTitle());
		assertEquals("monstersinc_high.mpg", videoFile.getFilename()); 
	}

}
