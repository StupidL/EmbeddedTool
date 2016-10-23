package me.stupideme.embeddedtool.model;

import java.util.List;
import java.util.Map;

/**
 * Created by stupidl on 16-10-19.
 */

public interface ISettingsModel {
    /**
     * remove a custom data type
     *
     * @param name name of type
     */
    void removeDataType(String name);

    /**
     * add a custom data type
     *
     * @param map a map contains name and code
     */
    void addDataType(Map<String, String> map);

    /**
     * save a custom protocol
     *
     * @param map a map contains header and tail
     */
    void saveDataProtocol(Map<String, String> map);

    /**
     * get all data types include default and custom
     *
     * @return a list contains data types' info
     */
    List<Map<String, String>> getDataType();


    /**
     * get custom data protocol
     *
     * @return a list
     */
    List<Map<String, String>> getDataProtocol();

    /**
     * get default data type
     *
     * @return a list
     */
    List<Map<String, String>> getDataTypeDefault();

    /**
     * get default protocol
     *
     * @return a list
     */
    List<Map<String, String>> getProtocolDefault();
}
