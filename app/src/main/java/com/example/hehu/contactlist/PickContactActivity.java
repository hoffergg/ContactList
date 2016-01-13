package com.example.hehu.contactlist;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PickContactActivity extends AppCompatActivity {
    private final static String TAG = PickContactActivity.class.getSimpleName();
    private final static int PICK_CONTACT_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent,PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();

                String idProjection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
                String hasNumberProjection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
                String numberProjection = ContactsContract.CommonDataKinds.Phone.NUMBER;
                String nameProjection = Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Contacts.DISPLAY_NAME;

                String[] projection = {idProjection,nameProjection,hasNumberProjection};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);

                cursor.moveToFirst();

                //Get the contactId from the selected contact.
                String contactId = cursor.getString(cursor.getColumnIndex(idProjection));
                String hasNumber = cursor.getString(cursor.getColumnIndex(hasNumberProjection));
                String name = cursor.getString(cursor.getColumnIndex(nameProjection));

                Log.d(TAG,"picked:" + contactId + "," + hasNumber + "," +name);

                //Query all the numbers from the selected contact.
                if(hasNumber.equalsIgnoreCase("1")){
                    Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    ,null
                    ,idProjection + " = "+ contactId
                    ,null
                    ,null);
                    Log.d(TAG,"number count:" + numberCursor.getCount());
                    String[] numbers = new String[numberCursor.getCount()];
                    int i = 0;
                    while (numberCursor.moveToNext()){
                        String nm  = numberCursor.getString(numberCursor.getColumnIndex(numberProjection));
                        String na  = numberCursor.getString(numberCursor.getColumnIndex(nameProjection));
                        String id  = numberCursor.getString(numberCursor.getColumnIndex(idProjection));
                        Log.d(TAG,id + "," + na + "," + nm);

                        numbers[i] = numberCursor.getString(numberCursor.getColumnIndex(numberProjection));
                        i++;
                    }
                    numberCursor.close();
                    showChooseDialog(numbers);
                }
                cursor.close();
            }
        }
    }

    public void showChooseDialog(String[] numbers){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select number");
        builder.setItems(numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}



