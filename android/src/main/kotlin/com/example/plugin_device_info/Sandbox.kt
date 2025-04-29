
import android.content.Context
import android.os.Build
import android.os.Environment
import android.telephony.TelephonyManager
import java.io.File

object Sandbox {

    fun isInSandboxOrEmulator(context:Context) = isRooted() || isInVirtualAppContainer() || isMulti(context) || isMultiUser()

    fun hasEmulatorFiles(): Boolean {
        val knownFiles = arrayOf(
            "/dev/socket/qemud",
            "/dev/qemu_pipe",
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
        )
        return knownFiles.any { File(it).exists() }
    }

    fun isEmulatorByTelephony(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperatorName = tm.networkOperatorName.lowercase()
        return networkOperatorName.contains("android") || networkOperatorName.contains("unknown")
    }

    fun isRooted(): Boolean {
        val paths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/su/bin/su"
        )
        return paths.any { File(it).exists() }
    }

    fun isInVirtualAppContainer(): Boolean {
        val processPath = this::class.java.getResource("")?.toString().orEmpty()
        return processPath.contains("virtual") || processPath.contains("xspace") // 如：小米双开
    }

    fun isMulti(context:Context): Boolean {
        val uid = context.applicationInfo.uid
        val packages = context.packageManager.getPackagesForUid(uid)
        packages?.let {
            it.forEach {
                if (it != context.packageName) {
                    //上报在应用分身上使用
                    return true
                }
            }
        }
        return false
    }

    fun isMultiUser(): Boolean {
        return Environment.getExternalStorageDirectory().name != "0"
    }


}