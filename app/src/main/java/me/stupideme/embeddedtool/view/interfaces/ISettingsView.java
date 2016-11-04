package me.stupideme.embeddedtool.view.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by stupidl on 16-10-19.
 */

public interface ISettingsView {
    void recoveryTypeDefault(List<Map<String, String>> list);

    void recoveryProtocolDefault(List<Map<String, String>> list);
}
