package cn.zyp.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountDownLatch {

    private Sync sync;

    public CountDownLatch(int count) {
        sync = new Sync(count);
    }

    private class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return (getState() == 0) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int i) {
            for (; ; ) {
                int state = getState();
                if (state == 0) {
                    return false;
                }
                int next = state - i;
                if (compareAndSetState(state, next)) {
                    return state == 0;
                }
            }
        }
    }

    public void await() {
        sync.acquireShared(1);
    }

    public void countDown() {
        sync.releaseShared(1);
    }
}
