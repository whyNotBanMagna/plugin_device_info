import Flutter
import UIKit
import FingerprintJS 

public class PluginDeviceInfoPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "plugin_device_info", binaryMessenger: registrar.messenger())
    let instance = PluginDeviceInfoPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "getDeviceInfo":
      let fingerprinter = FingerprinterFactory.getInstance()
      fingerprinter.getFingerprint { (fingerprint) in
        result(fingerprint)
      }
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
