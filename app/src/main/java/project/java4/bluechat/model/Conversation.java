package project.java4.bluechat.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
public class Conversation {
    @PrimaryKey
    @NonNull
    String id;

    @ColumnInfo
    private String name;

    @ColumnInfo(name = "time_created")
    Date timeCreated;

    @Ignore
    public Conversation(String id, String name) {
        this.id = id;
        this.name = name;
        timeCreated = Calendar.getInstance().getTime();
    }

    public Conversation() {
        timeCreated = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

}
