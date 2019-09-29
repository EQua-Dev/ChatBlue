 package com.androidStrike.bluetoothchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

 public class MainActivity extends AppCompatActivity {

    Button btnlisten;
    Button btnListDevices;
    Button btnSend;
    TextView tvStatus;
    TextView tvReceivedMessage;
    EditText etWriteMessage;
    ListView listView;

//    variable for the bluetooth adapter
    BluetoothAdapter bluetoothAdapter;
//    variable for the bluetooth devices
    BluetoothDevice[] btArray;

//    we create an object variable of the SendReceive class
    SendReceive sendReceive;

//    constants for the handler
     static final int STATE_LISTENING = 1;
     static final int STATE_CONNECTING = 2;
     static final int STATE_CONNECTED = 3;
     static final int STATE_CONNECTION_FAILED = 4;
     static final int STATE_MESSAGE_RECEIVED = 5;

//     we use this constant to enable the device bluetooth
     int REQUEST_ENABLE_BLUETOOTH = 1;

     public static final String APP_NAME = "ChatBlue";
     public static final UUID MY_UUID = UUID.fromString("7a29c886-4356-4a33-b0bf-8e321264af74");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIds();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//        we want to enable the bluetooth when the onCreate calls it

//        if the device's bluetooth adapter is not enabled...
        if (!bluetoothAdapter.isEnabled()){

//            ...we request to activate the device's bluetooth
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        implementListeners();
    }

//    method to implement all the button listeners and listView
     private void implementListeners() {
        btnListDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                btArray = new BluetoothDevice[bt.size()];
                int index = 0;

                if (bt.size() > 0){
                    for (BluetoothDevice device: bt){
                        btArray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listView.setAdapter(arrayAdapter);
                }

            }
        });

        btnlisten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                we create an instance of the ServerClass class...
                ServerClass serverClass = new ServerClass();
//                ...and begin its execution
                serverClass.start();
            }
        });

//        we set onItemClick to the listView items in order to connect to the devices
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                we create an instance of the ClientClass class
                ClientClass clientClass = new ClientClass(btArray[i]);
//                we call on the thread to start
                clientClass.start();

                tvStatus.setText("Connecting");
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = String.valueOf(etWriteMessage.getText());
                sendReceive.write(string.getBytes());
            }
        });

     }

//     handler to listen for message changes in other threads
//
     Handler handler = new Handler(new Handler.Callback() {
         @Override
         public boolean handleMessage(@NonNull Message msg) {

//             we create a switch case loop for the 'msg' parameter using 'what' to check the message received
             switch (msg.what){
                 case STATE_LISTENING:
                     tvStatus.setText("Listening...");
                     break;
                 case STATE_CONNECTING:
                     tvStatus.setText("Connecting...");
                     break;
                 case STATE_CONNECTED:
                     tvStatus.setText("Connected");
                     break;
                 case STATE_CONNECTION_FAILED:
                     tvStatus.setText("Connection Failed");
                     break;
                 case STATE_MESSAGE_RECEIVED:
                     byte[] readBuff = (byte[]) msg.obj;
                     String tempMsg = new String(readBuff, 0, msg.arg1);
                     tvReceivedMessage.setText(tempMsg);
                     break;
             }
             return true;
         }
     });

     //    method to find all the components in the layout
     private void findViewByIds() {
        btnlisten = findViewById(R.id.button_listen);
        btnListDevices = findViewById(R.id.button_paired_devices);
        btnSend = findViewById(R.id.button_send);
        tvStatus = findViewById(R.id.text_view_status);
        tvReceivedMessage = findViewById(R.id.text_view_received_message);
        etWriteMessage = findViewById(R.id.edit_text_message);
        listView = findViewById(R.id.list_view_devices);
     }

//     we create a separate class for server socket
     private class ServerClass extends Thread{
         private BluetoothServerSocket serverSocket;
             public ServerClass(){
                 try {
//                     we search for device broadcast having our APP_NAME and UUID with the bluetooth adapter
                     serverSocket=bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }

    @Override
    public void run() {
//                 we initialize the bluetooth server socket to null
        BluetoothSocket socket = null;

        while (socket == null){
            try {
//                if the try is successful without any errors or exceptions...
                Message message = Message.obtain();
                message.what = STATE_CONNECTING;
                handler.sendMessage(message);

//                we set the server socket to accept connection requests
                socket = serverSocket.accept();
            } catch (IOException e) {
//                if there was an error or exception in execution attempt...
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }

//            if a connection has already been established
            if (socket!=null){
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
//                we initialize the sendReceive object in the Sever class
                sendReceive = new SendReceive(socket);
                sendReceive.start();
                break;
            }
        }
    }
}


     private class ClientClass extends Thread{
         private BluetoothDevice device;
         private BluetoothSocket socket;

         public ClientClass(BluetoothDevice device1){
             device = device1;

             try {
                 socket = device.createRfcommSocketToServiceRecord(MY_UUID);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

         @Override
         public void run() {
//             cancel discovery
             try {
                 socket.connect();
                 Message message = Message.obtain();
                 message.what = STATE_CONNECTED;
                 handler.sendMessage(message);
//                 we then initialize the sendReceive in the client thread
                 sendReceive = new SendReceive(socket);
                 sendReceive.start();
             } catch (IOException e) {
                 e.printStackTrace();
                 Message message = Message.obtain();
                 message.what = STATE_CONNECTION_FAILED;
                 handler.sendMessage(message);
             }
         }
     }


     private class SendReceive extends Thread{
         private final BluetoothSocket bluetoothSocket;
         private final InputStream inputStream;
         private final OutputStream outputStream;

         public SendReceive (BluetoothSocket socket){
             bluetoothSocket = socket;
             InputStream tempIn = null;
             OutputStream tempOut = null;

             try {
             tempIn = bluetoothSocket.getInputStream();
                 tempOut = bluetoothSocket.getOutputStream();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             inputStream = tempIn;
             outputStream = tempOut;
         }

         @Override
         public void run() {
             byte[] buffer = new byte[1024];
             int bytes;

             while (true){
                 try {
                     bytes=inputStream.read(buffer);
//                     we then send the message to the handler
//                     parameters are: 'what' ie. the action, the number of bytes in the message, '-1' because we are not using arg 2, and finally the message
                     handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }

//         method for sending the message
         private void write(byte[] bytes){
             try {
                 outputStream.write(bytes);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }
 }
