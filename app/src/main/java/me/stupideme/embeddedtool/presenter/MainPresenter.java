package me.stupideme.embeddedtool.presenter;

import android.widget.Button;
import android.widget.TextView;

import me.stupideme.embeddedtool.model.INetModel;
import me.stupideme.embeddedtool.model.NetModelImpl;
import me.stupideme.embeddedtool.view.IMainView;

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

    public void addReceiveButton(){
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

    public TextView findTextView(int id){
        return iMainView.findTextViewById(id);
    }
}
