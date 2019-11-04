package com.github.jiawei.intelligent_parking_system.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class FtpHelper {

    private FTPClient client;
    private Activity activity;
    private InetAddress inetAddress;
    private InetAddress localAddress;
    private NetworkInfo networkInfo;

    private Network network;

    public String getFtpFileName() {
        return ftpFileName;
    }

    public void setFtpFileName(String ftpFileName) {
        this.ftpFileName = ftpFileName;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    private String ftpFileName;
    private String localFilePath;

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Network getNetwork() {
        return network;
    }

    public FtpHelper(Activity activity){
        this.activity=activity;
    }

    public void setInetAddress(InetAddress inetAddress){
        this.inetAddress=inetAddress;
    }


    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            client=new FTPClient();
            try {
                if (getNetwork()!=null) {
                    ConnectivityManager connectivityManager= (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                    networkInfo=connectivityManager.getActiveNetworkInfo();
                    networkInfo.isConnected();

                    System.out.println("connect state:"+networkInfo.isConnected());
                    if (!networkInfo.isConnected()){
                        return;
                    }

                    client.setSocketFactory(network.getSocketFactory());
                    client.connect(network.getByName("ftp.boelink.com"),3000);

                }else {
                    client.connect("ftp.boelink.com",3000);
                }

                client.setConnectTimeout(10000);

                client.login("boelink","boelink123");

                if (!FTPReply.isPositiveCompletion(client.getReplyCode())){
                    System.out.println("connect failed");
                    client.disconnect();
                    return;
                }

                //  client.makeDirectory("/share/test/");
                client.changeWorkingDirectory("/share/test");

                client.setBufferSize(1024);
                client.setControlEncoding("UTF-8");
                client.setFileType(FTP.BINARY_FILE_TYPE);

                File localFile=new File(getLocalFilePath());
                FileInputStream inputStream=new FileInputStream(localFile);
                client.enterLocalPassiveMode();
                System.out.println("ftpFileName:"+getFtpFileName());
                boolean storeSuccess=client.storeFile(getFtpFileName(),inputStream);

                if (storeSuccess){
                    client.rename(getFtpFileName(),getFtpFileName()+".dat");

                    inputStream.close();
                    client.disconnect();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"文件上传成功！",Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"上传失败！",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });


    public void storeFile(final Network network, final String ftpFileName, final String localFilePath){
        setNetwork(network);
        setFtpFileName(ftpFileName);
        setLocalFilePath(localFilePath);
        thread.start();
    }


    public void rename(final Network network, final String from, final String to){
        new Thread(new Runnable() {
            @Override
            public void run() {
                client=new FTPClient();
                client.setSocketFactory(network.getSocketFactory());
                try {
                    client.connect(network.getByName("ftp.boelink.com"),3000);
                    client.login("boelink","boelink123");
                    client.changeWorkingDirectory("/share/test/");
                    boolean renameSuccess=client.rename(from,to);
                    if (renameSuccess){
                        client.disconnect();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"更名成功！",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"更名失败！",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
