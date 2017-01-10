package statistics;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by falcon on 17-1-8.
 *
 */
public class Catalina {
    public static void main(String[] args) throws Exception {
        Files.lines(Paths.get("/home/falcon/test/catalina.out"));
    }
}
