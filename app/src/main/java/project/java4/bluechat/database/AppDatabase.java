package project.java4.bluechat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.model.Message;
import project.java4.bluechat.utilities.Converters;

@Database(entities = { Conversation.class, Message.class }, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getDatabase(final Context context) {
        if(instance == null)
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Bluechat_DB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return instance;
    }

    public abstract ConversationDao conversationOperations();
    public abstract MessageDao messageOperations();
}