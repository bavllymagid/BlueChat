package project.java4.bluechat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(foreignKeys = {@ForeignKey(entity = Conversation.class,
    parentColumns = "conv_id",
        childColumns = "conv_id",
        onDelete = ForeignKey.CASCADE), @ForeignKey(entity = User.class,
        parentColumns = "user_id",
        childColumns = "admin_id",
        onDelete = ForeignKey.CASCADE)
},indices = {@Index(value = "admin_id")})
public class Group extends Conversation {
    @ColumnInfo(name = "admin_id")
    String adminId;
    @ColumnInfo
    String name;
    @ColumnInfo(name = "photo_path")
    String photoPath;

    public Group() {
        isGroup = true;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}