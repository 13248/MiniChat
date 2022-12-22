package Thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer {
    public static void main(String[] args) {
        System.out.println("*** ThreadServer is starting! ***");
        ServerSocket ss;
        int countNum=0;
        {
            try {
                ss = new ServerSocket(9999);
                while(true){
                    countNum++;
                    Socket socket= ss.accept();
                    new SThread(socket,countNum).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
