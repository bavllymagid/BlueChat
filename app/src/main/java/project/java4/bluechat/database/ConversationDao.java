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

@Dao
public interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Conversation conversation);

    @Query("SELECT * FROM Conversation")
    LiveData<List<Conversation>> getAll();

    @Query("SELECT * FROM Conversation WHERE id=:id")
    Conversation get(String id);

    @Delete
    void delete(Conversation conversation);
}