package start;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Raul on 07/05/2018.
 */
public class FutureTaskExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        System.out.println("merge");
            Callable<Integer> eval = new Callable<Integer>() {
                public Integer call() throws InterruptedException {
                    System.out.println("Inside callable " + Thread.currentThread().getName());
                    int sum = 0;
                    for(int i = 0; i < 100; i++) {
                        sum = sum + i;
                    }
                    return sum;

                }
            };
            FutureTask<Integer> ft = new FutureTask<Integer>(eval);
            executorService.submit(ft);
            //ft.run();

            System.out.println("Outside callable " + Thread.currentThread().getName());


    }
}
