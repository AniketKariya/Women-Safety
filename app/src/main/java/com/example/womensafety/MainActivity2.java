package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {

    ImageView profilePic;
    TextView name, email, location, lastUpdated;
    Button help, signout;
    FirebaseUser user;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAuxdD75U:APA91bFiDIKYfMdbRSLMhnAhfs_S2U9Tat-JXuuJW-rpufOiXZRFaC7tnw1w-v122tb6xLMUTRx8p_YGP9Qw18VcHpJ56PQtjNBTo7gat5fOIZSEdPjGn807LEil3W0Dc8nv6RxJYcGt";
    final private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // initialize components
        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);
        help = findViewById(R.id.btn_help);
        signout = findViewById(R.id.btn_signout);
        location = findViewById(R.id.tv_location);
        lastUpdated = findViewById(R.id.lastUpdated);

        // display user info
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String personName = user.getDisplayName();
            String personEmail = user.getEmail();
            Uri personPic = user.getPhotoUrl();

            name.setText(personName);
            email.setText(personEmail);
            Glide.with(this).load(String.valueOf(personPic)).into(profilePic);
        }

        // utilities
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);

                String Topic = "/topics/userABC";
                String notificationTitle = "Help";
                String notificationMessage = "Someone near you needs help!";

                JSONObject notification = new JSONObject();
                JSONObject notificationBody = new JSONObject();

                try {
                    notificationBody.put("title", notificationTitle);
                    notificationBody.put("message", notificationMessage);

                    notification.put("to", Topic);
                    notification.put("data", notificationBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendNotification(notification);
                Toast.makeText(MainActivity2.this, "Send Notification Called", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                updateUI();
            }
        });

        // fetch location
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity2.this, "Fetching Location...", Toast.LENGTH_SHORT).show();
                    checkPermissions();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(MainActivity2.this, 1);
                            } catch (IntentSender.SendIntentException ex) {
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getBundleExtra("Location");
                Location lastKnowLoc = (Location) b.getParcelable("Location");
                if (lastKnowLoc != null) {
                    Date date = new Date(lastKnowLoc.getTime());
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    format.setTimeZone(TimeZone.getDefault());
                    String formatted = format.format(date);

                    location.setText(lastKnowLoc.getLatitude() + ", " + lastKnowLoc.getLongitude());
                    lastUpdated.setText(formatted);

                    DatabaseReference DBuserInfo;
                    DBuserInfo = FirebaseDatabase.getInstance().getReference();

                    String id = DBuserInfo.push().getKey();
                    UserInfo userInfo = new UserInfo(user.getEmail(), lastKnowLoc.getLatitude(), lastKnowLoc.getLongitude(), lastKnowLoc.getTime());
                    DBuserInfo.child(user.getUid()).setValue(userInfo);
                } else {
                    Toast.makeText(context, "Location null", Toast.LENGTH_SHORT).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("LocationUpdate"));

        // For Firebase Cloud Messaging
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("FCM", "getInstance Failed", task.getException());
                    return;
                }

                // Get new Instance ID Token
                String token = task.getResult().getToken();

                // Log and Toast
                Log.d("FCM", "getInstance - " + token);
                Toast.makeText(MainActivity2.this, "Token - " + token, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i("Response", "onResponse: " + response.toString());
                },
                error -> {
                    Toast.makeText(MainActivity2.this, "Request Error", Toast.LENGTH_SHORT).show();
                    Log.i("Response", "onErrorResponse: Didn't work");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUI() {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkPermissions() {
        // GPS is on, but still need to check for permissions.
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startService();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            startService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    void startService() {
        Intent intent = new Intent(MainActivity2.this, LocationService.class);
        startService(intent);
    }
}