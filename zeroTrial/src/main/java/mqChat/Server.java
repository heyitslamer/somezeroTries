package mqChat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Server {

    public static void main(String[] args) {
        try( ZContext context = new ZContext(1) ) {
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            for(String s: args) {
                socket.bind("tcp://*:" + s);
            }
            while(true) {
                byte[] b = socket.recv();
                String s = new String(b);

                System.out.println("Received: " + s);
                socket.send(s, 0);
            }
        }
    }
}
