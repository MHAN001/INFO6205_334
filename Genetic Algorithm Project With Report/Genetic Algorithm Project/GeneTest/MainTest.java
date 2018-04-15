import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testMain() throws IOException {
        Main.main(null);
        File output = new File("result.txt");
        assertNotNull(output);
        assertNotEquals(0,output.length());
        System.out.println("we see");
    }
}
