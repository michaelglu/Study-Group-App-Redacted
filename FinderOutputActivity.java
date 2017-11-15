//@author Michael Glushakov
package tfi.studygroup;

import android.os.Bundle;

import android.support.transition.ChangeBounds;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


public class FinderOutputActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myCatRef;
    DatabaseReference myContainerRef;
    DatabaseReference myGroupRef;
    String category;
    int coursenum;
    String inputID;
    String out;
    SGContainer groupContainer;

    private TextView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sglist_view2);
        mTestView = (TextView) findViewById(R.id.testerView);
        mTestView.setText("No Groups Found for" + category + " " + coursenum);
        category = getIntent().getStringExtra("category");
        coursenum = getIntent().getIntExtra("num", 0);
        inputID = getIntent().getStringExtra("id");
        //mTestView=(TextView) findViewById(R.id.testerView);
        //super.onStart();
        myCatRef = mRootRef.child(category);
        myContainerRef = myCatRef.child(category + " " + coursenum);
        myGroupRef = myContainerRef.child("myGroups");
        groupContainer = new SGContainer(category, coursenum);
        out = "Groups:\n";
        myGroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                FBStudyGroup fbSG = dataSnapshot.getValue(FBStudyGroup.class);
                groupContainer.add(FBConverter.toStudyGroup(fbSG));
                if (inputID.equals("ID:")) {
                    out += fbSG.myID + "\n";
                } else if (fbSG.myID.equals(inputID.substring(3))) {
                    out += fbSG.myID;
                }
                mTestView.setText(out);
                //At first this works like a forloop and gets each child 1 at a time
                //then it runs every single time a child is added
                //add front end code for listview here

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FBStudyGroup fbSG = dataSnapshot.getValue(FBStudyGroup.class);
                groupContainer.change(FBConverter.toStudyGroup(fbSG));
                //add front end code for changing a specific entry in the listview here


            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FBStudyGroup fbSG = dataSnapshot.getValue(FBStudyGroup.class);
                groupContainer.remove(FBConverter.toStudyGroup(fbSG));
                //add front end code to remove an entry from listview here
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mTestView.setText("Cancelled");

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }


}
