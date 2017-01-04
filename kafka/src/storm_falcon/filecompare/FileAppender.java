package storm_falcon.filecompare;

import com.ge.adsl.DialupDownInfo;
import com.ge.adsl.DialupInfo;
import com.ge.util.FileReader;

import java.util.Set;
import java.util.function.Function;

/**
 * Created by Storm_Falcon on 2016/9/20.
 */
public class FileAppender extends Thread {

    private final String path;
    private final String encode;
    private final Set<Object> set;
    private final Function<String, String> function;

    public FileAppender(String path, Set<Object> set, Function<String, String> function, String encode) {
        this.path = path;
        this.encode = encode;
        this.set = set;
        this.function = function;
    }

    public void run() {
        FileReader reader = new FileReader();
        reader.open(path, encode);

        while (reader.hasNext()) {
            String line = reader.getLine();
            line = function.apply(line);

            if (line == null) {
                continue;
            }

            DialupInfo info = DialupDownInfo.parse(line);
            String string = format((DialupDownInfo) info);

            if (string == null) {
                continue;
            }

            int hash = string.hashCode();
//			System.out.println(hash);
            add(hash);
        }
        reader.close();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String format(DialupDownInfo info) {
        if (info == null) {
            return null;
        }
        return info.getKey();
    }

    public void add(Object t) {
        synchronized (set) {
            int size = set.size();
            set.add(t);
            if (size == set.size()) {
                set.remove(t);
            }
        }
    }

}
