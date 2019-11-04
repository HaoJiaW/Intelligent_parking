package com.jiawei.read.bestlibs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import sun.security.x509.IPAddressName;

public class ClassOne {

    public  static void main(String[] args){
       // System.out.println("hello world");
        try {
            DatagramSocket ds=new DatagramSocket(3333);
            byte[] bytes=new byte[1024];
            DatagramPacket dp=new DatagramPacket(bytes,bytes.length);

            while (true){
                ds.receive(dp);

                //解析数据
                InetAddress address=dp.getAddress();
                int port=dp.getPort();
                String content=new String(dp.getData(),0,dp.getLength());
                System.out.println("address:"+address+";port:"+port+",content:"+content+",len:"+dp.getLength());
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
                                                  