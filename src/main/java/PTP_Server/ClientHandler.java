package PTP_Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Mrli
 * @date 2020/9/26 16:22
 */
public class ClientHandler implements Runnable{
    private final Client client;

    /**
     * 初始化构造函数
     * @param client
     */
    public ClientHandler(Client client) {
        this.client = client;
    }

    /**
     * 主要监听用户输入状态
     */
    @Override
    public void run() {
        //获取本地输入流，监听用户输入
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String msg = reader.readLine();
                if(msg != null) {
                    client.sendMsg(msg);
                }

                if(client.isQuit(msg)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
