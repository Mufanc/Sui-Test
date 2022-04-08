package sui.oneway.poc

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.ServiceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import miui.process.ProcessCloudData
import miui.process.ProcessManagerNative
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuBinderWrapper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()

            findViewById<Button>(R.id.set_whitelist).setOnClickListener {
                send(listOf("com.netease.cloudmusic"))
            }

            findViewById<Button>(R.id.clear_whitelist).setOnClickListener {
                send(listOf())
            }
        } else {
            Shizuku.requestPermission(0)
        }
    }

    private fun send(list: List<String>) {
        try {
            val service = ProcessManagerNative.asInterface(
                ShizukuBinderWrapper(ServiceManager.getService("ProcessManager"))
            )

            val data = ProcessCloudData()
            data.setCloudWhiteList(list)

            // class ProcessManagerProxy implements IProcessManager {
            //
            //    ......
            //
            //    public void updateCloudData(ProcessCloudData cloudData) throws RemoteException {
            //        Parcel data = Parcel.obtain();
            //        Parcel reply = Parcel.obtain();
            //        data.writeInterfaceToken("miui.IProcessManager");
            //        cloudData.writeToParcel(data, 0);
            //        this.mRemote.transact(8, data, reply, 1);  // IBinder.FLAG_ONEWAY = 1
            //        reply.readException();
            //        data.recycle();
            //        reply.recycle();
            //    }
            //
            //    ......
            //
            // }
            service.updateCloudData(data)
        } catch (err: Throwable) {
            Log.e("ProcessManager", "", err)
        }
    }
}
