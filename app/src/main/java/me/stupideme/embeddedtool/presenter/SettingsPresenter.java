package me.stupideme.embeddedtool.presenter;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.model.ISettingsModel;
import me.stupideme.embeddedtool.model.SettingsModelImpl;
import me.stupideme.embeddedtool.view.interfaces.ISettingsView;

/**
 * Created by stupidl on 16-10-19.
 * a presenter of settings activity to communicate with data base
 */

public class SettingsPresenter {

    /**
     * iSettingsView. SettingsActivity actually
     */
    private ISettingsView iSettingsView;

    /**
     * iSettingsModel. operations whit database
     */
    private ISettingsModel iSettingsModel;

    /**
     * constructor
     * @param view ISettingsView
     */
    public SettingsPresenter(ISettingsView view) {
        //init iSettingsView
        iSettingsView = view;
        //get instance of iSettingsModel
        iSettingsModel = SettingsModelImpl.getInstance();
    }

    /**
     * add a data type
     * @param map a map contains name and code of type
     */
    public void addDataType(Map<String, String> map) {
        iSettingsModel.addDataType(map);
    }

    /**
     * remove a data type
     * @param name name of data type
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
     * @return a list
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
