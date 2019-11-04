package com.github.jiawei.intelligent_parking_system;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.helper.FtpHelper;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.telnet.TelnetOption;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.stream.IntStream;

import javax.net.SocketFactory;

public class MobileActivity extends AppCompatActivity {


    private Button btnWithMobile, btn,renameBtn;
    private TextView tv;
    private TextView tv1;
    private LinearLayout progressBar;
    private EditText ftpFileName;

    private FtpHelper ftpHelper;

    private ConnectivityManager connectivityManager;
    private NetworkRequest.Builder builder;
    private NetworkRequest build;

    private String ftpFileNameText;
    private Network mobileNetwork = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        ftpHelper = new FtpHelper(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        initView();

        initNetWork();
    }


    private void initView() {

        btnWithMobile = findViewById(R.id.btnWithMobile);
        renameBtn = findViewById(R.id.renameBtn);
        btn = findViewById(R.id.btn);
        progressBar = findViewById(R.id.progressBar);
        ftpFileName = findViewById(R.id.ftpFileName);
//        tv=findViewById(R.id.tv);
//        tv1=findViewById(R.id.tv1);
        ftpFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ftpFileNameText=ftpFileName.getText().toString();
            }
        });

        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftpHelper.rename(mobileNetwork,"error.txt","hello.txt");
            }
        });


        btnWithMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //serachInternet();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                InetAddress inetAddress= InetAddress.getByName("ftp.boelink.com");
//                                System.out.println("inetAddress:"+inetAddress);
//                            } catch (UnknownHostException e) {
//                                System.out.println("unknowen host exception:"+e);
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();

                if (ftpFileNameText==null){
                    return;
                }


                if (mobileNetwork != null) {
                    String localPath = Environment.getExternalStorageDirectory() + "/.boelink/error/errorlog.txt";
                    String ftpFileName = "error.txt";
                    ftpHelper.storeFile(mobileNetwork, ftpFileNameText, localPath);
                }

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  serachInternet1();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ftpHelper.setInetAddress(InetAddress.getByName("ftp.boelink.com"));
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



                String localPath = Environment.getExternalStorageDirectory() + "/.boelink/error/errorlog.txt";
                String ftpFileName = "error.txt";
                ftpHelper.storeFile(null, ftpFileName, localPath);
            }
        });
    }

    private void initNetWork() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        build = builder.build();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                mobileNetwork = network;
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void serachInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        NetworkRequest build = builder.build();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(final Network network) {
                super.onAvailable(network);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SocketFactory socketFactory = network.getSocketFactory();

                        FTPClient client = new FTPClient();
                        client.setSocketFactory(socketFactory);
                        try {
                            client.connect("192.168.0.3", 21);
                            int reply = client.getReplyCode();
                            if (FTPReply.isPositiveCompletion(reply)) {
                                Toast.makeText(MobileActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                            } else {
                                client.disconnect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String fileName = Environment.getExternalStorageDirectory() + "/.cc" + "/.adfwe.dat";
                        File file = new File(fileName);


                        Log.i("TBG", "1");

//                        HttpURLConnection connection=null;
//                        BufferedReader reader=null;
//                        try {
//                            URL url=new URL("https://www.baidu.com");
//                            connection= (HttpURLConnection) network.openConnection(url);
//                            //connection= (HttpURLConnection) url.openConnection();
//                            connection.setRequestMethod("GET");
//                            connection.setConnectTimeout(8000);
//                            connection.setReadTimeout(8000);
//                            InputStream in= connection.getInputStream();
//
//                            reader=new BufferedReader(new InputStreamReader(in));
//                            final StringBuilder response=new StringBuilder();
//                            String line;
//
//                            Log.i("TBG","2");
//
//                            while ((line = reader.readLine()) !=null ){
//                                response.append(line);
//                            }
//
//                            Log.i("TBG","respon:"+response);
//
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Log.i("TBG","3");
//                                    tv.setText(response.toString());
//                                }
//                            });

//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }).start();
            }
        });


    }


    private void serachInternet1() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    Log.i("TAG", "1");

                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    //connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    Log.i("TAG", "2");


                    reader = new BufferedReader(new InputStreamReader(in));
                    final StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Log.i("TAG", "respon:" + response.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("TAG", "3");
                            tv1.setText(response.toString());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (connection != null) {
                            connection.disconnect();
                        }

                    }

                }


//                ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkRequest.Builder builder=new NetworkRequest.Builder();
//                builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//                builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
//                NetworkRequest build=builder.build();
//                connectivityManager.requestNetwork(build,new ConnectivityManager.NetworkCallback(){
//                    @Override
//                    public void onAvailable(final Network network) {
//                        super.onAvailable(network);
//
//                    }
//                });
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                    Toast.makeText(this, "请先给予权限！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
