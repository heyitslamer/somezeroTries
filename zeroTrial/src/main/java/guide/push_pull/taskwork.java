package guide.push_pull;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class taskwork {
    public static void main(String[] args) {

        for(int i = 0; i < 2; i++) {
            new Thread(() -> {
                try (ZContext context = new ZContext()) {
                    ZMQ.Socket receiver = context.createSocket(SocketType.PULL);
                    receiver.connect("tcp://localhost:5557");

                    ZMQ.Socket sender = context.createSocket(SocketType.PUSH);
                    sender.connect("tcp://localhost:5558");

                    while (!Thread.currentThread().isInterrupted()) {
                        String string = new String(receiver.recv(0),
                                ZMQ.CHARSET).trim();
                        long msec = Long.parseLong(string);

                        System.out.flush();
                        System.out.print(string + '.');

                        try {
                            Thread.sleep(msec);
                        } catch (Exception e) { e.printStackTrace(); }

                        sender.send(ZMQ.MESSAGE_SEPARATOR, 0);
                    }
                }
            }).start();
        }
    }
}
