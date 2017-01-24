package statistics;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by falcon on 17-1-22.
 *
 */
public class PushQuery4GTest {

    public static String replaceUrlArgs(String url, String id, String code) {
        return url.replace("<id>", id)
                .replace("<code>", code);
    }

    public static void main(String[] args) throws Exception {
        String url = "http://130.91.101.28:8085/crm/AdslPushQuery4G?UserId=<id>&DiscntCode=<code>";
        Files.lines(Paths.get("/home/falcon/temp/test.txt"), Charset.forName("gbk"))
                .map(line -> line.split("\t"))
                .map(items -> replaceUrlArgs(url, items[1], items[2]))
                .peek(System.out::println)
//                .map(HttpTools::get)
                .forEach(System.out::println);
    }
}
