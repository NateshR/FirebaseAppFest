package user.complaintchef.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nateshrelhan on 27/10/16.
 */

/**
 * @param <T> to attach firebase node/child/singleValue listener based on their type and example class
 */
public class FirebaseDataStoreFactory<T> {
    private static final String TAG = FirebaseDataStoreFactory.class.getSimpleName();

    public FirebaseDataStoreFactory() {
    }

    /**
     * @param exampleClass  for parsing data into
     * @param queryDatabase to attach child listener
     * @return
     */
    public void data(final Class<T> exampleClass, Query queryDatabase, final ChildCallBack<T> childCallBack) {
        Log.d(TAG, " --> attached child listener");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, " --> onChildAdded:" + dataSnapshot.getKey());
                T childNodeObject = dataSnapshot.getValue(exampleClass);
                childCallBack.onChildAdded(childNodeObject);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, " --> onChildChanged:" + dataSnapshot.getKey());
                T childNodeObject = dataSnapshot.getValue(exampleClass);
                childCallBack.onChildChanged(childNodeObject);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, " --> onChildRemoved:" + dataSnapshot.getKey());
                T childNodeObject = dataSnapshot.getValue(exampleClass);
                childCallBack.onChildRemoved(childNodeObject);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, " --> onChildMoved:" + dataSnapshot.getKey());
                T childNodeObject = dataSnapshot.getValue(exampleClass);
                childCallBack.onChildMoved(childNodeObject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, " --> onChildCancelled", databaseError.toException());
                childCallBack.onCancelled();
            }
        };
        queryDatabase.addChildEventListener(childEventListener);
    }

    /**
     * @param listenerType  valueEvent or singleValue
     * @param exampleClass  for parsing data into
     * @param queryDatabase to attach listener base on their type
     * @return
     */
    public void dataList(ListenerType listenerType, final Class<T> exampleClass, Query queryDatabase, final DataListCallBack dataListCallBack) {
        switch (listenerType) {
            case NODE:
                Log.d(TAG, " --> attached node listener");
                ValueEventListener nodeEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, " --> onDataChanged:" + dataSnapshot.getKey() + " --- " + dataSnapshot.getChildrenCount());
                        List<T> listOfEntity = new ArrayList<T>();
                        for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                            T rootNodeObject = singleDataSnapshot.getValue(exampleClass);
                            listOfEntity.add(rootNodeObject);
                        }
                        dataListCallBack.onDataChange(listOfEntity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, " --> onCancelled", databaseError.toException());
                        dataListCallBack.onCancelled();
                    }
                };
                queryDatabase.addValueEventListener(nodeEventListener);
            case SINGLE_NODE:
                ValueEventListener nodeSingleEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, " --> onDataChanged Single:" + dataSnapshot.getKey() + " --- " + dataSnapshot.getChildrenCount());
                        List<T> listOfEntity = new ArrayList<T>();
                        for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
                            T rootNodeObject = singleDataSnapshot.getValue(exampleClass);
                            listOfEntity.add(rootNodeObject);
                        }
                        dataListCallBack.onDataChange(listOfEntity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, " --> onCancelled Single", databaseError.toException());
                        dataListCallBack.onCancelled();
                    }
                };
                queryDatabase.addListenerForSingleValueEvent(nodeSingleEventListener);
        }
    }

    public enum ListenerType {
        NODE,
        SINGLE_NODE
    }

    public interface ChildCallBack<T> {
        void onChildAdded(T child);

        void onChildChanged(T child);

        void onChildRemoved(T child);

        void onChildMoved(T child);

        void onCancelled();
    }

    public interface DataListCallBack<T> {
        void onDataChange(List<T> dataList);

        void onCancelled();
    }
}
