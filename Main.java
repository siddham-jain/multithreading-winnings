public class Main{
    public static void main(String[] args){
        // System.out.println("Hello World");
        HelloWorldPrinter t1 = new HelloWorldPrinter();
        // t1.start();
        HelloWorldPrinter t2 = new HelloWorldPrinter();
        // t2.start();

        NumberPrinter n1 = new NumberPrinter();
        // n1.start();
        NumberPrinter n2 = new NumberPrinter();
        // n2.start();

        for(int i = 1; i<=100; i++){
            DigitPrinter d = new DigitPrinter(i);
            // d.start();
            SingleNumberPrinter s = new SingleNumberPrinter(i);
            Thread th = new Thread(s);
            th.start();
        }
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