import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server{
    public static void main(String[]args){

        System.out.println("***服务器启动***");
        try {
            ServerSocket ss=new ServerSocket(9999);
            Socket socket=ss.accept();
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String msg;
            while((msg= br.readLine())!=null){
                System.out.println("服务器收到的信息："+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
