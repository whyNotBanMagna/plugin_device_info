package com.example.plugin_device_info

import androidx.annotation.NonNull
import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.AbiTypeSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.CameraListSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.CoresCountSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.GlesVersionSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.InputDevicesV2Signal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.ManufacturerNameSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.ModelNameSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.ProcCpuInfoV2Signal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.SensorsSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.TotalInternalStorageSpaceSignal
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.TotalRamSignal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import org.json.JSONObject

/** PluginDeviceInfoPlugin */
class PluginDeviceInfoPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context : Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "plugin_device_info")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else if (call.method == "getDeviceInfo"){
      val fingerPrinter = FingerprinterFactory.create(context)
      fingerPrinter.getFingerprint(Fingerprinter.Version.V_6) { fingerprint ->
        fingerPrinter.getDeviceId(Fingerprinter.Version.V_6) { deviceIdResult ->
          //device id info
          val deviceId = deviceIdResult.deviceId
          val androidId = deviceIdResult.androidId
          val gsfId = deviceIdResult.gsfId
          val mediaDrmId = deviceIdResult.mediaDrmId
          val jsonObject = JSONObject()
          jsonObject.put("deviceId",deviceId)
          jsonObject.put("androidId",androidId)
          jsonObject.put("gsfId",gsfId)
          jsonObject.put("mediaDrmId",mediaDrmId)
          jsonObject.put("fingerPrint",fingerprint)
//          jsonObject.put("appVersion",AppConstant.getVersionCode())
//          jsonObject.put("imei",AppConstant.getDeviceIMEI())

          fingerPrinter.getFingerprintingSignalsProvider()?.getSignalsMatching(Fingerprinter.Version.V_6,StabilityLevel.STABLE)?.forEach {
            when (it){
              is ManufacturerNameSignal -> {
                jsonObject.put("ManufacturerNameSignal", it.value)
              }
              is ModelNameSignal -> {
                jsonObject.put("ModelNameSignal", it.value)
              }
              is TotalRamSignal -> {
                jsonObject.put("TotalRamSignal", it.value)
              }
              is TotalInternalStorageSpaceSignal -> {
                jsonObject.put("TotalInternalStorageSpaceSignal", it.value)
              }
              is CameraListSignal -> {
                jsonObject.put("CameraListSignal", it.value)
              }
              is GlesVersionSignal -> {
                jsonObject.put("GlesVersionSignal", it.value)
              }
              is AbiTypeSignal -> {
                jsonObject.put("AbiTypeSignal", it.value)
              }
              is CoresCountSignal -> {
                jsonObject.put("CoresCountSignal", it.value)
              }
              else -> {}
            }
          }
          result.success(jsonObject.toString())
        }
      }
    }else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
