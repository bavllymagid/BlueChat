package project.java4.bluechat.UI;

import android.media.Image;
import android.widget.ImageView;

public class Message {
    private String text;
    private String name;
    private ImageView image;
    private boolean belongsToCurrentUser;

    public Message(String text, String name, boolean belongsToCurrentUser, ImageView image) {
        this.text = text;
        this.name = name;
        this.image = image;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
    public String getText() {
        return text;
    }

    public ImageView getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}