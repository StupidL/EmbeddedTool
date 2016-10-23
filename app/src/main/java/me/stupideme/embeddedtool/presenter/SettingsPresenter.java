package me.stupideme.embeddedtool.presenter;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.model.ISettingsModel;
import me.stupideme.embeddedtool.model.SettingsModelImpl;
import me.stupideme.embeddedtool.view.ISettingsView;

/**
 * Created by stupidl on 16-10-19.
 */

public class SettingsPresenter {

    private ISettingsView iSettingsView;
    private ISettingsModel iSettingsModel;

    public SettingsPresenter(ISettingsView view) {
        iSettingsView = view;
        iSettingsModel = SettingsModelImpl.getInstance();
    }

    /**
     * add a data type
     * @param map
     */
    public void addDataType(Map<String, String> map) {
        iSettingsModel.addDataType(map);
    }

    /**
     * remove a data type
     * @param name
     */
    public void removeDataType(String name) {
        iSettingsModel.removeDataType(name);
    }

    /**
     * save the changes when edit protocol
     * @param map contains header and tail
     */
    public void saveDataProtocol(Map<String, String> map) {
        iSettingsModel.saveDataProtocol(map);
    }

    /**
     * get all data types
     * @return
     */
    public List<Map<String, String>> getDataType() {
        return iSettingsModel.getDataType();
    }

    /**
     * get all data types
     * @return a list contains all data types
     */
    public List<Map<String, String>> getDataProtocol() {
        return iSettingsModel.getDataProtocol();
    }

    /**
     * set data types to default
     */
    public void recoveryTypeDefault() {
        iSettingsView.recoveryTypeDefault(iSettingsModel.getDataTypeDefault());
    }

    /**
     * set data protocol to default
     */
    public void recoveryProtocolDefault(){
        iSettingsView.recoveryProtocolDefault(iSettingsModel.getProtocolDefault());
    }
}
