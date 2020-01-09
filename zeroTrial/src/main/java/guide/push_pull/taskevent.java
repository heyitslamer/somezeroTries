package guide.push_pull;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Random;

public class taskevent {
    public static void main(String[] args) throws Exception {
        try(ZContext context = new ZContext()) {
            ZMQ.Socket sender = context.createSocket(SocketType.PUSH);
            sender.bind("tcp://*:5557");

            ZMQ.Socket sink = context.createSocket(SocketType.REP.PUSH);
            sink.connect("tcp://localhost:5558");

            System.out.println("Press Enter to Start");

            System.in.read();

            System.out.println("Sending task to workers");

            sink.send("0",0);

            Random srandom = new Random(System.currentTimeMillis());

            int task_nbr;
            int total_msec = 0;
            for(task_nbr = 0; task_nbr < 100; task_nbr++) {
                int workload;

                workload = srandom.nextInt(100) + 1;
                total_msec += workload;
                System.out.println(workload + ".");
                String string = String.format("%d", workload);
                sender.send(string, 0);
            }

            System.out.println("Total cost: " + total_msec + "msec");
            Thread.sleep(1000);
        }
    }
}
