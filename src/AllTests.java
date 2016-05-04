import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ server.XMLReaderTest.class, client.ClientTest.class, server.ServerTest.class 
}) 
public class AllTests {

}
