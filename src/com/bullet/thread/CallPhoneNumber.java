package com.bullet.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CallPhoneNumber {

    public static void main(String[] args) {
        String[] arrNumberStrings = { "00000001", "00000002", "00000003", "00000004", "00000005", "00000006",
                "00000007", "00000008", "00000009", "00000010", "00000011", "00000012", "00000013", "00000014",
                "00000015", "00000016", "00000017", "00000018", "00000019", "00000020", "00000021", "00000022",
                "00000023", "00000024", "00000025", "00000026", "00000027", "00000028", "00000029", "00000030" };
        List<Number> numbers = new LinkedList<Number>();

        for (String str : arrNumberStrings) {
            numbers.add(new Number(str, NumberState.notDial));
        }

        new Phone(numbers, 3).run();
    }

}

class Number {
    private String phoneNumber;
    private NumberState numberState;
    private Lock lock = new ReentrantLock();

    public Number(String phoneNumber, NumberState numberState) {
        this.phoneNumber = phoneNumber;
        this.numberState = numberState;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public NumberState getPhoneNumberState() {
        return numberState;
    }

    public void setPhoneNumberState(NumberState numberState) {
        this.numberState = numberState;
    }

    public Lock getLock() {
        return lock;
    }
}

enum NumberState {
    notDial, finish;
}

class Phone {
    private List<Number> numbers;
    private int threadCount = 0;

    public Phone(List<Number> numbers, int threadCount) {
        this.numbers = numbers;
        this.threadCount = threadCount;
    }

    public void run() {
        System.out.println("Call program in processing...");

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new ThreadPhone(numbers);

            thread.start();
        }
    }
}

class ThreadPhone extends Thread {
    private List<Number> numbers;

    public ThreadPhone(List<Number> numbers) {
        this.numbers = numbers;
    }

    @Override
    public void run() {
        for (Number number : numbers) {
            if (number.getLock().tryLock()) {
                try {
                    if (number.getPhoneNumberState() == NumberState.notDial) {
                        System.out.println(Thread.currentThread() + " Calling -- " + number.getPhoneNumber());

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println(Thread.currentThread() + " Done    -- " + number.getPhoneNumber());

                        number.setPhoneNumberState(NumberState.finish);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    number.getLock().unlock();
                }
            }
        }
    }
}
