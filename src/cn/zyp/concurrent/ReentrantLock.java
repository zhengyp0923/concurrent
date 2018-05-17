package cn.zyp.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReentrantLock implements Lock{
    private Sync sync;

    public ReentrantLock(){
        sync=new NofairSync(0);
    }

    public ReentrantLock(boolean fair){
        sync=fair? new FairSync(0) : new NofairSync(0);
    }

     abstract class Sync extends AbstractQueuedSynchronizer{

        Sync(int count){
            setState(count);
        }


        @Override
        protected boolean tryRelease(int i) {
            Thread current = Thread.currentThread();
            if(current!=getExclusiveOwnerThread()){
                throw  new IllegalStateException();
            }
            int state = getState();
            int next = state - i;
            setState(next);
            if(next==0){
                setExclusiveOwnerThread(null);
                return  true;
            }
             return false;
        }
    }

    /**
     * 不公平锁
     */
     class NofairSync extends Sync{
         NofairSync(int count) {
             super(count);
         }
         @Override
         protected boolean tryAcquire(int i) {
             Thread current = Thread.currentThread();
             int state = getState();
             if(state==0){
                 if(compareAndSetState(0,i)){
                     setExclusiveOwnerThread(current);
                     return true;
                 }

             }else if(current==getExclusiveOwnerThread()){
                 int next = state + i;
                 setState(next);
                 return true;
             }
             return false;
         }
     }

    /**
     * 公平锁
     */
    class FairSync extends Sync{
          FairSync(int count) {
              super(count);
          }

          @Override
          protected boolean tryAcquire(int i) {
              /**
               * 判断同步队列是否有前驱
               */
              if(hasQueuedPredecessors()){
                  return false;
              }
              Thread current = Thread.currentThread();
              int state = getState();
              if(state==0){
                  if(compareAndSetState(0,i)){
                      setExclusiveOwnerThread(current);
                      return true;
                  }

              }else if(current==getExclusiveOwnerThread()){
                  int next = state + i;
                  setState(next);
                  return true;
              }
              return false;
          }
      }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void unlock() {
       sync.release(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
