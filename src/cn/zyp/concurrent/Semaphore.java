package cn.zyp.concurrent;

import java.io.Serializable;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Semaphore implements Serializable {
    private Syc syc;


    public Semaphore(int count){
        syc=new NoFairSyc(count);
    }

    public Semaphore(int count,boolean fair){
        syc=fair? new FairSync(count):new NoFairSyc(count);
    }

    abstract class Syc extends AbstractQueuedSynchronizer{
         Syc(int state){
             setState(state);
         }

         /**
          * 返回值大于等于0，为获取同步状态成功
          * @param i
          * @return
          */
        @Override
        protected abstract int tryAcquireShared(int i);

        @Override
        protected boolean tryReleaseShared(int i) {
           for (;;){
               int state= getState();
               int current=state+i;

               if(compareAndSetState(state,current)){
                   return true;
               }
           }
        }
    }

    /**
     * 不公平
     */
    class NoFairSyc extends Syc{

        NoFairSyc(int state) {
            super(state);
        }
        @Override
        protected int tryAcquireShared(int i) {
            for (;;){
                int state= getState();
                int current=state-i;
                if(current<0 || compareAndSetState(state,current)){
                    return current;
                }
            }
        }
    }

    /**
     * 公平
     */
    class FairSync extends Syc{
        FairSync(int state) {
            super(state);
        }

        @Override
        protected int tryAcquireShared(int i) {
            /**
             * 如果在同步队列中有前驱
             */
            if(hasQueuedPredecessors()){
                return -1;
            }
            for (;;){
                int state= getState();
                int current=state-i;
                if(current<0 || compareAndSetState(state,current)){
                    return current;
                }
            }
        }
    }

    public void acquire(){
         syc.acquireShared(1);
    }

    public void acquire(int count){
       syc.acquireShared(count);
    }

    public boolean release(){
        return syc.release(1);
    }

    public boolean release(int count){
        return syc.release(count);
    }

}
