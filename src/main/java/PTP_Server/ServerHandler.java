package PTP_Server;
import java.io.*;
import java.net.Socket;

/**
 * @author Mrli
 * @date 2020/9/26 16:22
 */
public class ServerHandler implements Runnable{    //这里继承一个线程Runnable类
    private final Server server;
    private final Socket socket;

    /**
     * 构造器初始化server、socket
     * @param server
     * @param socket
     */
    public ServerHandler(Server server, Socket socket) {    //典型的构造函数
        this.server = server;
        this.socket = socket;
    }

    /**
     * 线程主要业务逻辑
     */
    @Override
    public void run() {         //线程的run方法
        try {
            //添加客户端socket
            server.addClient(socket);   //调用Server类里面的田间客户端信息的方法

            //获取客户端输入字符流
            BufferedReader reader = new BufferedReader(new InputStreamReader(   //获得Socket里面连接的
                    socket.getInputStream()
            ));

            //阻塞获取客户端的消息
            String msg = null;      //用String存储信息
            while ((msg = reader.readLine()) != null) {     //赋值msg为reader，当msg不为null，输出
                String fwdmsg = "客户端[" + socket.getPort() + "] : " + msg + "\n";
                System.out.println(fwdmsg);     //输出msg

                //转发消息给所有客户端
                server.forwardMsg(socket,fwdmsg);   //调用Server类里面的方法，让它传出去客户端

                //检测是否退出
                if(server.isQuit(msg)) {            //又是放进Server里的类调用方法
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

