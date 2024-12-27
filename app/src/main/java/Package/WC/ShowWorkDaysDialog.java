package Package.WC;

import static Package.WC.DBHelper.WORK_ID;
import static Package.WC.DBHelper.WORK_TIME;
import static Package.WC.DBHelper.WORK_ZAGL;
import static Package.WC.DBHelper.WORK_ZP;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class ShowWorkDaysDialog extends AppCompatActivity implements  Removable{
    DBHelper dbHelper;
    Button[] smenabuttons;
    String[] nameButtons;
    Integer[] idOfSmensinBut;
    TextView AddDate;
    Button GoBackBut;

    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_smens);

        Bundle arguments = getIntent().getExtras();
        String ChosenDATEone = arguments.get("ChosenDate").toString();

        GoBackBut = (Button) findViewById(R.id.GoForCal);
        AddDate = (TextView) findViewById(R.id.ZagDial);

        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseMain = dbHelper.getWritableDatabase();

        smenabuttons = new Button[5];
        smenabuttons[0] = (Button) findViewById(R.id.button1);
        smenabuttons[1] = (Button) findViewById(R.id.button2);
        smenabuttons[2] = (Button) findViewById(R.id.button3);
        smenabuttons[3] = (Button) findViewById(R.id.button4);
        smenabuttons[4] = (Button) findViewById(R.id.button5);

        nameButtons = new String[10];
        idOfSmensinBut = new Integer[10];

        ListView smensList = findViewById(R.id.smensList);
        ArrayList<String> smens = new ArrayList<>();

        String selection;
        String[] selectionArgs;

        selectionArgs = new String[] {ChosenDATEone};
        selection = "Data_work_day = ?";

        String text = "";
        int SchetchikKola = 0;

        Cursor cursor = databaseMain.query(DBHelper.WORK_DAYS, null, selection, selectionArgs, null, null, null);
        AddDate.setText(ChosenDATEone);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(WORK_ID);
            int zagolo = cursor.getColumnIndex(WORK_ZAGL);
            int zp = cursor.getColumnIndex(WORK_ZP);
            int time = cursor.getColumnIndex(WORK_TIME);
            int x = 0;
            do {
                text += cursor.getInt(idIndex) + ". " + cursor.getString(zagolo) +
                        "\nЗаработок: " + cursor.getInt(zp) + "руб   Время: "
                        + cursor.getFloat(time) + "ч";
                nameButtons[x] = text;
                idOfSmensinBut[x] = cursor.getInt(idIndex);
                smens.add(text);
                x += 1;
                text = "";
                SchetchikKola += 1;
            } while (cursor.moveToNext());
        }
        cursor.close();

        int i = 0;
        while (i < SchetchikKola) {
            smenabuttons[i].setText(nameButtons[i]);
            //smenabuttons[i].setVisibility(View.VISIBLE);
            i += 1;
        }

        adapter = new ArrayAdapter<>(this, R.layout.text_layout, smens);
        smensList.setAdapter(adapter);

        smensList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSmen = adapter.getItem(position);

                int p = 0;
                String codesmeni = null;
                while (p < idOfSmensinBut.length) {
                    if (String.valueOf(selectedSmen).equals(nameButtons[p])) {
                        codesmeni = String.valueOf(idOfSmensinBut[p]);
                        break;
                    }
                    else {
                        p += 1;
                    }
                }
                DialogFragmentDel dialog = new DialogFragmentDel();
                Bundle args = new Bundle();
                args.putString("code", codesmeni);
                args.putString("text", selectedSmen);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");
            }
        });

        GoBackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ShowWorkDaysDialog.this, MainActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);
                //finish() ;
            }
        });
    }

    @Override
    public void remove(String name) {
        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseMain = dbHelper.getWritableDatabase();
        adapter.remove(name);
        String q = String.valueOf(name.charAt(0));
        databaseMain.delete(DBHelper.WORK_DAYS, WORK_ID + "=" + q, null);
    }
}
