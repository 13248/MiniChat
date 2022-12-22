package Thread;

import java.io.*;
import java.net.Socket;

public class SThread extends Thread{
    private final Socket socket;
    private final int countNum;
    public SThread(Socket socket,int countNum) {
        this.socket=socket;
        this.countNum=countNum;
    }

    @Override
    public void run() {
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String msg;
            while((msg= br.readLine())!=null){

                System.out.println("客户端"+countNum+"："+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
