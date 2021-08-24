package project.java4.bluechat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import project.java4.bluechat.model.Message;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(Message message);

    @Query("SELECT * FROM Message")
    List<Message> getAll();
    @Query("SELECT * FROM Message WHERE user_id = :user_id AND conv_id = :conv_id")
    List<Message> getAll(long user_id, long conv_id);
    @Query("SELECT * FROM Message WHERE conv_id=:conv_id")
    List<Message> getAllFromConversation(long conv_id);
}