package storm_falcon.filecompare;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by Storm_Falcon on 2016/9/19.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Set<Object> set = new HashSet<>(1_0000_0000);

        Function<String, String> processor1 = line -> {
            line = line.replaceAll("\u0000", "");
            int n = line.indexOf(',', 40);
            try {
                return line.substring(n + 1, line.length());
            } catch (Exception ignored) {
                return null;
            }
        };

        Function<String, String> processor2 = line -> {
//            line = line.replaceAll("\u0000", "");
//            int n = line.indexOf('1');
//            try {
//                return line.substring(n + 1, line.length());
//            } catch (Exception e) {
//                return null;
//            }
            return line;
        };

        new FileAppender("d:/test/dialup/2016-09-22_kafka.log", set, processor1, "UTF-8").start();
        new FileAppender("d:/test/dialup/2016-09-22_session.log", set, processor2, "gbk").start();
        Thread t = new Timer(set);
        t.setDaemon(true);
        t.start();
    }
}
//51443
//12287038-12034017=253021
//51443+253021=304464