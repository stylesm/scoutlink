package uk.org.mattford.scoutlink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import uk.org.mattford.scoutlink.R;
import uk.org.mattford.scoutlink.irc.IRCService;
import uk.org.mattford.scoutlink.model.Settings;

public class SettingsActivity extends ActionBarActivity {

    private Settings settings;

    private final int AUTOJOIN_REQUEST_CODE = 0;
    private final int CONNECT_COMMANDS_REQUEST_CODE = 1;
    private final int NOTIFY_LIST_REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = new Settings(this);

       // ((ScoutlinkApplication) getApplication()).getTracker(ScoutlinkApplication.TrackerName.APP_TRACKER);


        EditText et;

        et = (EditText)findViewById(R.id.settings_nickname);
        et.setText(settings.getString("nickname", ""));

        et = (EditText)findViewById(R.id.settings_ident);
        et.setText(settings.getString("ident", "androidirc"));

        et = (EditText)findViewById(R.id.settings_gecos);
        et.setText(settings.getString("gecos", "ScoutLink IRC for Android!"));

        et = (EditText)findViewById(R.id.settings_nickserv_user);
        et.setText(settings.getString("nickserv_user", ""));

        et = (EditText)findViewById(R.id.settings_nickserv_password);
        et.setText(settings.getString("nickserv_password", ""));

        et = (EditText)findViewById(R.id.settings_quit_message);
        et.setText(settings.getString("quit_message", getString(R.string.default_quit_message)));


    }

    public void openAutojoinSettings(View v) {
        Intent intent = new Intent(this, ListEditActivity.class);

        intent.putExtra("title", "Autojoin Channels");
        intent.putExtra("firstChar", "#");
        intent.putStringArrayListExtra("items", settings.getStringArrayList("autojoin_channels"));
        startActivityForResult(intent, AUTOJOIN_REQUEST_CODE);
    }

    public void openCommandOnConnectSettings(View v) {
        Intent intent = new Intent(this, ListEditActivity.class);

        intent.putStringArrayListExtra("items", settings.getStringArrayList("command_on_connect"));
        intent.putExtra("title", "Commands on Connect");
        intent.putExtra("firstChar", "/");
        startActivityForResult(intent, CONNECT_COMMANDS_REQUEST_CODE);
    }

    public void openNotifyListSettings(View v) {
        Intent intent = new Intent(this, ListEditActivity.class);

        intent.putStringArrayListExtra("items", settings.getStringArrayList("notify_list"));
        intent.putExtra("title", "Notify List");
        intent.putExtra("firstChar", "");
        startActivityForResult(intent, NOTIFY_LIST_REQUEST_CODE);
    }

    @Override
    public void onPause() {
        super.onPause();
        EditText et;
        et = (EditText)findViewById(R.id.settings_nickname);
        settings.putString("nickname", et.getText().toString()); // Validate here?

        et = (EditText)findViewById(R.id.settings_ident);
        settings.putString("ident", et.getText().toString());

        et = (EditText)findViewById(R.id.settings_gecos);
        settings.putString("gecos", et.getText().toString());

        et = (EditText)findViewById(R.id.settings_nickserv_user);
        settings.putString("nickserv_user", et.getText().toString());

        et = (EditText)findViewById(R.id.settings_nickserv_password);
        settings.putString("nickserv_password", et.getText().toString());

        et = (EditText)findViewById(R.id.settings_quit_message);
        settings.putString("quit_message", et.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AUTOJOIN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    settings.putStringArrayList("autojoin_channels", data.getStringArrayListExtra("items"));
                }
                break;
            case CONNECT_COMMANDS_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    settings.putStringArrayList("command_on_connect", data.getStringArrayListExtra("items"));
                }
                break;
            case NOTIFY_LIST_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    settings.putStringArrayList("notify_list", data.getStringArrayListExtra("items"));
                    ArrayList<String> newItems = data.getStringArrayListExtra("newItems");
                    ArrayList<String> removedItems = data.getStringArrayListExtra("removedItems");
                    Intent addNotify = new Intent(this, IRCService.class);
                    addNotify.setAction("ADD_NOTIFY");
                    addNotify.putStringArrayListExtra("items", newItems);
                    startService(addNotify);
                    Intent removeNotify = new Intent(this, IRCService.class);
                    removeNotify.setAction("REMOVE_NOTIFY");
                    removeNotify.putStringArrayListExtra("items", removedItems);
                    startService(removeNotify);
                }
                break;


        }
    }

}
