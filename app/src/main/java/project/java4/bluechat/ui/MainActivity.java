package project.java4.bluechat.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import project.java4.bluechat.preferences.SettingsActivity;
import java.util.List;

import project.java4.bluechat.adapters.messageAdapter;
import project.java4.bluechat.database.AppDatabase;
import project.java4.bluechat.model.Conversation;
import project.java4.bluechat.utilities.ChatUtils;
import project.java4.bluechat.R;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private ChatUtils chatUtils;

    private ListView listMainChat;
    private EditText edCreateMessage;
    private Button btnSendMessage;

    private Button btnSendImage;

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
                    Conversation conv = new Conversation(connectedDeviceAddress, connectedDeviceName);
                    AppDatabase.getDatabase(context).conversationOperations().insert(conv);
                    project.java4.bluechat.model.Message msg = new project.java4.bluechat.model.Message(outputBuffer, connectedDeviceAddress, true);
                    AppDatabase.getDatabase(context).messageOperations().insert(msg);
                    break;
                case MESSAGE_READ:
                    byte[] buffer = (byte[]) message.obj;
                    String inputBuffer = new String(buffer, 0, message.arg1);
                    Conversation conv1 = new Conversation(connectedDeviceAddress, connectedDeviceName);
                    AppDatabase.getDatabase(context).conversationOperations().insert(conv1);
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
        setContentView(R.layout.activity_main);

        context = this;

        init();
        initBluetooth();
    }

    private void init() {
        chatUtils = new ChatUtils(handler);
        listMainChat = findViewById(R.id.list_conversation);
        edCreateMessage = findViewById(R.id.ed_enter_message);
        btnSendMessage = findViewById(R.id.btn_send_msg);
        btnSendImage = findViewById(R.id.btn_send_image);

        adapterMainChat = new messageAdapter(context);

        AppDatabase.getDatabase(context).messageOperations().getAll().observe(this, new Observer<List<project.java4.bluechat.model.Message>>() {
            @Override
            public void onChanged(List<project.java4.bluechat.model.Message> messages) {
                adapterMainChat.setMessages(messages);
                adapterMainChat.notifyDataSetChanged();
            }
        });

        listMainChat.setAdapter(adapterMainChat);

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

        btnSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhotoPermission();
            }
        });
    }

    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if(!bluetoothAdapter.isEnabled()) {
                enableBluetooth();
                try {
                    Thread.sleep(1000);

                    chatUtils.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                chatUtils.start();
            }
        }else {
            Toast.makeText(context, "No bluetooth found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start_server:
                checkPermissions();
                return true;
            case R.id.menu_enable_bluetooth:
                enableBluetooth();
                return true;
            case R.id.menu_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void  checkPhotoPermission(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
        }else {
            getImageFromGallery();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(context, DeviceListActivity.class);
            startActivityForResult(intent, SELECT_DEVICE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getStringExtra("deviceAddress");
            chatUtils.connect(bluetoothAdapter.getRemoteDevice(address));
        }

//        if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap image = BitmapFactory.decodeStream(imageStream);
//                final ByteArrayOutputStream outImgStream = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.JPEG, 50, outImgStream);
//                final byte[] imageBytes = outImgStream.toByteArray();
//                Pair<String, byte[]> pair = new Pair<>("", imageBytes) ;
//                adapterMainChat.add(pair);
//                chatUtils.write(imageBytes);
//            }catch (FileNotFoundException e){
//                e.printStackTrace();
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(context, DeviceListActivity.class);
                startActivityForResult(intent, SELECT_DEVICE);
            } else {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Location permission is required.\nPlease grant")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermissions();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        }).show();
            }
        }
        else if(requestCode == STORAGE_PERMISSION_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getImageFromGallery();
            }else {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Memory permission is required.\nPlease grant")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkPhotoPermission();
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        }).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void enableBluetooth(){
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoveryIntent);
        }

        try {
            Thread.sleep(1000);
            if(bluetoothAdapter.isEnabled())
               chatUtils.start();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getImageFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatUtils != null) {
            chatUtils.stop();
        }
    }
}