package project.java4.bluechat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.java4.bluechat.R;
import project.java4.bluechat.database.AppDatabase;
import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.model.Message;

public class HomeAdapter extends BaseAdapter {
    List<Conversation> conversations = new ArrayList<Conversation>();
    Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public int getCount() {
        return conversations.size();
    }

    @Override
    public Object getItem(int position) {
        return conversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeAdapter.MessageViewHolder holder = new HomeAdapter.MessageViewHolder();
        LayoutInflater convInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Conversation conversation = conversations.get(position);

        convertView = convInflater.inflate(R.layout.home_item, null);
        holder.name = convertView.findViewById(R.id.textView1);
        holder.address = convertView.findViewById(R.id.textView2);
        holder.photo = convertView.findViewById(R.id.image23);

        holder.name.setText(AppDatabase.getDatabase(context).conversationOperations().get(conversation.getId()).getName());
        holder.address.setText(AppDatabase.getDatabase(context).conversationOperations().get(conversation.getId()).getId());
        holder.photo.setText(String.valueOf(AppDatabase.getDatabase(context).conversationOperations().get(conversation.getId()).getName().charAt(0)));

        return convertView;
    }

    class MessageViewHolder {

        public TextView name;
        public TextView address;
        public TextView photo;
    }
}
