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

    public void addDataType(Map<String, String> map) {
        iSettingsModel.addDataType(map);
    }

    public void removeDataType(String name) {
        iSettingsModel.removeDataType(name);
    }

    public List<Map<String, String>> getAllDataType() {
        return iSettingsModel.getAllDataType();
    }

    public void saveDataProtocol(Map<String, String> map) {
        iSettingsModel.saveDataProtocol(map);
    }

    public List<Map<String, String>> getDataProtocol() {
        return iSettingsModel.getDataProtocol();
    }

    public void recoveryDefault() {
        iSettingsView.recoveryDefault(iSettingsModel.getDefault());
    }
}
