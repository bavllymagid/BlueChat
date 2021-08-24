package project.java4.bluechat.ui;

import android.media.Image;
import android.widget.ImageView;

public class Message {
    private String text;
    private String name;
    private boolean belongsToCurrentUser;

    public Message(String text, String name, boolean belongsToCurrentUser) {
        this.text = text;
        this.name = name;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
    public String getText() {
        return text;
    }


    public String getName() {
        return name;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}