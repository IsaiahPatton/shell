package me.isaiah.shell;

public class Multithreading {
    
    public static void run(Runnable r) {
        new Thread(r).start();
    }

}
