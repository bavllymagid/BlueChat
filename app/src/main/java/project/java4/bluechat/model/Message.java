package project.java4.bluechat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = Conversation.class,
        parentColumns = "id",
        childColumns = "conv_id",
        onDelete = ForeignKey.CASCADE)
},
indices = { @Index("conv_id") })
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "conv_id")
    private String convId;

    @ColumnInfo
    boolean isMyMessage;

    @ColumnInfo
    private String content;

    @ColumnInfo(name = "time_sent")
    private Date timeSent;

    @Ignore
    public Message(String content, String convId, boolean isMyMessage) {
        this.content = content;
        this.convId = convId;
        this.isMyMessage = isMyMessage;
        this.timeSent = Calendar.getInstance().getTime();
    }

    public Message() {
        this.timeSent = Calendar.getInstance().getTime();
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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