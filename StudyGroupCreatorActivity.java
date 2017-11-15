//@author Michael Glushakov
package tfi.studygroup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class StudyGroupCreatorActivity extends AppCompatActivity {

    public static StudyGroup myGroup;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference myCatRef;
    DatabaseReference myContainerRef;
    SGContainer myContainer;
    private GroupChecker createChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_group_creator);
        createChecker = new GroupChecker();
    }

    public void createStudyGroup(View view) {
        // Do something in response to button
        User charlie = new User("charlie", "charlie", "911911911", "charlie@duke.edu", "password");//in the future will be replaced with user's credentials
        //objcts that corrsepond to textboxes
        EditText courseCategoryInput = (EditText) findViewById(R.id.sgCourseCat);
        EditText courseNumInput = (EditText) findViewById(R.id.sgCourseNum);
        EditText coursedurInput = (EditText) findViewById(R.id.sgDuration);
        EditText courseDetailsInput = (EditText) findViewById(R.id.SGCourseDetails);
        EditText courseMaxInput = (EditText) findViewById(R.id.sgMaxSize);
        //storing textbox input as variables
        if (TextUtils.isEmpty(courseCategoryInput.getText()) || TextUtils.isEmpty(courseNumInput.getText()) || TextUtils.isEmpty(coursedurInput.getText()) || TextUtils.isEmpty(courseMaxInput.getText())) {
            Context context = getApplicationContext();
            CharSequence text = "PLEASE FILL IN ALL FIELDS";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            String courseCategory = courseCategoryInput.getText().toString();
            int courseNum = Integer.parseInt(courseNumInput.getText().toString());
            double duration = Double.parseDouble(coursedurInput.getText().toString());
            String details = " ";
            int maxSize = Integer.parseInt(courseMaxInput.getText().toString());
            if (!TextUtils.isEmpty(courseDetailsInput.getText())) {//details can be empty, this avoids null pointer exception
                details = courseDetailsInput.getText().toString();
            }
            if (!GroupChecker.checkCategory(courseCategory)) {//checks that a category is vvalid to avoid incorrect inputs
                Context context = getApplicationContext();
                CharSequence text = "INVALID CATEGORY";
                int toastdur = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, toastdur);
                toast.show();
            } else {
                //get categotry child
                myCatRef = mRootRef.child(courseCategory);
                //get/create container child
                myContainerRef = myCatRef.child(courseCategory + " " + courseNum);
                //make new study group object
                StudyGroup newGroup = new StudyGroup(charlie.getMyUserName(), courseCategory, courseNum, duration, details, maxSize);

                //get the child of the container that is a map of all groups
                DatabaseReference currentGroups = myContainerRef.child("myGroups");
                //make a new child, key=id of the studygroup
                DatabaseReference fbGroup = currentGroups.child(newGroup.getMyID());
                //sets the value of that child to the newGroup object converted to FBGroup
                fbGroup.setValue(FBConverter.toFBStudyGroup(newGroup));
                myGroup = newGroup;//store local copy of the group on owner's phone

                Intent intent = new Intent(this, GroupHostInfo.class);//start the host info activity
                startActivity(intent);
            }
        }
    }


}
