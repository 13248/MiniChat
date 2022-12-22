package Thread;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadClient {
    public static void main(String[] args) {
        try {
            Socket socket=new Socket("127.0.0.1",9999);
            PrintStream ps=new PrintStream(socket.getOutputStream());
            Scanner sc=new Scanner(System.in);


            while(true){
                System.out.print("输入：");
                String msg=sc.nextLine();
                ps.println(msg);
                ps.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
