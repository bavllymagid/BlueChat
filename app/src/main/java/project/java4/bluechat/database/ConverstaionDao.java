package project.java4.bluechat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.model.ConversationWithUsers;

@Dao
public interface ConverstaionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Conversation conversation);

    @Query("SELECT * FROM Conversation")
    List<Conversation> getAll();

    @Delete
    void delete(Conversation conversation);

    @Transaction
    @Query("SELECT * FROM Conversation")
    public List<ConversationWithUsers> getAllConversationParticipant();

    @Transaction
    @Query("SELECT * FROM Conversation WHERE conv_id = :id")
    public List<ConversationWithUsers> getConversationParticipants(long id);

}