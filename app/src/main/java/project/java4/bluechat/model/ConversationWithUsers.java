package project.java4.bluechat.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ConversationWithUsers {
    @Embedded public Conversation conversation;
    @Relation(
            parentColumn = "conv_id",
            entityColumn = "user_id",
            associateBy = @Junction(ConversationUserCrossRef.class)
    )
    public List<User> users;
}