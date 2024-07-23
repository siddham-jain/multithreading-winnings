public class Main{
    public static void main(String[] args){
        // System.out.println("Hello World");
        HelloWorldPrinter t1 = new HelloWorldPrinter();
        // t1.start();
        HelloWorldPrinter t2 = new HelloWorldPrinter();
        // t2.start();

        NumberPrinter n1 = new NumberPrinter();
        n1.start();
        NumberPrinter n2 = new NumberPrinter();
        n2.start();
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

class HelloWorldPrinter extends Thread{
    @Override
    public void run(){
        System.out.println("Hello World " + Thread.currentThread().getName());
    }
}