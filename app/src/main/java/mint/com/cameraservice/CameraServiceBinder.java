package mint.com.cameraservice;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.RemoteException;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by mint924 on 2016/3/15.
 */
public class CameraServiceBinder extends ICameraService.Stub {
    Surface mSurface = null;
    SurfaceHolder mSurfaceHolder = new SurfaceHolder() {
        @Override
        public void addCallback(Callback callback) {

        }

        @Override
        public void removeCallback(Callback callback) {

        }

        @Override
        public boolean isCreating() {
            return false;
        }

        @Override
        public void setType(int i) {

        }

        @Override
        public void setFixedSize(int i, int i1) {

        }

        @Override
        public void setSizeFromLayout() {

        }

        @Override
        public void setFormat(int i) {

        }

        @Override
        public void setKeepScreenOn(boolean b) {

        }

        @Override
        public Canvas lockCanvas() {
            return null;
        }

        @Override
        public Canvas lockCanvas(Rect rect) {
            return null;
        }

        @Override
        public void unlockCanvasAndPost(Canvas canvas) {

        }

        @Override
        public Rect getSurfaceFrame() {
            return null;
        }

        @Override
        public Surface getSurface() {
            return mSurface;
        }
    };
    private Camera mCamera;

    @Override
    public void startPreviewFromCameraService(Surface surface) throws RemoteException {
        mSurface = surface;
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    mCamera = Camera.open(i);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mCamera.startPreview();
            }
        }
    }

    @Override
    public void stopPreviewFromCameraService() throws RemoteException {
        mCamera.stopPreview();
        mCamera.release();
    }
}
