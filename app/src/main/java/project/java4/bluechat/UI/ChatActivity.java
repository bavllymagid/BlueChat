package project.java4.bluechat.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import project.java4.bluechat.R;
import project.java4.bluechat.adapters.HomeAdapter;
import project.java4.bluechat.adapters.messageAdapter;
import project.java4.bluechat.database.AppDatabase;
import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.utilities.ChatUtils;

public class ChatActivity extends AppCompatActivity {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private ChatUtils chatUtils;

    private ListView listMainChat;
    private EditText edCreateMessage;
    private ImageButton btnSendMessage;
    private messageAdapter adapterMainChat;

    private final int LOCATION_PERMISSION_REQUEST = 101;
    private final int STORAGE_PERMISSION_REQUEST = 103;
    private final int SELECT_DEVICE = 102;
    private static final int RESULT_LOAD_IMG = 200;

    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;

    public static final String DEVICE_NAME = "deviceName";
    public static final String DEVICE_ADDRESS = "deviceAddress";
    public static final String TOAST = "toast";
    private String connectedDeviceName;
    private String connectedDeviceAddress;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_STATE_CHANGED:
                    switch (message.arg1) {
                        case ChatUtils.STATE_NONE:
                            setState("Not Connected");
                            break;
                        case ChatUtils.STATE_LISTEN:
                            setState("Not Connected");
                            finish();
                            break;
                        case ChatUtils.STATE_CONNECTING:
                            setState("Connecting...");
                            break;
                        case ChatUtils.STATE_CONNECTED:
                            setState("Connected: " + connectedDeviceName);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] buffer1 = (byte[]) message.obj;
                    String outputBuffer = new String(buffer1);
                    project.java4.bluechat.model.Message msg = new project.java4.bluechat.model.Message(outputBuffer, connectedDeviceAddress, true);
                    AppDatabase.getDatabase(context).messageOperations().insert(msg);
                    break;
                case MESSAGE_READ:
                    byte[] buffer = (byte[]) message.obj;
                    String inputBuffer = new String(buffer, 0, message.arg1);
                    project.java4.bluechat.model.Message msg1 = new project.java4.bluechat.model.Message(inputBuffer, connectedDeviceAddress, false);
                    AppDatabase.getDatabase(context).messageOperations().insert(msg1);
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(context, message.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private void setState(CharSequence subTitle) {
        getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init(){
        context = this;
        connectedDeviceAddress = getIntent().getStringExtra("deviceAddress");
        connectedDeviceName = getIntent().getStringExtra("deviceName");

        listMainChat = findViewById(R.id.list_conversation);
        edCreateMessage = findViewById(R.id.ed_enter_message);
        btnSendMessage = findViewById(R.id.btn_send_msg);
        adapterMainChat = new messageAdapter(this);
        listMainChat.setVisibility(View.VISIBLE);

        listMainChat.setAdapter(adapterMainChat);
        chatUtils = ChatUtils.getInstance(handler);

        AppDatabase.getDatabase(context).messageOperations().getAllFromConversation(connectedDeviceAddress).observe(this, new Observer<List<project.java4.bluechat.model.Message>>() {
            @Override
            public void onChanged(List<project.java4.bluechat.model.Message> messages) {
                adapterMainChat.setMessages(messages);
                adapterMainChat.notifyDataSetChanged();
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edCreateMessage.getText().toString();
                if(chatUtils.getState() != ChatUtils.STATE_LISTEN) {
                    if (!message.isEmpty()) {
                        edCreateMessage.setText("");
                        chatUtils.write(message.getBytes());
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "You're not connected please make a connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}