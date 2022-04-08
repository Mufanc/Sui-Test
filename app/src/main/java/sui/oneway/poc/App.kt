package sui.oneway.poc

import android.app.Application
import org.lsposed.hiddenapibypass.HiddenApiBypass
import rikka.sui.Sui

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        HiddenApiBypass.setHiddenApiExemptions("")
        Sui.init(packageName)
    }
}