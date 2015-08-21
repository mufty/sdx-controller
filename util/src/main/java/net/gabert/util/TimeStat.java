package net.gabert.util;

import java.util.ArrayList;
import java.util.List;

public class TimeStat {
    private static class Marker {
        public final String message;
        public final long time;

        private Marker(String message, long time) {
            this.message = message;
            this.time = time;
        }
    }

    private List<Marker> markers = new ArrayList<>();

    public synchronized void mark(String message) {
        long t = System.currentTimeMillis();
        markers.add(new Marker(message, t));
    }

    public void printTimesBetweenMarkers() {
        for (int i = 1; i < markers.size(); i++) {
            long delay = markers.get(i).time - markers.get(i-1).time;
            Log.sout("["+ delay + "ms] " + markers.get(i-1).message +" -> "+markers.get(i).message);
        }
    }
}
