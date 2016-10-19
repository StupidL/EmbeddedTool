package me.stupideme.embeddedtool.model;

import java.util.Map;

/**
 * Created by stupidl on 16-10-19.
 */

public interface ISettingsModel {
    void removeDataType(String name);

    void addDataType(Map<String, String> map);

    Map<String, String> getAllDataType();

    void saveDataProtocol(Map<String, String> map);

    Map<String, String> getDataProtocol();

    Map<String,String> getDefault();
}
