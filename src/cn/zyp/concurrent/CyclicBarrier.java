package cn.zyp.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CyclicBarrier {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int count;

    public CyclicBarrier(int count) {
        if(count<0){
            throw new RuntimeException("count must >0");
        }
        this.count = count;
    }

    public int await() {
        lock.lock();
        try {
            // 判断count>0 为了避免第count+1个线程进入
            if (count > 0) {
                --count;
            }
            if (count == 0) {
                condition.signalAll();
                return 0;
            }
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
        return count;
    }
}
