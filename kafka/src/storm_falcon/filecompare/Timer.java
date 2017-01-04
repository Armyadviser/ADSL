package storm_falcon.filecompare;

import java.util.Set;

/**
 * Created by Storm_Falcon on 2016/9/20.
 */
public class Timer extends Thread {
    private final Set<Object> set;

    public Timer(Set<Object> set) {
        this.set = set;
    }

    public void run() {
        while (true) {
            System.out.println(set.size());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
