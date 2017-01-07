package statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Created by falcon on 17-1-6.
 *
 */
public class AccessLog {

    public static Stream<String> getIp(String file) throws IOException {
        file = "/home/falcon/test/" + file;
        return Files.lines(Paths.get(file))
                .map(line -> line.substring(0, line.indexOf(' ')));
    }

    public static void main(String[] args) throws Exception {
//        getIp("localhost_access_log.2017-01-05.txt")
//            .distinct()
//            .forEach(System.out::println);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 8);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        System.out.println(c.getTime());
        System.out.println(c.getTime().getTime() / 1000);
    }
}
