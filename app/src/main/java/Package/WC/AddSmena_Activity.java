package Package.WC;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AddSmena_Activity extends AppCompatActivity {

    Calendar time1 = Calendar.getInstance();
    Calendar time2 = Calendar.getInstance();
    Button StartTBut, EndTBut, DBAddBut, DBReadBut;

    EditText ZagolovokBox;
    EditText StavkaBox;
    TextView poleTimeStart, poleTimeEnd;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_smena_page);

        Button BackBackBut = findViewById(R.id.Back_But);
        StartTBut = (Button) findViewById(R.id.TimeEnterBut1);
        EndTBut = (Button) findViewById(R.id.TimeEnterBut2);
        DBAddBut = (Button) findViewById(R.id.AddDBBut);
        DBReadBut = (Button) findViewById(R.id.ReadDBBut);

        poleTimeStart = (TextView) findViewById(R.id.timeSTART);
        poleTimeEnd = (TextView) findViewById(R.id.timeEND);

        ZagolovokBox = (EditText) findViewById(R.id.NameEdit);
        StavkaBox = (EditText) findViewById(R.id.StavkaEdit);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        BackBackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final int[] IDID = {0};
        Bundle takeArgs = getIntent().getExtras();
        if (takeArgs != null) {
            //Integer editid = (Integer) takeArgs.get("editID");
            String editZagl = (String) takeArgs.get("editZagl");
            String edit1Time = (String) takeArgs.get("edit1Time");
            String edit2Time = (String) takeArgs.get("edit2Time");
            Integer editStav = (Integer) takeArgs.get("editStav");
            IDID[0] = (Integer) takeArgs.get("editID");
            ZagolovokBox.setText(editZagl);
            poleTimeStart.setText(edit1Time);
            poleTimeEnd.setText(edit2Time);
            StavkaBox.setText(String.valueOf(editStav));
            //Log.d("forEDIT", editid + " " + editZagl + " " + edit1Time + " " + edit2Time + " " + editStav);
        }

        Log.d("IDID", String.valueOf(IDID[0]));

        StartTBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddSmena_Activity.this, t1,
                        time1.get(Calendar.HOUR_OF_DAY),
                        time1.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        EndTBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddSmena_Activity.this, t2,
                        time2.get(Calendar.HOUR_OF_DAY),
                        time2.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        DBAddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zag = ZagolovokBox.getText().toString();
                String stav = StavkaBox.getText().toString();
                String timeSTR = poleTimeStart.getText().toString();
                String timeEND = poleTimeEnd.getText().toString();
                if (zag.length() == 0 || timeSTR.equals("время") || timeEND.equals("время") || stav.equals("")) {
                    Toast toast = Toast.makeText(AddSmena_Activity.this, "Введены не все данные!",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    String[] allPartsStart = timeSTR.split(" ");
                    String[] allPartsEnd = timeEND.split(" ");
                    //Log.d("allStart", allPartsStart[0]);
                    //Log.d("allEnd", allPartsEnd[0]);
                    float hourStart;
                    float hourEnd;
                    float intDayStart;
                    float intDayEnd;
                    hourStart = Integer.parseInt(allPartsStart[0].split(":")[0]);
                    hourEnd = Integer.parseInt(allPartsEnd[0].split(":")[0]);

                    if (Integer.parseInt(allPartsStart[0].split(":")[1]) > 15 && Integer.parseInt(allPartsStart[0].split(":")[1]) <= 45) {
                        intDayStart = (float) (hourStart + 0.5);
                    } else if (Integer.parseInt(allPartsStart[0].split(":")[1]) > 45) {
                        intDayStart = (float) (hourStart + 1);
                    } else { intDayStart = (float) (hourStart);}
                    if (Integer.parseInt(allPartsEnd[0].split(":")[1]) > 15 && Integer.parseInt(allPartsEnd[0].split(":")[1]) <= 45) {
                        intDayEnd = (float) (hourEnd + 0.5);
                    } else if (Integer.parseInt(allPartsEnd[0].split(":")[1]) > 45) {
                        intDayEnd = (float) (hourEnd + 1);
                    } else { intDayEnd = (float) (hourEnd); }
                    Log.d("+12time", intDayStart + " " + intDayEnd);

                    float AllTime;
                    if (intDayEnd - intDayStart <= 0) {
                        AllTime = intDayEnd - intDayStart + 24;
                    } else { AllTime = intDayEnd - intDayStart; };

                    ContentValues cv = new ContentValues();
                    cv.put(DBHelper.KEY_ZAGOLOVOK, zag);
                    cv.put(DBHelper.KEY_START_TIME, timeSTR);
                    cv.put(DBHelper.KEY_END_TIME, timeEND);
                    cv.put(DBHelper.KEY_TIME, AllTime);
                    cv.put(DBHelper.KEY_STAVKA, stav);
                    if (IDID[0] == 0) {
                        database.insert(DBHelper.TABLE_OTCHET, null, cv);
                        Toast qwerty1 = Toast.makeText(AddSmena_Activity.this, "Шаблон смены успешно создан", Toast.LENGTH_SHORT);
                        qwerty1.show();
                    }
                    else {
                        database.update(DBHelper.TABLE_OTCHET, cv, DBHelper.KEY_ID + "= ?", new String[] {String.valueOf(IDID[0])});
                        Toast qwerty2 = Toast.makeText(AddSmena_Activity.this, "Шаблон смены изменён", Toast.LENGTH_SHORT);
                        qwerty2.show();
                    }

                    Intent intent = new Intent(AddSmena_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Perenos_zagolovka", zag);
                    intent.putExtra("Vremya_starta", timeSTR);
                    intent.putExtra("Vremya_konca", timeEND);
                    intent.putExtra("Stavochka", stav);
                    startActivity(intent);
                }
            }
        });

        DBReadBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.query(DBHelper.TABLE_OTCHET, null, null, null, null, null, null);

                if (cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int zagolovokIndex = cursor.getColumnIndex(DBHelper.KEY_ZAGOLOVOK);
                    int StartTimeIndex = cursor.getColumnIndex(DBHelper.KEY_START_TIME);
                    int EndTimeIndex = cursor.getColumnIndex(DBHelper.KEY_END_TIME);
                    int stavkaIndex = cursor.getColumnIndex(DBHelper.KEY_STAVKA);
                    int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
                        do {
                            Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                    ", zag = " + cursor.getString(zagolovokIndex) +
                                    ", timeStart =" + cursor.getString(StartTimeIndex) +
                                    ", timeEnd =" + cursor.getString(EndTimeIndex) +
                                    ", stavka = " + cursor.getInt(stavkaIndex) + ", TIME = " + cursor.getInt(timeIndex));
                        } while (cursor.moveToNext());
                } else
                    Log.d("ТАБЛИЦА ШАБЛОНОВ", "0 rows");
                cursor.close();
            }
        });
    }


    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t1=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time1.set(Calendar.HOUR_OF_DAY, hourOfDay);
            time1.set(Calendar.MINUTE, minute);
            poleTimeStart.setText(DateUtils.formatDateTime(AddSmena_Activity.this, time1.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
        }
    };

    TimePickerDialog.OnTimeSetListener t2=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time2.set(Calendar.HOUR_OF_DAY, hourOfDay);
            time2.set(Calendar.MINUTE, minute);
            poleTimeEnd.setText(DateUtils.formatDateTime(AddSmena_Activity.this, time2.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
        }
    };
}