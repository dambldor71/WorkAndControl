package Package.WC;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteRedactActivity extends AppCompatActivity {


    Button DeleteButSm, BackBut, EditBut;

    DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttonsdo);

        BackBut = (Button) findViewById(R.id.BackBackBack);
        DeleteButSm = (Button) findViewById(R.id.DeleteButSm);
        EditBut = (Button) findViewById(R.id.EditButSm);

        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseH = dbHelper.getWritableDatabase();

        DeleteButSm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = getIntent().getExtras();
                Integer DELID = (Integer) arguments.get("DelID");
                databaseH.delete(DBHelper.TABLE_OTCHET, DBHelper.KEY_ID + "=" + DELID, null);
                //Log.d("Udali", String.valueOf(DELID));
                Intent back = new Intent(DeleteRedactActivity.this, MainActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
            }
        });

        EditBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editadd = new Intent(DeleteRedactActivity.this, AddSmena_Activity.class);
                Bundle Args = getIntent().getExtras();
                Integer EditID = (Integer) Args.get("DelID");
                String selection = "_id = ?";
                String[] selectionArgs = new String[] { String.valueOf(EditID) };
                String izagl = "";
                String tim1 = "";
                String tim2 = "";
                Integer stavochka = 0;

                Cursor CCC = databaseH.query(DBHelper.TABLE_OTCHET, null, selection, selectionArgs, null, null, null);
                if (CCC.moveToFirst()) {
                    int indexZagl = CCC.getColumnIndex(DBHelper.KEY_ZAGOLOVOK);
                    int index1Time = CCC.getColumnIndex(DBHelper.KEY_START_TIME);
                    int index2Time = CCC.getColumnIndex(DBHelper.KEY_END_TIME);
                    int indexStavka = CCC.getColumnIndex(DBHelper.KEY_STAVKA);

                    do {
                        izagl = CCC.getString(indexZagl);
                        tim1 = CCC.getString(index1Time);
                        tim2 = CCC.getString(index2Time);
                        stavochka = CCC.getInt(indexStavka);
                        Log.d("EDIT",  EditID + " " + CCC.getString(indexZagl) + " " + CCC.getString(index1Time) + " " + CCC.getString(index2Time) + " " + CCC.getInt(indexStavka));

                    } while (CCC.moveToNext());
                }
                editadd.putExtra("editID", EditID);
                editadd.putExtra("editZagl", izagl);
                editadd.putExtra("edit1Time", tim1);
                editadd.putExtra("edit2Time", tim2);
                editadd.putExtra("editStav", stavochka);
                Log.d("putExtra", EditID + " " + izagl + " " + tim1 + " " + tim2 + " " + stavochka);
                editadd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(editadd);
            }
        });



        BackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
