package com.github.jiawei.intelligent_parking_system.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.activity.Camera2Activity;
import com.github.jiawei.intelligent_parking_system.event.CaptureEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetWorkHelper {

    public static String dstIp=null;
    public static String srcIP;
    private static final int port=10002;

    private ServerSocket server;
    public static Socket srcClient;

    private Camera2Activity camera2Activity;

    public static OutputStream outputStream;



    public NetWorkHelper(){

    }

    public static void sendUdpBytes(byte[] message) {
        Log.i("TAG","start send bytes..."+"bytes.length():"+message.length);
        try {
            InetAddress address = InetAddress.getByName(dstIp);            // 根据主机名称得到IP地址
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);            // 用数据和地址创建数据报文包
            DatagramSocket dsocket = new DatagramSocket();            // 创建数据报文套接字并通过它传送
            dsocket.send(packet);
            Log.i("TAG","start send bytes finish!");
            dsocket.close();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void startServer() throws IOException {
        System.out.println("Server Start...");
        ServerSocket server=new ServerSocket(port);
        while (true){
            if (srcClient==null){
                System.out.println("init srcClient!");
                Socket socket=server.accept();
                srcClient=socket;
            }

            byte[] bytes=new byte[1024];
            InputStream inputStream=srcClient.getInputStream();
            int len=inputStream.read(bytes);

            String command=new String(bytes,0,len);

            Log.i("TCP","receive command:"+command+".");

            if (command.equals("capture")){
                System.out.println("Capture Start...");
                EventBus.getDefault().post(new CaptureEvent());
            }
        }

    }

    public static  void send(byte[] message){
        if (srcClient ==null){
            System.out.println("client is null!");
            return;
        }

        try {
            outputStream=srcClient.getOutputStream();
            outputStream.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void tcpSend(byte[] message) throws IOException {
        //1.创建客户端
        Socket client=new Socket();

       // 2.与服务器端建立连接
        if (srcIP==null){
             System.out.println("srcIp is null!");
             return;
        }



        InetSocketAddress address=new InetSocketAddress(srcIP,port);
        client.connect(address);


        //获取输出流
        OutputStream outputStream=client.getOutputStream();
        //将文件写入服务端
        outputStream.write(message,0,message.length);
        //通知服务器数据写入完毕
        client.shutdownOutput();

        //读取服务端的返回信息
        //InputStream inputStream=client.getInputStream();
        //BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        //String line=reader.readLine();

        //Toast.makeText(activity,line,Toast.LENGTH_LONG).show();
    }

}
