package mqChat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try(ZContext context = new ZContext(1)) {
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:" + args[0]);

            while(true) {
                Scanner s = new Scanner(System.in);
                String srt = s.nextLine();
                if(srt == null || srt.equals(""))
                    break;
                socket.send(srt);
                byte[] b = socket.recv();
                System.out.println(new String(b, ZMQ.CHARSET));
            }
            socket.close();
        }
    }
}
