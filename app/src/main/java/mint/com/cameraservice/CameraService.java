package mint.com.cameraservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Surface;

public class CameraService extends Service {
    CameraServiceBinder mBinder = null;

    public CameraService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return getBinder();
    }

    private IBinder getBinder() {
        if (mBinder == null) {
            mBinder = new CameraServiceBinder();
        }

        return mBinder;
    }
}
