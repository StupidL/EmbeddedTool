package me.stupideme.embeddedtool.presenter;

import android.widget.Button;
import android.widget.TextView;

import me.stupideme.embeddedtool.model.INetModel;
import me.stupideme.embeddedtool.model.NetModelImpl;
import me.stupideme.embeddedtool.view.IMainView;
import me.stupideme.embeddedtool.view.custom.StupidButton;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

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

    public String receiveDataOverButton() {
        return iNetModel.receiveDataOverButton();
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
