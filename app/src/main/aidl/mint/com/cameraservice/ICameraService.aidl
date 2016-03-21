// ICameraService.aidl
package mint.com.cameraservice;

// Declare any non-default types here with import statements

interface ICameraService {
    void startPreviewFromCameraService(in Surface surface);
    void stopPreviewFromCameraService();
}
