package common.complaintcheflib.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Simar Arora on 21/06/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null || remoteMessage.getData() == null)
            return;
        Map<String, String> params =  remoteMessage.getData();
    }
}
