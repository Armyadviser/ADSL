package statistics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        String file = "2017-01-05.log";
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

        Set<String> set = new HashSet<>();
        FileReader reader = new FileReader();
        reader.open("2017-01-05.log");
        while (reader.hasNext()) {
            String line = reader.getLine();
            if (!line.contains("Kick off succ:true")) {
                continue;
            }
            line = line.substring(30, line.length());
            set.add(line);
        }
        reader.close();

        Map<String, Integer> map = new HashMap<>();

        for (String line : set) {
            String[] items = line.split(",");
            Integer value = map.get(items[0]);
            if (value == null) {
                map.put(items[0], 0);
            } else {
                map.put(items[0], value + 1);
            }
        }

        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }
}
