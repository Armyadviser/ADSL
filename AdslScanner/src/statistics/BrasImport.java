package statistics;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by falcon on 17-1-4.
 *
 */
public class BrasImport {
    public static void main(String[] args) throws Exception {
        String file = "";
        int length = 2;
        int ipPos = 0;
        int contextPos = 1;

        Files.lines(Paths.get("/home/falcon/test/" + file))
                .map(line -> line.replaceAll("\\s+", " "))
                .map(line -> line.split(" "))
                .filter(items -> items.length == length)
                .map(items -> "update cp_nas_t " +
                        "set nas_codes='secret=ln2008ok;context=" + items[contextPos] + "' " +
                        "where nas_id='" + items[ipPos] + "';")
                .forEach(System.out::println);
    }
}
