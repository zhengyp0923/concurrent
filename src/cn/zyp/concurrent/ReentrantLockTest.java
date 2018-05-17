package cn.zyp.concurrent;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock=new ReentrantLock(true);

        for (int i = 0; i <5 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"请求锁");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName()+"获得锁");
                    try {
                        try {
                            Thread.sleep(5000l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }finally {
                      lock.unlock();
                        System.out.println(Thread.currentThread().getName()+"释放锁");
                    }
                }
            }).start();
        }
    }
}
