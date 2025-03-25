import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'plugin_device_info_method_channel.dart';

abstract class PluginDeviceInfoPlatform extends PlatformInterface {
  /// Constructs a PluginDeviceInfoPlatform.
  PluginDeviceInfoPlatform() : super(token: _token);

  static final Object _token = Object();

  static PluginDeviceInfoPlatform _instance = MethodChannelPluginDeviceInfo();

  /// The default instance of [PluginDeviceInfoPlatform] to use.
  ///
  /// Defaults to [MethodChannelPluginDeviceInfo].
  static PluginDeviceInfoPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PluginDeviceInfoPlatform] when
  /// they register themselves.
  static set instance(PluginDeviceInfoPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getDeviceInfo() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
