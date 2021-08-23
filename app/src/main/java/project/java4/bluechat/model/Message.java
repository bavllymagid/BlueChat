package project.java4.bluechat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(foreignKeys = {
@ForeignKey(entity = Conversation.class,
        parentColumns = "conv_id",
        childColumns = "conv_id",
        onDelete = ForeignKey.CASCADE)},
indices = {@Index("user_id"), @Index("conv_id")})
public class Message {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "conv_id")
    private long convId;
    @ColumnInfo
    private String content;
    @ColumnInfo(name = "time_sent")
    private Date timeSent;

    @Ignore
    public Message(String content, String macAddress) {
        this.content = content;
        this.userId = macAddress;
    }

    public Message() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
