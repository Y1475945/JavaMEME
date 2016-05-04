package server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	Server myServer;
	@Before
	public void setUp() throws Exception {
		myServer = new Server("videoList.xml");
	}

	@Test
	public void test() {
		assertNotNull(myServer.getPopulatedList());
	}

}
