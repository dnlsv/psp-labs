package Lab;
import java.net.*;
import java.io.*;
import java.util.*;

public class UDPServer {
    public final static int DEFAULT_PORT = 8001;

    public void runServer() throws IOException {
        DatagramSocket s = null;
        try {
            boolean stopFlag = false;
            byte[] buf = new byte[512];
            s = new DatagramSocket(DEFAULT_PORT);
            System.out.println("UDPServer: Started on " + s.getLocalAddress() + ":" + s.getLocalPort());
            DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
            s.receive(recvPacket);
            String cmd = new String(recvPacket.getData()).trim();
            DatagramPacket sendPacket = new DatagramPacket(buf, 0, recvPacket.getAddress(), recvPacket.getPort());                                                                 // дейтаграммы для отсылки данных
            int n = 0, count =0, x = 0, y = 0, z = 0;
            try (Scanner ss = new Scanner(cmd)) {
                while (ss.hasNextInt()) {
                    if (count == 0) {
                        x = ss.nextInt();
                        count++;
                    }
                    if (count == 1) {
                        y = ss.nextInt();
                        count++;
                    }
                    if (count == 2)
                        z = ss.nextInt();
                }
            }
            System.out.println("UDPServer: x = " + x + ", y = " + y + ", z = " + z);
            double res = 0;
            res = Math.pow(y, x) + Math.sqrt(Math.abs(x) + Math.pow(Math.E, y))- Math.pow(z,3)*Math.pow(Math.sin(y), 2)/(y + Math.pow(z,3)/(y-Math.pow(z,3)));
            String result = Double.toString(res);
            byte []resB;
            resB = result.getBytes();
            n = resB.length;
            System.arraycopy(resB, 0, buf, 0, n);
            sendPacket.setData(buf);
            sendPacket.setLength(n);
            s.send(sendPacket);

            File file = new File("textFile");
            PrintWriter pw = new PrintWriter(file);
            pw.println("x = " + x + ", y = " + y + ", z = " + z);
            pw.println("Function value = " + res);
            pw.close();

            System.out.println("UDPServer: Stopped");
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
            UDPServer udpSvr = new UDPServer();
            udpSvr.runServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
