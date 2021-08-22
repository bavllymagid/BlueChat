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
    LiveData<List<Conversation>> getAll();

    @Delete
    void delete(Conversation conversation);

    @Transaction
    @Query("SELECT * FROM Conversation")
    public LiveData<List<ConversationWithUsers>> getAllConverstaionParticipant();

    @Transaction
    @Query("SELECT * FROM Conversation WHERE conv_id = :id")
    public LiveData<List<ConversationWithUsers>> getConverstaionParticipants(long id);

}