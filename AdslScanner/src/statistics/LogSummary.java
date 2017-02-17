package statistics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by falcon on 17-1-6.
 *
 */
public class LogSummary {

    public static Stream<String[]> getFail(String file) throws Exception {
        String path = "/home/falcon/test/" + file;
        return Files.lines(Paths.get(path))
                .filter(line -> line.contains("Kick off succ:false"))
                .distinct()
                .map(line -> line.substring(30, line.length()))
                .map(line -> line.split(","));
    }

    public static Stream<String[]> getLoginCity(String file) throws Exception {
        String path = "/home/falcon/test/" + file;
        return Files.lines(Paths.get(path))
                .filter(line -> line.contains("Kick off succ:true"))
                .distinct()
                .map(line -> line.substring(30, line.length()))
                .map(line -> line.split(","));
    }

    public static void main(String[] args) throws Exception {
        String file = "/home/falcon/test/2017-02-14.log";
        Files.lines(Paths.get(file))
            .filter(line -> line.contains("Kick off succ:true"))
            .distinct()
            .map(line -> line.substring(30, line.length()))
            .map(line -> line.split(","))
            .collect(Collectors.groupingBy(items -> items[1]))
            .entrySet()
            .stream()
            .map(entry -> entry.getKey() + "\t" + entry.getValue().size())
            .forEach(System.out::println);

    }
}
