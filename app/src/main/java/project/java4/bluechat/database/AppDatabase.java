package project.java4.bluechat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.model.ConversationWithUsers;
import project.java4.bluechat.model.Group;
import project.java4.bluechat.model.Message;
import project.java4.bluechat.model.ConversationUserCrossRef;
import project.java4.bluechat.model.User;
import project.java4.bluechat.utilities.Converters;

@Database(entities = {User.class,
        Conversation.class, Group.class,
        ConversationUserCrossRef.class,
        Message.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getDatabase(final Context context) {
        if(instance == null)
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Bluechat_DB").fallbackToDestructiveMigration().build();
        return instance;
    }

    public abstract UserDao userOperations();
    public abstract ConverstaionDao conversationOperations();
    public abstract GroupDao groupOperations();
    public abstract MessageDao messageOperations();
}