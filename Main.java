import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main{
    public static void main(String[] args) throws Exception{
        // System.out.println("Hello World");
        HelloWorldPrinter t1 = new HelloWorldPrinter();
        // t1.start();
        HelloWorldPrinter t2 = new HelloWorldPrinter();
        // t2.start();

        NumberPrinter n1 = new NumberPrinter();
        // n1.start();
        NumberPrinter n2 = new NumberPrinter();
        // n2.start();

        // for(int i = 1; i<=100; i++){
        //     DigitPrinter d = new DigitPrinter(i);
        //     // d.start();
        //     SingleNumberPrinter s = new SingleNumberPrinter(i);
        //     Thread th = new Thread(s);
        //     th.start();
        // }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        // for(int i = 1; i<=100; i++){
        //     SingleNumberPrinter s = new SingleNumberPrinter(i);
        //     executor.execute(s);
        // }

        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int i = 0; i<10; i++){
            list.add(i);
        }

        ArrayListModifier am = new ArrayListModifier(list);

        Future<ArrayList<Integer>> doubledList = executor.submit(am);
        try {
            System.out.println(doubledList.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}

class ArrayListModifier implements Callable<ArrayList<Integer>>{
    ArrayList<Integer> list;

    public ArrayListModifier(ArrayList<Integer> list){
        this.list = list;
    }

    @Override
    public ArrayList<Integer> call(){
        ArrayList<Integer> doubledList = new ArrayList<Integer>();
        for(int i = 0; i < list.size(); i++){
            doubledList.add(this.list.get(i) * 2);
        }
        return doubledList;
    }
}

class SingleNumberPrinter implements Runnable{
    public int n;
    public SingleNumberPrinter(int n){
        this.n = n;
    }
    @Override
    public void run(){
        System.out.println(n + " " + Thread.currentThread().getName());
    }
}

class NumberPrinter extends Thread{
    @Override
    public void run(){
        for(int i = 0; i < 10; i++){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(i + " " + Thread.currentThread().getName());
        }
    }
}

class DigitPrinter extends Thread{
    public int n;
    public DigitPrinter(int n){
        this.n = n;
    }
    @Override
    public void run(){
        System.out.println(n + " " + Thread.currentThread().getName());
    }
}

class HelloWorldPrinter extends Thread{
    @Override
    public void run(){
        System.out.println("Hello World " + Thread.currentThread().getName());
    }
}