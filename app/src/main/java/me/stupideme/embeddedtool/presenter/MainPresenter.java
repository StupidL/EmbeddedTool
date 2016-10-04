package me.stupideme.embeddedtool.presenter;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.model.IBluetoothModel;
import me.stupideme.embeddedtool.model.BluetoothModelImpl;
import me.stupideme.embeddedtool.view.IMainView;
import me.stupideme.embeddedtool.view.custom.StupidButton;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public class MainPresenter {

    private IBluetoothModel iBluetoothModel;
    private IMainView iMainView;

    public MainPresenter(IMainView view, Context context) {
        iMainView = view;
        iBluetoothModel = new BluetoothModelImpl(context);
    }

    public void connectDevice(String address, boolean secure) {
        iBluetoothModel.connectDevice(address, secure);
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

    public void sendDataOverButton(DataType type, String s) {
        iBluetoothModel.sendDataOverButton(type, s);
    }

    public String receiveDataOverButton(DataType type) {
        return iBluetoothModel.receiveDataOverButton(type);
    }

    public int bindTextViewById(int other, int self) {
        StupidButton button = (StupidButton) iMainView.getViewById(other);
        button.setBindTextView((StupidTextView) iMainView.getViewById(self));
        return 1;
    }

    public int bindEditTextById(int other, int self) {
        if (iMainView.getViewById(other) instanceof StupidButtonSend) {
            StupidButton button = (StupidButton) iMainView.getViewById(other);
            button.setBindEditText((StupidEditText) iMainView.getViewById(self));
            return 1;
        }
        return 0;
    }

}
