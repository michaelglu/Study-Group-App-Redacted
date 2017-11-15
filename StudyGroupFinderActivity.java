
//@author Michael Glushakov
package tfi.studygroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class StudyGroupFinderActivity extends AppCompatActivity {

    private EditText mCatView;
    private EditText mIDView;
    private EditText mNumView;
    private GroupChecker findChecker;//used to check if group names are valid
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_group_finder);
        mCatView = (EditText) findViewById(R.id.searchSubject);
        mIDView = (EditText) findViewById(R.id.searchbyID);
        mNumView = (EditText) findViewById(R.id.searchNumber);
        findChecker = new GroupChecker();


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void openSGListView(View view) {
        //Do something in response to button

        if (TextUtils.isEmpty(mCatView.getText())) {//avoids nullpointer in when category is left blank
            Context context = getApplicationContext();
            CharSequence text = "PLEASE ENTER COURSE SUBJECT";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if (TextUtils.isEmpty(mNumView.getText())) {//avoids nullpointer in when course number is left blank
            Context context = getApplicationContext();
            CharSequence text = "PLEASE ENTER COURSE NUMBER";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            String category = mCatView.getText().toString();
            int num = Integer.parseInt(mNumView.getText().toString());
            String id = "ID:";
            id = id + mIDView.getText().toString();
            if (GroupChecker.checkCategory(category)) {//avoids incorrect user input

                Intent intent = new Intent(this, FinderOutputActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("num", num);
                intent.putExtra("id", id);
                startActivity(intent);
            } else {
                Context context = getApplicationContext();
                CharSequence text = "INVALID CATEGORY";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

}


