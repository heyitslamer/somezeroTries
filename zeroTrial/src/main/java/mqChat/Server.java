package mqChat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

class ServerThread extends Thread {

    ZMQ.Socket publisher;
    ZMQ.Socket puller;

    ServerThread(ZContext zc, int pub, int pull) {

        this.publisher = zc.createSocket(SocketType.PUB);
        this.publisher.bind("tcp://localhost:"+pub);

        this.puller = zc.createSocket(SocketType.PULL);
        this.puller.bind("tcp://localhost:"+pull);

    }

    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            String message = puller.recvStr(0);
            System.out.println("[Server] Received: " + message);
            publisher.send(message, 0);
        }
        publisher.close();
        puller.close();
    }
}


public class Server {

    public static void main(String[] args) {
        ZContext context = new ZContext(1);
        new ServerThread(context, 5000, 5001).start();
    }
}
