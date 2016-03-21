package mint.com.cameraservice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, ServiceConnection {

    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private ICameraService mICameraServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        } else {
            connectCameraService();
        }
    }

    private void connectCameraService() {
        Intent intent = new Intent(this, CameraService.class);
        if (!bindService(intent, this, BIND_AUTO_CREATE | BIND_IMPORTANT)) {
            Log.e(TAG, "onStart: fail to bind service");
        }
    }

    @Override
    protected void onStop() {
        try {
            if (mICameraServiceBinder != null) {
                mICameraServiceBinder.stopPreviewFromCameraService();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(this);

        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectCameraService();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            if (mICameraServiceBinder != null) {
                mICameraServiceBinder.startPreviewFromCameraService(surfaceHolder.getSurface());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        mICameraServiceBinder = ICameraService.Stub.asInterface(binder);

        setContentView(R.layout.activity_main);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
