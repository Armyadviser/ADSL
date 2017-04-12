package statistics;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author gewp
 */
public class Advertisement {
    public static void main(String[] args) throws Exception {
        Path file1 = Paths.get("/home", "falcon", "test", "2017-03-21.log");
        Path file2 = Paths.get("/home", "falcon", "test", "2017-03-22.log");

        Stream<String> stream = Stream.concat(Files.lines(file1), Files.lines(file2));
        stream.filter(line -> line.contains("Push coa success"))
                .map(line -> {
                    String[] items = line.split(",");
                    return items[2].substring(1, items[2].length());
                }).distinct()
                .forEach(System.out::println);
    }
}
