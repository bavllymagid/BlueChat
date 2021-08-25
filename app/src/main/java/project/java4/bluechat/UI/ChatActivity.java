package project.java4.bluechat.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import project.java4.bluechat.R;
import project.java4.bluechat.adapters.messageAdapter;
import project.java4.bluechat.database.AppDatabase;
import project.java4.bluechat.utilities.ChatUtils;

import static project.java4.bluechat.UI.MainActivity.DEVICE_ADDRESS;
import static project.java4.bluechat.UI.MainActivity.DEVICE_NAME;

public class ChatActivity extends AppCompatActivity {
    private Context context;
    private ChatUtils chatUtils;

    private ListView listMainChat;
    private EditText edCreateMessage;
    private ImageButton btnSendMessage;
    private messageAdapter adapterMainChat;
    private Button connectionButton;
    private RelativeLayout connectionMessage;

    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_DEVICE_NAME = 3;
    public static final int MESSAGE_TOAST = 4;

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
                            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case ChatUtils.STATE_CONNECTING:
                            setState("Connecting...");
                            break;
                        case ChatUtils.STATE_CONNECTED:
                            connectionMessage.setVisibility(View.GONE);
                            setState("");
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
                case MESSAGE_DEVICE_NAME:
                    connectedDeviceName = message.getData().getString(DEVICE_NAME);
                    connectedDeviceAddress = message.getData().getString(DEVICE_ADDRESS);
                    Toast.makeText(context, connectedDeviceName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, connectedDeviceAddress, Toast.LENGTH_SHORT).show();
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
        if(chatUtils.getState() == ChatUtils.STATE_CONNECTED && MainActivity.bluetoothAdapter != null){
            connectionMessage.setVisibility(View.GONE);
        }
    }

    private void init(){
        context = this;
        connectedDeviceAddress = getIntent().getStringExtra("deviceAddress");
        connectedDeviceName = getIntent().getStringExtra("deviceName");

        listMainChat = findViewById(R.id.list_conversation);
        edCreateMessage = findViewById(R.id.ed_enter_message);
        btnSendMessage = findViewById(R.id.btn_send_msg);
        connectionButton = findViewById(R.id.btn_connect);
        connectionMessage = findViewById(R.id.connect_layout);

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

        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.bluetoothAdapter != null && chatUtils.getState() != ChatUtils.STATE_CONNECTED) {
                    chatUtils.connect(MainActivity.bluetoothAdapter.getRemoteDevice(connectedDeviceAddress));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder back = new AlertDialog.Builder(this);
            back.setMessage("Do you want to disconnect");
            back.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    chatUtils.connectionLost();
                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            back.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    back.setCancelable(true);
                }
            });
            back.show();

    }
}