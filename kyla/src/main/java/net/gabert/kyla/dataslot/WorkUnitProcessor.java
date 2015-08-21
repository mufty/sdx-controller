package net.gabert.kyla.dataslot;

import net.gabert.kyla.api.Configuration;
import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.api.Endpoint.Message;

import java.util.concurrent.*;

import static net.gabert.util.Log.logger;
        
public class WorkUnitProcessor {
    private static final int POLL_TIMEOUT = 1000;
    private static final TimeUnit POLL_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

    private final BlockingQueue<WorkUnit> workUnits;
    private final ExecutorService executorService;
    private final int workersCount;
    private final int workQueueHardLimit;

    private volatile boolean stop = false;

    public WorkUnitProcessor(Configuration configuration) {
        this.workQueueHardLimit = configuration.getWorkQueueHardLimit();
        this.workersCount = configuration.getWorkersCount();

        this.workUnits = new ArrayBlockingQueue<>(this.workQueueHardLimit);
        this.executorService = Executors.newFixedThreadPool(this.workersCount);
    }

    public synchronized void start() {
        for (int i = 0; i < workersCount; i++) {
            this.executorService.submit(new Worker());
        }
    }

    public synchronized void shutdown() {
        this.executorService.shutdown();
        this.stop = true;
    }

    public void createWorkUnit(Message message, Endpoint enpoint) {
        WorkUnit workUnit = new WorkUnit(message, enpoint);
        workUnits.offer(workUnit);
    }

    private WorkUnit nextWorkUnit() {
        try {
            return workUnits.poll(POLL_TIMEOUT, POLL_TIMEOUT_UNIT);
        } catch (InterruptedException e) {
            // FIXME: revisit
            this.shutdown();
        }

        return null;
    }

    public void await() {
        while (workUnits.isEmpty() == false) {
            //pass
            //TODO: Need to test if any handler is in process of handling
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (stop == false) {
                WorkUnit workUnit = nextWorkUnit();

                //FIXME: Handle exceptions
                if (workUnit != null) {
                    workUnit.endpoint.handle(workUnit.message);
                }
            }
        }
    }
    
    private static class WorkUnit {
        public final Message message;
        public final Endpoint endpoint;

        public WorkUnit(Message message, Endpoint endpoint) {
            this.message = message;
            this.endpoint = endpoint;
        }
    }
}
