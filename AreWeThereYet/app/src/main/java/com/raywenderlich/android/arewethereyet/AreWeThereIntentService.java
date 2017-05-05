package com.raywenderlich.android.arewethereyet;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import com.google.gson.Gson;

import java.util.Map;

public class AreWeThereIntentService extends IntentService {

  // region Properties

  private final String TAG = AreWeThereIntentService.class.getName();

  private SharedPreferences prefs;
  private Gson gson;

  // endregion

  // region Constructors

  public AreWeThereIntentService() {
    super("AreWeThereIntentService");
  }

  // endregion

  // region Overrides



  @Override
  protected void onHandleIntent(Intent i) {
    prefs = getApplicationContext().getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
    gson = new Gson();
    Location blocation=new Location(LocationManager.GPS_PROVIDER);
    blocation.setLatitude(MapsActivity.baggage.latitude);
    blocation.setLongitude(MapsActivity.baggage.longitude);
    //List<NamedGeofence> namedGeofences = new ArrayList<>();
    Map<String, ?> keys = prefs.getAll();
    for (Map.Entry<String, ?> entry : keys.entrySet())
    {
      String jsonString = prefs.getString(entry.getKey(), null);
      NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
      Location temp=new Location(LocationManager.GPS_PROVIDER);
      temp.setLongitude(namedGeofence.longitude);
      temp.setLatitude(namedGeofence.latitude);

      if(temp.distanceTo(blocation)<=namedGeofence.radius)
      {
        String contextText = String.format(this.getResources().getString(R.string.Notification_Text), namedGeofence.name);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AllGeofencesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(this.getResources().getString(R.string.Notification_Title))
                .setContentText(contextText)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);

      }
      //namedGeofences.add(namedGeofence);
    }



  }
}

 /* protected void onHandleIntent(Intent intent) {
    prefs = getApplicationContext().getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
    gson = new Gson();

    GeofencingEvent event = GeofencingEvent.fromIntent(intent);
    if (event != null) {
      if (event.hasError()) {
        onError(event.getErrorCode());
      } else {
        int transition = event.getGeofenceTransition();
        //if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {

        {List<String> geofenceIds = new ArrayList<>();
          for (Geofence geofence : event.getTriggeringGeofences()) {
            geofenceIds.add(geofence.getRequestId());
          }
          if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            onEnteredGeofences(geofenceIds);
          }
        }
      }
    }
  }

  // endregion

  // region Private

  private void onEnteredGeofences(List<String> geofenceIds) {
    Location blocation=new Location(LocationManager.GPS_PROVIDER);
    blocation.setLatitude(MapsActivity.baggage.latitude);
    for (String geofenceId : geofenceIds) {
      String geofenceName = "";

      // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference
      Map<String, ?> keys = prefs.getAll();
      for (Map.Entry<String, ?> entry : keys.entrySet()) {
        String jsonString = prefs.getString(entry.getKey(), null);
        NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
        Location location=new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(namedGeofence.latitude);
        location.setLongitude(namedGeofence.longitude);
        if (MapsActivity.) {

          break;
        }
      }


      // Set the notification text and send the notification
      String contextText = String.format(this.getResources().getString(R.string.Notification_Text), geofenceName);

      NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
      Intent intent = new Intent(this, AllGeofencesActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

      Notification notification = new NotificationCompat.Builder(this)
              .setSmallIcon(R.mipmap.ic_launcher)
              .setContentTitle(this.getResources().getString(R.string.Notification_Title))
              .setContentText(contextText)
              .setContentIntent(pendingNotificationIntent)
              .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
              .setPriority(NotificationCompat.PRIORITY_HIGH)
              .setAutoCancel(true)
              .build();
      notificationManager.notify(0, notification);

    }
  }

  private void onError(int i) {
    Log.e(TAG, "Geofencing Error: " + i);
  }

  // endregion*/




