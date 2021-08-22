package project.java4.bluechat.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import project.java4.bluechat.model.Group;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Group group);

    @Query("SELECT * FROM `group`")
    LiveData<List<Group>> getAll();

    @Query("SELECT * FROM `group` WHERE conv_id = :id")
    Group getGroup(long id);

    @Delete
    void delete(Group group);
}