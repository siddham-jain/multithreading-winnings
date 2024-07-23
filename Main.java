public class Main{
    public static void main(String[] args){
        // System.out.println("Hello World");
        HelloWorldPrinter t1 = new HelloWorldPrinter();
        t1.start();
        HelloWorldPrinter t2 = new HelloWorldPrinter();
        t2.start();
    }
}

class HelloWorldPrinter extends Thread{
    @Override
    public void run(){
        System.out.println("Hello World " + Thread.currentThread().getName());
    }
}