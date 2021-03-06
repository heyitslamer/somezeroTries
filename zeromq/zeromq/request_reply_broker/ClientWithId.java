package request_reply_broker;
import org.zeromq.ZMQ;

public class ClientWithId {
  public static void main(String[] args) {
    ZMQ.Context context = ZMQ.context(1);
    ZMQ.Socket socket = context.socket(ZMQ.REQ);
    socket.setIdentity(args[1].getBytes());
    socket.connect("tcp://localhost:"+args[0]);
    while (true) {
      String s = System.console().readLine();
      if (s == null) break;
      socket.send(s);
      byte[] b = socket.recv();
      System.out.println(new String(b));
    }
    socket.close();
    context.term();
  }
}

