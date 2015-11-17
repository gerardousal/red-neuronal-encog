package nd.nn.stdper.impl.utils;

/**
 * Created by driucorado on 11/6/15.
 */
public class CountTimer {
    long start;


    public CountTimer start() {
        start = System.currentTimeMillis();
        return this;
    }

    public long getElapsedTimeMiliSeconds() {

        return System.currentTimeMillis() - start;
    }

}
