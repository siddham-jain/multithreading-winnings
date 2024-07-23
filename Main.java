import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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

        ExecutorService executor = Executors.newCachedThreadPool();
        // for(int i = 1; i<=100; i++){
        //     SingleNumberPrinter s = new SingleNumberPrinter(i);
        //     executor.execute(s);
        // }

        ArrayList<Integer> list = new ArrayList<Integer>();

        for(int i = 10; i>0; i--){
            list.add(i);
        }

        // ArrayListModifier am = new ArrayListModifier(list);

        // Future<ArrayList<Integer>> doubledList = executor.submit(am);
        // try {
        //     System.out.println(doubledList.get());
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        // Sorter sorter = new Sorter(list);
        // Future<ArrayList<Integer>> sortedList = executor.submit(sorter);

        // try {
        //     System.out.println(sortedList.get());
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        Value v = new Value(0);
        
        Adder adder = new Adder(v);
        Subtractor subtractor = new Subtractor(v);
        Future<Void> adderFuture = executor.submit(adder);
        Future<Void> subtractorFuture = executor.submit(subtractor);
        adderFuture.get();
        subtractorFuture.get();
        System.out.println(v.val);

        v.val = 0;

        Lock l = new ReentrantLock();
        AdderWithLock awl = new AdderWithLock(v, l);
        SubtractorWithLock swl = new SubtractorWithLock(v, l);
        Future<Void> awlFuture = executor.submit(awl);
        Future<Void> swlFuture = executor.submit(swl);
        awlFuture.get();
        swlFuture.get();
        System.out.println(v.val);

        v.val = 0;

        SyncronizedAdder sa = new SyncronizedAdder(v);
        SyncronizedSubtractor ss = new SyncronizedSubtractor(v);
        Future<Void> saFuture = executor.submit(sa);
        Future<Void> ssFuture = executor.submit(ss);
        saFuture.get();
        ssFuture.get();
        System.out.println(v.val);
        executor.shutdown();
    }
}

class Value {
    int val;
    public Value(int val) {
        this.val = val;
    }
}

class Adder implements Callable<Void> {
    Value v;
    public Adder(Value v) {
        this.v = v;
    }

    @Override
    public Void call() {
        for(int i = 1; i < 10000; i++) {
            v.val += i;
        }
        return null;
    }
}

class Subtractor implements Callable<Void> {
    Value v;
    public Subtractor(Value v) {
        this.v = v;
    }

    @Override
    public Void call() {
        for(int i = 1; i < 10000; i++) {
            v.val -= i;
        }
        return null;
    }
}

class AdderWithLock implements Callable<Void> {
    Value v;
    Lock l;
    public AdderWithLock(Value v, Lock l) {
        this.v = v;
        this.l = l;
    }

    @Override
    public Void call() {
        for(int i = 1; i < 10000; i++) {
            l.lock();
            v.val += i;
            l.unlock();
        }
        return null;
    }
}

class SubtractorWithLock implements Callable<Void> {
    Value v;
    Lock l;
    public SubtractorWithLock(Value v, Lock l) {
        this.v = v;
        this.l = l;
    }

    @Override
    public Void call() {
        for(int i = 1; i < 10000; i++) {
            l.lock();
            v.val -= i;
            l.unlock();
        }
        return null;
    }
}

class SyncronizedAdder implements Callable<Void> {
    Value v;
    public SyncronizedAdder(Value v) { // Fix the constructor name
        this.v = v;
    }

    @Override
    public Void call() {
        for (int i = 1; i < 10000; i++) {
            synchronized (v) { // Fix the synchronized keyword
                this.v.val += i;
            }
        }
        return null;
    }
}

class SyncronizedSubtractor implements Callable<Void> {
    Value v;
    public SyncronizedSubtractor(Value v) { // Fix the constructor name
        this.v = v;
    }

    @Override
    public Void call() {
        for (int i = 1; i < 10000; i++) {
            synchronized (v) { // Fix the synchronized keyword
                this.v.val -= i;
            }
        }
        return null;
    }
}


class Sorter implements Callable<ArrayList<Integer>>{
    ArrayList<Integer> listToSort;

    public Sorter(ArrayList<Integer> list){
        this.listToSort = list;
    }

    @Override
    public ArrayList<Integer> call(){
        if (listToSort.size() <= 1){
            return listToSort;
        }

        int mid = listToSort.size() / 2;

        ArrayList<Integer> left = new ArrayList<Integer>();
        left = getSubList(listToSort, 0, mid-1);

        ArrayList<Integer> right = new ArrayList<Integer>();
        right = getSubList(listToSort, mid, listToSort.size()-1);

        // ExecutorService to sort two halves in parallel
        // ExecutorService executor = Executors.newFixedThreadPool(5); it creates only 5 threads

        ExecutorService executor = Executors.newCachedThreadPool(); // new cached thread pool will create new threads if needed

        // creating new tasks
        Sorter leftSorter = new Sorter(left);
        Sorter rightSorter = new Sorter(right);

        Future<ArrayList<Integer>> leftSorterFuture = executor.submit(leftSorter);
        Future<ArrayList<Integer>> rightSorterFuture = executor.submit(rightSorter);

        ArrayList<Integer> sortedLeft = new ArrayList<Integer>();
        ArrayList<Integer> sortedRight = new ArrayList<Integer>();

        try {
            sortedLeft = leftSorterFuture.get();
            sortedRight = rightSorterFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return merge(sortedLeft, sortedRight);
    }

    // function to get sublist
    public ArrayList<Integer> getSubList(ArrayList<Integer> list, int start, int end){

        System.out.println("getting sublist " + Thread.currentThread().getName());

        ArrayList<Integer> subList = new ArrayList<Integer>();
        for(int i = start; i <= end; i++){
            subList.add(list.get(i));
        }
        return subList;
    }

    // function to merge two sorted lists   
    public ArrayList<Integer> merge(ArrayList<Integer> left, ArrayList<Integer> right){

        System.out.println("merging " + Thread.currentThread().getName());

        ArrayList<Integer> mergedList = new ArrayList<Integer>();
        int leftIndex = 0;
        int rightIndex = 0;

        while(leftIndex < left.size() && rightIndex < right.size()){
            if(left.get(leftIndex) < right.get(rightIndex)){
                mergedList.add(left.get(leftIndex));
                leftIndex++;
            }else{
                mergedList.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        while(leftIndex < left.size()){
            mergedList.add(left.get(leftIndex));
            leftIndex++;
        }

        while(rightIndex < right.size()){
            mergedList.add(right.get(rightIndex));
            rightIndex++;
        }

        return mergedList;
    }

    // function to sort the list
    public ArrayList<Integer> sort(ArrayList<Integer> list){

        System.out.println("sorting " + Thread.currentThread().getName());

        if(list.size() <= 1){
            return list;
        }

        int mid = list.size() / 2;

        ArrayList<Integer> left = new ArrayList<Integer>();
        left = getSubList(list, 0, mid-1);

        ArrayList<Integer> right = new ArrayList<Integer>();
        right = getSubList(list, mid, list.size()-1);

        left = sort(left);
        right = sort(right);

        return merge(left, right);
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