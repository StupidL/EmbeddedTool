package me.stupideme.embeddedtool.view.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by stupidl on 16-10-19.
 */

public interface ISettingsView {

    /**
     * recovery default data type
     * @param list a list contains all default types
     */
    void recoveryTypeDefault(List<Map<String, String>> list);

    /**
     * recovery default protocol
     * @param list a list contains all default protocol
     */
    void recoveryProtocolDefault(List<Map<String, String>> list);
}
