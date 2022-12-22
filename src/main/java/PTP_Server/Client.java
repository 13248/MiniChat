package PTP_Server;
import java.io.*;
import java.net.Socket;

/**
 * @author Mrli
 * @date 2020/9/26 16:20
 * 多人聊天室客户端
 */
public class Client {
    private final String host = "127.0.0.1";    //设置ip
    private final int port = 6767;              //设置端口值
    private final String QUIT = "quit";         //设置退出值

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * 默认构造器
     */
    public Client() {
                            //客户端构造方法（空的）
    }

    /**
     * 给服务端发送消息
     * @param msg
     */
    public void sendMsg(String msg) throws IOException {        //输出msg
        //socket的输出流未关闭
        if(!socket.isOutputShutdown()) {        //查看msg是否关闭，如果输出流还没关
            writer.write(msg+"\n");         //就写出来
            writer.flush();                     //清理缓存
        }
    }

    /**
     * 接收服务端消息
     */
    public String receive() throws IOException {    //接收方法
        String msg = null;

        //socket的输入流未关闭
        if(!socket.isInputShutdown()) {     //判断输入流是否关闭，还没关就读msg
            msg = reader.readLine();
        }
        return msg;     //然后返回msg
    }

    /**
     * 检测是否退出
     * @param msg
     * @return
     */
    public boolean isQuit(String msg) {     //退出连接的函数
        return QUIT.equals(msg);
    }

    /**
     * 关闭socket、输出流
     */
    public void close() {       //关闭的函数
        try {
            writer.close();     //输出关闭
            socket.close();     //连接关闭
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多人聊天室客户端启动
     */
    public void start() {
        try {
            //创建socket，与serverSocket简历连接
            socket = new Socket(host,port);     //创建和客户端的Socket连接

            //创建IO流
            reader = new BufferedReader(new InputStreamReader(  //创建输入流
                    socket.getInputStream()
            ));
            writer = new BufferedWriter(new OutputStreamWriter(    //创捷输出流
                    socket.getOutputStream()
            ));

            //创建线程处理,
            new Thread(new ClientHandler(this)).start();    //创建客户端线程启动

            //监听服务器转发的消息
            String msg = null;
            while ((msg = receive()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    /**
     * 通过Client主线程启动客户端
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}