package Package.WC;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.jar.Attributes;

public class AddWorkDay_Activity extends AppCompatActivity {

    TextView Title;
    Button[] ButtonfOfSm;

    Button AddWorkDayBut;
    String[] TextOfButtons;
    String[] ArrayForCheck;

    Integer[] ID_sm;
    EditText Chaevie;
    TextView NameText, TimeText, StavkaText;
    Button BackBut;

    DBHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addworkday_layout);

        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseMain = dbHelper.getWritableDatabase();

        NameText = (TextView) findViewById(R.id.NameText);
        TimeText = (TextView) findViewById(R.id.TimeText);
        StavkaText = (TextView) findViewById(R.id.StavkaText);

        AddWorkDayBut = (Button) findViewById(R.id.SaveCalBut);
        BackBut = (Button) findViewById(R.id.BackCalBut);
        Title = (TextView) findViewById(R.id.ShapkaTitle);

        Chaevie = (EditText) findViewById(R.id.ChaiEdit);

        ButtonfOfSm = new Button[10];
        ButtonfOfSm[0] = (Button) findViewById(R.id.but1add);
        ButtonfOfSm[1] = (Button) findViewById(R.id.but2add);
        ButtonfOfSm[2] = (Button) findViewById(R.id.but3add);
        ButtonfOfSm[3] = (Button) findViewById(R.id.but4add);
        ButtonfOfSm[4] = (Button) findViewById(R.id.but5add);
        ButtonfOfSm[5] = (Button) findViewById(R.id.but6add);
        ButtonfOfSm[6] = (Button) findViewById(R.id.but7add);
        ButtonfOfSm[7] = (Button) findViewById(R.id.but8add);
        ButtonfOfSm[8] = (Button) findViewById(R.id.but9add);
        ButtonfOfSm[9] = (Button) findViewById(R.id.but10add);

        TextOfButtons = new String[10];

        ID_sm = new Integer[1];

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayForCheck = new String[10];

        Bundle arguments = getIntent().getExtras();
        String ChosenDATE1 = arguments.get("ChosenDate").toString();
        Title.setText("Выберите шаблон на " + ChosenDATE1);


        String text = "";
        String textCheck = "";
        int SchetchikKola = 0;
        Cursor c = databaseMain.query(DBHelper.TABLE_OTCHET, null, null, null, null, null, null);
        if (c.moveToFirst()){
            int idIndex = c.getColumnIndex(DBHelper.KEY_ID);
            int StartIndex = c.getColumnIndex(DBHelper.KEY_START_TIME);
            int EndIndex = c.getColumnIndex(DBHelper.KEY_END_TIME);
            int zagolovokIndex = c.getColumnIndex(DBHelper.KEY_ZAGOLOVOK);
            int stavkaIndex = c.getColumnIndex(DBHelper.KEY_STAVKA);
            int TimeIndex = c.getColumnIndex(DBHelper.KEY_TIME);
            int x = 0;

            do {
                text += c.getString(zagolovokIndex) +
                        "\nСтавка:" + c.getInt(stavkaIndex) + "руб/ч     Время:"
                        + c.getFloat(TimeIndex) + "ч";
                textCheck += c.getInt(idIndex) + " " + c.getString(zagolovokIndex) + " " + c.getString(StartIndex) + " " + c.getString(EndIndex) + " " + c.getFloat(TimeIndex) + " " + c.getString(stavkaIndex);
                ArrayForCheck[x] = textCheck;
                Log.d("archeck", ArrayForCheck[x]);
                TextOfButtons[x] = text;
                x += 1;
                text = "";
                textCheck = "";
                SchetchikKola += 1;
            } while (c.moveToNext());
        } else
            Log.d("ТАБЛИЦА ШАБЛОНОВ", "0 rows");
        c.close();

        int i = 0;
        while (i < SchetchikKola) {
            ButtonfOfSm[i].setText(TextOfButtons[i]);
            ButtonfOfSm[i].setVisibility(View.VISIBLE);
            i += 1;
        }

        View.OnClickListener dudka = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < TextOfButtons.length; i += 1) {
                    int Q = v.getId();
                    if (ButtonfOfSm[i].getId() == Q) {
                        String[] words =ArrayForCheck[i].split(" ");
                        NameText.setText(words[1]);
                        TimeText.setText(words[2] + " - " + words[3] + " : " + words[4] + "ч");
                        StavkaText.setText(words[5] + " рублей в час");
                        ID_sm[0] = i;
                    }
                }
                //Log.d("O", String.valueOf(ID_sm[0]));
            }
        };

        ButtonfOfSm[0].setOnClickListener(dudka);
        ButtonfOfSm[1].setOnClickListener(dudka);
        ButtonfOfSm[2].setOnClickListener(dudka);
        ButtonfOfSm[3].setOnClickListener(dudka);
        ButtonfOfSm[4].setOnClickListener(dudka);
        ButtonfOfSm[5].setOnClickListener(dudka);
        ButtonfOfSm[6].setOnClickListener(dudka);
        ButtonfOfSm[7].setOnClickListener(dudka);
        ButtonfOfSm[8].setOnClickListener(dudka);
        ButtonfOfSm[9].setOnClickListener(dudka);



        BackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });

        AddWorkDayBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(NameText.getText()).isEmpty()) {
                    Toast notChoose = Toast.makeText(AddWorkDay_Activity.this, "Выберите шаблон выше", Toast.LENGTH_SHORT);
                    notChoose.show();
                }
                else {
                    int ID = Integer.parseInt(ArrayForCheck[ID_sm[0]].split(" ")[0]);
                    String selection = "_id = ?";
                    String[] selectionArgs = new String[] {String.valueOf(ID)};
                    Log.d("check", String.valueOf(ID));
                    Cursor G = databaseMain.query(DBHelper.TABLE_OTCHET, null, selection, selectionArgs, null, null, null);
                    float Time = 0;
                    int Stavka = 0;
                    String zagl = "";
                    if (G.moveToFirst()) {
                        int zaglInd = G.getColumnIndex(DBHelper.KEY_ZAGOLOVOK);
                        float timeInd = G.getColumnIndex(DBHelper.KEY_TIME);
                        int stavInd = G.getColumnIndex(DBHelper.KEY_STAVKA);
                        do {
                            zagl = G.getString(zaglInd);
                            Time = G.getFloat((int) timeInd);
                            Log.d("vremya", String.valueOf(G.getFloat((int) timeInd)));
                            Stavka = G.getInt(stavInd);
                            Log.d("stavka", String.valueOf(G.getInt(stavInd)));
                        } while (G.moveToNext());
                    }
                    float zp = Time * Stavka;
                    String Sverh  = Chaevie.getText().toString();
                    ContentValues cv = new ContentValues();
                    cv.put(DBHelper.WORK_ZAGL, zagl);
                    cv.put(DBHelper.WORK_DATA, ChosenDATE1);
                    cv.put(DBHelper.WORK_TIME, Time);
                    cv.put(DBHelper.WORK_ZP, zp);
                    cv.put(DBHelper.WORK_CHAI, Sverh);
                    database.insert(DBHelper.WORK_DAYS, null, cv);

                    Intent intent = new Intent(AddWorkDay_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}
