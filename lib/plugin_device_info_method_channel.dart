import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'plugin_device_info_platform_interface.dart';

/// An implementation of [PluginDeviceInfoPlatform] that uses method channels.
class MethodChannelPluginDeviceInfo extends PluginDeviceInfoPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('plugin_device_info');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> getDeviceInfo() async {
    final deviceInfo = await methodChannel.invokeMethod<String>('getDeviceInfo');
    return deviceInfo;
  }
}
