package mqChat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZSocket;

import java.time.ZonedDateTime;
import java.util.Scanner;

class ClientThreadWrite extends Thread {

    ZMQ.Socket push;

    ClientThreadWrite(ZContext context, int push) {
        this.push = context.createSocket(SocketType.PUSH);
        this.push.connect("tcp://localhost:"+push);
    }

    public void run() {

        Scanner sc = new Scanner(System.in);

        while(!Thread.currentThread().isInterrupted()) {
            String msg = sc.nextLine().trim();
            push.send(msg, 0);
        }
    }
}

class ClientThreadRead extends Thread {

    ZMQ.Socket sub;

    ClientThreadRead(ZContext context, int sub) {
        this.sub = context.createSocket(SocketType.SUB);
        this.sub.connect("tcp://localhost:"+sub);
        this.sub.subscribe("".getBytes());
    }

    public void run() {

        Scanner sc = new Scanner(System.in);

        while(!Thread.currentThread().isInterrupted()) {
            String msg = sub.recvStr(0);
            System.out.println(msg);
        }
    }
}

public class Client {
    public static void main(String[] args) {
        ZContext context = new ZContext(1);
        new ClientThreadRead(context, 5000).start();
        new ClientThreadWrite(context, 5001).start();
    }
}
