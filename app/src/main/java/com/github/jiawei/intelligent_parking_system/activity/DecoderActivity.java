package com.github.jiawei.intelligent_parking_system.activity;



public class DecoderActivity{/* extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);


        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        resultTextView=findViewById(R.id.tv);

        if (ContextCompat.checkSelfPermission(DecoderActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DecoderActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },1);
        }else{
            // Use this function to enable/disable decoding
            qrCodeReaderView.setQRDecodingEnabled(true);

            // Use this function to change the autofocus interval (default is 5 secs)
            qrCodeReaderView.setAutofocusInterval(2000L);

            // Use this function to enable/disable Torch
            qrCodeReaderView.setTorchEnabled(true);

            // Use this function to set front camera preview
            qrCodeReaderView.setFrontCamera();

            // Use this function to set back camera preview
            qrCodeReaderView.setBackCamera();
        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        resultTextView.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults.length>0){
                // Use this function to enable/disable decoding
                qrCodeReaderView.setQRDecodingEnabled(true);

                // Use this function to change the autofocus interval (default is 5 secs)
                qrCodeReaderView.setAutofocusInterval(2000L);

                // Use this function to enable/disable Torch
                qrCodeReaderView.setTorchEnabled(true);

                // Use this function to set front camera preview
                qrCodeReaderView.setFrontCamera();

                // Use this function to set back camera preview
                qrCodeReaderView.setBackCamera();
            }else {
                Toast.makeText(this,"请先授予权限!",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}

