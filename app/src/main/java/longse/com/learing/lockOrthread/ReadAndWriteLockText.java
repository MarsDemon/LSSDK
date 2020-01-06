package longse.com.learing.lockOrthread;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteLockText {

    public static void get(Thread thread) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock().lock();
        System.out.println("start time:" + System.currentTimeMillis());
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread.getName() + ":正在进行读操作。。。");
        }
        System.out.println(thread.getName() + ":读操作完毕！");
        System.out.println("end time:" + System.currentTimeMillis());
        lock.readLock().unlock();
    }


    /**
     * 两个线程同时执行  ReentrantReadWriteLock 效率高于  synchronized
     * @param args
     */
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                get(Thread.currentThread());
            }
        }).start();

        Method[] methods = String.class.getMethods();
        for (Method m : methods) {
            System.out.println("ReadAndWriteLockText.main == " + m.getName());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                get(Thread.currentThread());
            }
        }).start();

    }
}

