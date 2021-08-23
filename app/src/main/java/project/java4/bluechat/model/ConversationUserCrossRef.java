package project.java4.bluechat.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"conv_id", "user_id"},indices = {@Index("conv_id"), @Index("user_id")})
public class ConversationUserCrossRef {
    long conv_id;
    @NonNull
    String user_id;
}