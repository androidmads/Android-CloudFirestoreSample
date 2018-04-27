package com.androidmads.firestoresample;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore myDB;
    EditText edtData;
    ListView listView;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // InitViews
        edtData = findViewById(R.id.edt_hint);
        listView = findViewById(R.id.lv);
        // Init FireStore
        myDB = FirebaseFirestore.getInstance();
        readData();
    }

    void readData() {
        myDB.collection("tasks").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null)
                    toastResult(e.getMessage());
                list.clear();
                for (DocumentSnapshot doc : documentSnapshots) {
                    list.add(doc.getString("task_name"));
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.simple_list_item,
                        R.id.text1,
                        list);
                listView.setAdapter(arrayAdapter);
            }
        });
    }

    public void onAddClicked(View view) {
        hideKeyboard(this);
        if (edtData.getText().toString().length() > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("task_name", edtData.getText().toString());
           /* CollectionReference solarSystem = myDB.collection("myData");
            solarSystem.add(data);
            myDB.collection("myData").document("1").set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            toastResult("Data added successfully");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toastResult("Data add Completed");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastResult("Error while adding the data : " + e.getMessage());
                        }
                    });*/
            myDB.collection("tasks")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            toastResult("Data added successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastResult("Error while adding the data : " + e.getMessage());
                        }
                    });

        } else {
            edtData.setError("Value Required");
        }
    }

    public void onReadClicked(View view) {
        hideKeyboard(this);
        DocumentReference documentReference = myDB.collection("myData").document("1");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    edtData.setText(task.getResult().get("data").toString());
                }
            }
        });
    }

    public void onUpdateClicked(View view) {
        hideKeyboard(this);
        if (edtData.getText().toString().length() > 0) {
            Map<String, Object> data = new HashMap<>();
            data.put("data", edtData.getText().toString());
            myDB.collection("myData").document("1").update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            toastResult("Data updated successfully");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            toastResult("Data update Completed");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastResult("Error while updating the data : " + e.getMessage());
                        }
                    });
        } else {
            edtData.setError("Value Required");
        }
    }

    public void onDeleteClicked(View view) {
        hideKeyboard(this);
        myDB.collection("myData").document("1").delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        toastResult("Data deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastResult("Error while deleting the data : " + e.getMessage());
                    }
                });
    }

    public void toastResult(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}