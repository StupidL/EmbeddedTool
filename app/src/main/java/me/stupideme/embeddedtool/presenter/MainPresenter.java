package me.stupideme.embeddedtool.presenter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.stupideme.embeddedtool.model.INetModel;
import me.stupideme.embeddedtool.model.NetModelImpl;
import me.stupideme.embeddedtool.view.IMainView;
import me.stupideme.embeddedtool.view.custom.StupidEditText;

/**
 * Created by StupidL on 2016/9/30.
 */

public class MainPresenter {

    private INetModel iNetModel;
    private IMainView iMainView;

    public MainPresenter(IMainView view) {
        iMainView = view;
        iNetModel = new NetModelImpl();
    }

    public void addSendButton() {
        iMainView.addSendButton();
    }

    public void addReceiveButton() {
        iMainView.addReceiveButton();
    }

    public void removeButton(Button view) {
        iMainView.removeButton(view);
    }

    public void addTextView() {
        iMainView.addTextView();
    }

    public void removeTextView(TextView view) {
        iMainView.removeTextView(view);
    }

    public void addEditText() {
        iMainView.addEditText();
    }

    public void removeEditText(StupidEditText view) {
        iMainView.removeEditText(view);
    }

    public void sendDataOverButton(String s) {
        iNetModel.sendDataOverButton(s);
    }

    public void sendDataOverTextView(String s) {
        iNetModel.sendDataOverTextView(s);
    }

    public String receiveDataOverButton() {
        return iNetModel.receiveDataOverButton();
    }

    public String receiveDataOverTextView() {
        return iNetModel.receiveDataOverTextView();
    }

    public void sendDataOverEditText(String s) {
        iNetModel.sendDataOverEditText(s);
    }

    public String receiveDataOverEditText() {
        return iNetModel.receiveDataOverEditText();
    }

    public View bindViewById(int id){
        return iMainView.getViewById(id);
    }

}
