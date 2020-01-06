package longse.com.learing.knowledgepoint;

public class RxjavaKnowledge {

    /**
     * observable.subscribe(subscriber);
     *
     * RxJava 有两个方法可以很方便的指定观察者和被观察者的代码运行线程，RxAndroid 还有一扩展，可以指定在 UI 线程。
     *
     *     //设置观察者和发布者代码所要运行的线程后注册观察者
     *     observable.subscribeOn(Schedulers.immediate())//在当前线程执行subscribe()方法
     *     .observeOn(AndroidSchedulers.mainThread())//在UI线程执行观察者的方法
     *     .subscribe(subscriber);
     *
     *     通过Scheduler作为参数来指定代码运行的线程，非常方便，好用到不行…其他常用的参数还有：
     * 　　Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。　
     * 　　Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * 　　Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
     *     区别在于 io() 的内部实现是是用一个无数量上限的线程池，
     *     可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免建不必要的线程。
     */


    /**
     * Observable 主要创建方式：
     * 1. Observable.create();
     * 2. Observable.from(); from 方法创建，可以传入一个数组或者继承 Interable 的类的对象作为参数。当Observable 发射数据时，他会依次靶序列中的元素发射出来。
     * 3. Observable.just(); just 直接接收Object 作为参数，直接发射出来。
     * 4. Observable.timer(); timer 有定时作用，延时发送一个值。
     * 5. Observable.range(3,7).repeat(2); range 发射从 n 到 m 的整数序列，可以指定 Scheduler 设置执行方法； repeat 方法可以指定重复的次数。
     */

}
