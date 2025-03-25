import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_device_info/plugin_device_info.dart';
import 'package:plugin_device_info/plugin_device_info_platform_interface.dart';
import 'package:plugin_device_info/plugin_device_info_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPluginDeviceInfoPlatform
    with MockPlatformInterfaceMixin
    implements PluginDeviceInfoPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PluginDeviceInfoPlatform initialPlatform = PluginDeviceInfoPlatform.instance;

  test('$MethodChannelPluginDeviceInfo is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPluginDeviceInfo>());
  });

  test('getPlatformVersion', () async {
    PluginDeviceInfo pluginDeviceInfoPlugin = PluginDeviceInfo();
    MockPluginDeviceInfoPlatform fakePlatform = MockPluginDeviceInfoPlatform();
    PluginDeviceInfoPlatform.instance = fakePlatform;

    expect(await pluginDeviceInfoPlugin.getPlatformVersion(), '42');
  });
}
