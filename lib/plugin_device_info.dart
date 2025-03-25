
import 'plugin_device_info_platform_interface.dart';

class PluginDeviceInfo {
  Future<String?> getPlatformVersion() {
    return PluginDeviceInfoPlatform.instance.getPlatformVersion();
  }

  Future<String?> getDeviceInfo() {
    return PluginDeviceInfoPlatform.instance.getDeviceInfo();
  }

}
