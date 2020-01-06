package longse.com.learing.lockOrthread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1.Java 并发库中 ReentrantReadWriteLock 实现了 ReadWriteLock 中接口并添加了可重入的特性；
 * 2.ReentrantReadWriteLock 的读写锁明显高于 synchronized;
 * 3.ReentrantReadWriteLock 读写锁的实现中，读锁使用共享模式，写锁使用独占模式，换句话说，读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
 * 4.ReentrantReadWriteLock 读写锁的实现中，要注意，当有读锁时候，写锁就不能获得；而当有写锁的时候，除了获得写锁的这个线程可以获得读锁外，其他线程不能
 * 获得读锁。
 */
public class ReadAndWriteLock {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读操作
     *
     * @param thread
     */
    public static void readFile(Thread thread) {
        lock.readLock().lock();
        boolean readLock = lock.isWriteLocked();
        if (!readLock) {
            System.out.println("当前为读锁！");
            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(thread.getName() + ":正在进行读操作。。。");
                }
                System.out.println(thread.getName() + ":读操作完毕！");
            } finally {
                System.out.println("释放读锁！");
                lock.readLock().unlock();
            }
        }
    }

    /**
     * 写操作
     *
     * @param thread
     */
    public static void writeFile(Thread thread) {
        lock.writeLock().lock();
        boolean writeLock = lock.isWriteLocked();
        if (writeLock) {
            System.out.println("当前为写操作！");

            try {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(thread.getName() + ":正在进行写操作。。。");
                }
                System.out.println(thread.getName() + ":写操作完毕！");
            } finally {
                System.out.println("释放写锁！");
                lock.writeLock().unlock();
            }
        }
    }




    /**
     * 读写锁的实现必须确保写操作对读操作的内存影响，一个获得了读锁操作的线程必须能看到前一个写锁所更新的内容，读写锁之间为互斥
     *
     * @param args
     */
    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();

        service.execute(new Runnable() {

            @Override
            public void run() {
                readFile(Thread.currentThread());
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                writeFile(Thread.currentThread());
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                readFile(Thread.currentThread());
            }
        });
    }
}
