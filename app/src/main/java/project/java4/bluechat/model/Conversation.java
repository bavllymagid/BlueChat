package project.java4.bluechat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(primaryKeys = {"conv_id"})
public class Conversation {
    long conv_id;

    @ColumnInfo(name = "time_created")
    Date timeCreated;

    @ColumnInfo(name = "is_group")
    boolean isGroup;

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getConv_id() {
        return conv_id;
    }

    public void setConv_id(long conv_id) {
        this.conv_id = conv_id;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
