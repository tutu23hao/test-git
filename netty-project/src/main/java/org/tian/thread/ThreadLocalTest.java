package org.tian.thread;

/**
 * @author ：
 */
public class ThreadLocalTest {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                threadLocal.set(Thread.currentThread().getName());


                System.out.println(Thread.currentThread().getName() + "====>>>" + threadLocal.get() + "输出内容");

            }
        };


        new Thread(task, "线程1").start();
        new Thread(task, "线程2").start();


        System.out.println("main");


    }
}
