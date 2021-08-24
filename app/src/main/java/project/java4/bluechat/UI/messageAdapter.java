package project.java4.bluechat.UI;

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

public class messageAdapter extends BaseAdapter {
    List<Message> messages = new ArrayList<Message>();
    Context context;

    public messageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);


        if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.mymessage, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.messageBody.setText(message.getText());
            holder.name.setText(message.getName());

        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.theirmessage, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
            holder.name.setText(message.getName());
            holder.messageBody.setText(message.getText());
        }

        return convertView;
    }

    class MessageViewHolder {

        public TextView name;
        public TextView messageBody;
    }
}
