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
    void insert(Message message);

    @Query("SELECT * FROM Message")
    LiveData<List<Message>> getAll();

    @Query("SELECT * FROM Message WHERE conv_id=:conv_id")
    LiveData<List<Message>> getAllFromConversation(long conv_id);
}