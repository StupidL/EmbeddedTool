package me.stupideme.embeddedtool.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.stupideme.embeddedtool.view.custom.StupidEditText;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface IMainView {

    /**
     * add a send button to a view group which becomes it's parent
     */
    void addSendButton();

    /**
     * add a receive button to a view group which becomes it's parent
     */
    void addReceiveButton();

    /**
     * remove a button(both of send button and receive button) from it's parent(a view group).
     * @param view the button to be removed.
     */
    void removeButton(Button view);

    /**
     * add a text view to a view group which becomes to it's parent.
     */
    void addTextView();

    /**
     * remove a text view from it's parent(a view group).
     * @param view the view to be removed.
     */
    void removeTextView(TextView view);

    void addEditText();

    void removeEditText(StupidEditText view);

    View getViewById(int id);
}
