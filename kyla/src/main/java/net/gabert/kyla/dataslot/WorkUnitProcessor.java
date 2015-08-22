package net.gabert.kyla.dataslot;

import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.concurrent.*;

public class WorkUnitProcessor {
    private static final Logger LOGGER = LogUtil.getLogger();

    private static final int POLL_TIMEOUT = 1000;
    private static final TimeUnit POLL_TIMEOUT_UNIT = TimeUnit.MILLISECONDS;

    private final BlockingQueue<WorkUnit> workUnits;
    private final ExecutorService executorService;
    private final int workersCount;
    private final int workQueueHardLimit;

    private volatile boolean stop = false;

    public WorkUnitProcessor(KylaConfiguration kylaConfiguration) {
        this.workQueueHardLimit = kylaConfiguration.getWorkQueueHardLimit();
        this.workersCount = kylaConfiguration.getWorkersCount();

        this.workUnits = new ArrayBlockingQueue<>(this.workQueueHardLimit);
        this.executorService = Executors.newFixedThreadPool(this.workersCount);
    }

    public synchronized void start() {
        for (int i = 0; i < this.workersCount; i++) {
            this.executorService.submit(new Worker());
        }

        LOGGER.info("Created " + this.workersCount + " Workers.");
    }

    public synchronized void shutdown() {
        this.executorService.shutdown();
        this.stop = true;
    }

    public void createWorkUnit(Message message, Endpoint enpoint) {
        WorkUnit workUnit = new WorkUnit(message, enpoint);
        workUnits.offer(workUnit);
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

    private WorkUnit nextWorkUnit() {
        try {
            return workUnits.poll(POLL_TIMEOUT, POLL_TIMEOUT_UNIT);
        } catch (InterruptedException e) {
            // FIXME: revisit
            this.shutdown();
        }

        return null;
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
