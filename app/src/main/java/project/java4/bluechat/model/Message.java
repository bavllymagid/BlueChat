package project.java4.bluechat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "user_id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE),
@ForeignKey(entity = Conversation.class,
        parentColumns = "conv_id",
        childColumns = "conv_id",
        onDelete = ForeignKey.CASCADE)},
indices = {@Index("user_id"), @Index("conv_id")})
public class Message {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "user_id")
    private long userId;
    @ColumnInfo(name = "conv_id")
    private long convId;
    @ColumnInfo
    private String content;
    @ColumnInfo(name = "time_sent")
    private Date timeSent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getConvId() {
        return convId;
    }

    public void setConvId(long convId) {
        this.convId = convId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }
}
