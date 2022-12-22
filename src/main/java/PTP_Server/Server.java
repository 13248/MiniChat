package PTP_Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Mrli
 * @date 2020/9/26 16:20
 * 多人聊天室，服务端
 */
public class Server {
    private final int port = 6767;                   //设置端口号
    private final String QUIT = "quit";         //设置退出连接暗号

    private final ServerSocket serverSocket = null;   //设置服务端连接，初始值为null

    /**
     * 存放所有的客户端连接（socket、writer）
     */
    private final Map<Integer, Writer> clientList;    //用map容器，将客户端的端口信息、输出信息放进里面

    /**
     *构建线程池
     */
    private final ExecutorService executor;       //java的线程池

    /**
     * 构造初始化
     */
    public Server() {                       //构造函数Server
        this.clientList = new HashMap<>();      //设置this.HashMap<>
        this.executor = new ThreadPoolExecutor(10,10,0L,
                TimeUnit.MILLISECONDS,                                  //设置线程池10个，最大线程，平常线程0个（等开始自己自动创），
                new LinkedBlockingQueue<Runnable>());               //这里是BlockingQueue<Runnable>线程容器存线程
    }

    /**
     * 添加客户端
     * @param socket
     */
    public synchronized void addClient(Socket socket) throws IOException {   //这个是保存连接的，add客户端进HashMap容器
        if(socket != null) {                //如果有链接请求
            int key = socket.getPort();     //就给他保存端口号
            Writer writer = new BufferedWriter(new OutputStreamWriter(    //Writer类型保存连接时的输出流
                    socket.getOutputStream()
            ));

            clientList.put(key,writer);                                 //保存端口号和输出的输出流，保存到那个HashMap容器里
            System.out.println("客户端["+port + "]：已连接！");             //输出端口号（只在客户端输出）
        }
    }

    /**
     * 删除客户端
     * @param socket
     */
    public synchronized void removeClient(Socket socket) throws IOException {  //在HashMap里面删除掉客户端的这个信息
        if(socket != null) {                    //如果有连接请求
            int key = socket.getPort();         //同上，获得端口值
            clientList.get(key).close();        //关闭上面这个流
            clientList.remove(key);             //然后在那个容器里面删除掉这个字典
            System.out.println("客户端["+port + "]：已断开连接！");       //打印出这个端口号
        }
    }

    /**
     * 将消息转发给所有客户端
     * @param socket
     * @param msg
     */
    public synchronized void forwardMsg(Socket socket, String msg) throws IOException {   //转发信息的函数
        for (Integer key : clientList.keySet()) {           //用int属性的key遍历HashMap里面的key值（keyset（）用的是反射映射，反射HashMap里面的key值）
            if(!key.equals(socket.getPort())) {             //如果key的值里面！不！等于连接的过来的端口值就运行
                Writer writer = clientList.get(key);        //拿出Writer类型的保存那个对应的输出流
                writer.write(msg);                          //向各个输出流输出msg
                writer.flush();                             //刷新缓存区，相当于结束上面的东西
            }
        }
    }

    /**
     * 判断用户消息是否为quit指令
     * @param msg
     * @return
     */
    public synchronized boolean isQuit(String msg) {        //锁线程，然后询问是否关闭连接的东西
        return QUIT.equals(msg);                //返回是否quit的布尔值
    }

    /**
     * 关闭serverSocket
     */
    public synchronized void close(){           //把这个线程锁住，然后进行关闭操作
        if(serverSocket != null) {              //判断服务器的Socket是否存在
            try {
                serverSocket.close();           //把ServerSocket给关了
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {                   //开始线程的东西
        try {
            ServerSocket serverSocket = new ServerSocket(port); //创建一个服务器Socket
            System.out.println("服务器启动，正在监听端口：" + port);     //输出服务器启动的信息

            while (true) {
                //监听客户端连接
                Socket socket = serverSocket.accept();      //接受Socket信息

                //创建线程处理
                //new Thread(new ChartHandler(this,socket)).start();
                executor.execute(new ServerHandler(this,socket));   //线程池放入一个线程，来自另一个ServerHandler（）的，this是指当前的Server。
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();    //运行完就关了
        }
    }

    /**
     * 通过主线程启动server
     * @param args
     */
    public static void main(String[] args) {   //这里的Server类没开多线程，多线程在里面的ServerHandler方法里面
        Server server = new Server();       //主程序运行，创建一个Server类
        server.start();                     //然后start，开这个线程，运行里面的start（）方法
    }
}


