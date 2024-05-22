package Package.WC;



import static Package.WC.DBHelper.WORK_CHAI;
import static Package.WC.DBHelper.WORK_DATA;
import static Package.WC.DBHelper.WORK_ID;
import static Package.WC.DBHelper.WORK_TIME;
import static Package.WC.DBHelper.WORK_ZAGL;
import static Package.WC.DBHelper.WORK_ZP;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements  CalendarAdapter.OnItemListener {

    //СОЗДАНИЕ ПЕРЕМЕННЫХ
    ImageButton CalendarBut;
    ImageButton OtchetBut;
    ImageButton ShablonBut;
    ImageButton DopolnBut;
    ImageButton PlusSmBut;
    ImageButton ChangePeriodBut;
    ImageButton AddOtchetBut;

    Button prevMonthBut, nextMonthBut;
    Button AddOnBut;
    Button CheckWorkDaysBut;
    Button ShowWorkDays;
    Button GenerateBut;
    ConstraintLayout cal, otch, sha, dop;
    DBHelper dbHelper;

    TableLayout table1, table2, table3;
    Button holidaysBut, clockbut, politicsbut, massagebut;

    Button[] smenabuttons;
    String[] nameButtons;

    Integer[] idOfSmensinBut;
    LinearLayout ScrollSShab;
    String GlobalDate;

    TextView SelectedDatas;
    TextView DataText, KolSmens, AllSmens;
    TextView DataText2, KolChai, AllChai;
    TextView DataText3, KolZP, AllZP;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();


        smenabuttons = new Button[10];
        smenabuttons[0] = (Button) findViewById(R.id.but1);
        smenabuttons[1] = (Button) findViewById(R.id.but2);
        smenabuttons[2] = (Button) findViewById(R.id.but3);
        smenabuttons[3] = (Button) findViewById(R.id.but4);
        smenabuttons[4] = (Button) findViewById(R.id.but5);
        smenabuttons[5] = (Button) findViewById(R.id.but6);
        smenabuttons[6] = (Button) findViewById(R.id.but7);
        smenabuttons[7] = (Button) findViewById(R.id.but8);
        smenabuttons[8] = (Button) findViewById(R.id.but9);
        smenabuttons[9] = (Button) findViewById(R.id.but10);

        nameButtons = new String[10];
        idOfSmensinBut = new Integer[10];

        //ИНИЦИАЛИЗАЦИЯ ОБЪЕКТОВ ФАЙЛОВ XML
        CalendarBut = (ImageButton) findViewById(R.id.CalButton);
        OtchetBut = (ImageButton) findViewById(R.id.OtchetButton);
        ShablonBut = (ImageButton) findViewById(R.id.ShablonButton);
        DopolnBut= (ImageButton) findViewById(R.id.DopButton);
        cal = (ConstraintLayout) findViewById(R.id.cal_show_hide);
        otch = (ConstraintLayout) findViewById(R.id.otch_show_hide);
        sha = (ConstraintLayout) findViewById(R.id.shab_show_hide);
        dop = (ConstraintLayout) findViewById(R.id.dop_show_hide);
        //AddOtchetBut = (ImageButton) findViewById(R.id.OtchetAddBut);

        prevMonthBut = (Button) findViewById(R.id.prevMonthBut);
        nextMonthBut = (Button) findViewById(R.id.nextMonthBut);

        AddOnBut = (Button) findViewById(R.id.AddOnBut);

        PlusSmBut = (ImageButton) findViewById(R.id.ShabAddBut);
        ChangePeriodBut = (ImageButton) findViewById(R.id.ChangePeriodButton);

        CheckWorkDaysBut = (Button) findViewById(R.id.razrabBut);
        ScrollSShab = (LinearLayout) findViewById(R.id.VnutriScrolla);

        ShowWorkDays = (Button) findViewById(R.id.ShowWorkDays);

        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseMain = dbHelper.getWritableDatabase();

        SelectedDatas = (TextView) findViewById(R.id.SelectedDatas);
        GenerateBut = (Button) findViewById(R.id.GenerateBut);
        DataText = (TextView) findViewById(R.id.DataText);
        KolSmens = (TextView) findViewById(R.id.KolSmens);
        AllSmens = (TextView) findViewById(R.id.AllSmens);
        DataText2 = (TextView) findViewById(R.id.DataText2);
        KolChai = (TextView) findViewById(R.id.KolChai);
        AllChai = (TextView) findViewById(R.id.AllChai);
        DataText3 = (TextView) findViewById(R.id.DataText3);
        KolZP = (TextView) findViewById(R.id.KolZP);
        AllZP = (TextView) findViewById(R.id.AllZP);

        table1 = (TableLayout) findViewById(R.id.firstTable);
        table2 = (TableLayout) findViewById(R.id.secondTable);
        table3 = (TableLayout) findViewById(R.id.thirdTable);

        holidaysBut = (Button) findViewById(R.id.holidaysBut);
        clockbut = (Button) findViewById(R.id.FormClockBut);
        politicsbut = (Button) findViewById(R.id.politicsBut);
        massagebut = (Button) findViewById(R.id.MassageBut);

        // КОМАНДЫ ДЛЯ КНОПОК ДЛЯ ПЕРЕХОДА МЕЖДУ РАЗДЕЛАМИ
        OtchetBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.setVisibility(View.GONE);
                otch.setVisibility(View.VISIBLE);
                sha.setVisibility(View.GONE);
                dop.setVisibility(View.GONE);
            }
        });

        // ПРИГОДИТСЯ
        String startDate = "";
        String endDate = "";

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            startDate = String.valueOf(arguments.get("firstDate"));
            endDate = String.valueOf(arguments.get("secondDate"));
            if (startDate.equals("null") && endDate.equals("null")) {
                SelectedDatas.setText("Выберите диапазон дат в правом верхнем углу");
            } else {
                String showDate1 = startDate.split("-")[2] +"."+ startDate.split("-")[1] + "." + startDate.split("-")[0];
                String showDate2 = endDate.split("-")[2] +"."+ endDate.split("-")[1] + "." + endDate.split("-")[0];
                SelectedDatas.setText("Отчёт с " + showDate1 + " по " + showDate2);
            }
        }


        CalendarBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.setVisibility(View.VISIBLE);
                otch.setVisibility(View.GONE);
                sha.setVisibility(View.GONE);
                dop.setVisibility(View.GONE);

            }
        });
        ShablonBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.setVisibility(View.GONE);
                otch.setVisibility(View.GONE);
                sha.setVisibility(View.VISIBLE);
                dop.setVisibility(View.GONE);
            }
        });
        DopolnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.setVisibility(View.GONE);
                otch.setVisibility(View.GONE);
                sha.setVisibility(View.GONE);
                dop.setVisibility(View.VISIBLE);
            }
        });

        String finalEndDate = endDate;
        String finalStartDate = startDate;
        // Log.d("check", finalStartDate + " " + finalEndDate);
        String finalStartDate1 = startDate;
        String finalEndDate1 = endDate;
        GenerateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalStartDate1.isEmpty() && finalEndDate1.isEmpty()) {
                    Toast dati = Toast.makeText(MainActivity.this, "Укажите даты", Toast.LENGTH_SHORT);
                    dati.show(); }
                else {
                    table1.setVisibility(View.VISIBLE);
                    table2.setVisibility(View.VISIBLE);
                    table3.setVisibility(View.VISIBLE);
                    DataText.setVisibility(View.VISIBLE);
                    DataText2.setVisibility(View.VISIBLE);
                    DataText3.setVisibility(View.VISIBLE);
                    int schet = 0;
                    float alltime = 0;
                    int sum = 0;
                    int zp = 0;
                    String selection = "Data_work_day >= ? and Data_work_day <= ?";
                    String[] selectionArgs = new String[] {finalStartDate, finalEndDate};
                    Cursor cct = databaseMain.query(DBHelper.WORK_DAYS, null, selection, selectionArgs, null, null, "Data_work_day");
                    DataText.setText("");
                    DataText2.setText("");
                    DataText3.setText("");
                    if (cct.moveToFirst()) {
                        int indexDate = cct.getColumnIndex(DBHelper.WORK_DATA);
                        int indexId = cct.getColumnIndex(DBHelper.WORK_ID);
                        int indexChai = cct.getColumnIndex(DBHelper.WORK_CHAI);
                        int indexTime = cct.getColumnIndex(DBHelper.WORK_TIME);
                        int indexSum = cct.getColumnIndex(DBHelper.WORK_ZP);
                        int indexZagl = cct.getColumnIndex(DBHelper.WORK_ZAGL);

                        do {
                            DataText.append(cct.getString(indexDate) + " " + cct.getString(indexZagl) + "\n");
                            DataText2.append(cct.getString(indexDate) + "\n");
                            DataText3.append(cct.getString(indexDate) + "\n");
                            KolZP.append(cct.getInt(indexSum) + "\n");
                            KolSmens.append(cct.getFloat(indexTime) + "\n");
                            KolChai.append(cct.getInt(indexChai) + "\n");
                            schet += 1;
                            alltime += cct.getFloat(indexTime);
                            sum += cct.getInt(indexChai);
                            zp += cct.getInt(indexSum);
                            //Log.d("sorting", cct.getString(indexDate) + " " + cct.getInt(indexId));
                        } while (cct.moveToNext());
                    }
                    //Log.d("table", String.valueOf(DataText.getText()));
                    AllSmens.setText(schet + "      " + alltime);
                    AllChai.setText(String.valueOf(sum));
                    AllZP.setText(String.valueOf(zp));
                }
            }
        });

        PlusSmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSmena_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        ChangePeriodBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChangePeriod_Activity.class);
                startActivity(intent);
            }
        });

        String text = "";
        int SchetchikKola = 0;
        Cursor c = databaseMain.query(DBHelper.TABLE_OTCHET, null, null, null, null, null, null);
        if (c.moveToFirst()){
            int idIndex = c.getColumnIndex(DBHelper.KEY_ID);
            int zagolovokIndex = c.getColumnIndex(DBHelper.KEY_ZAGOLOVOK);
            int stavkaIndex = c.getColumnIndex(DBHelper.KEY_STAVKA);
            int TimeIndex = c.getColumnIndex(DBHelper.KEY_TIME);
            int x = 0;

            do {
                text += c.getString(zagolovokIndex) +
                        "\n Ставка:" + c.getInt(stavkaIndex) + "руб/ч     Время:"
                        + c.getFloat(TimeIndex) + "ч";
                nameButtons[x] = text;
                idOfSmensinBut[x] = c.getInt(idIndex);
                x += 1;
                text = "";
                SchetchikKola += 1;
            } while (c.moveToNext());
        } else
            Log.d("ТАБЛИЦА ШАБЛОНОВ", "0 rows");
        c.close();

        int i = 0;
        while (i < SchetchikKola) {
            smenabuttons[i].setText(nameButtons[i]);
            smenabuttons[i].setVisibility(View.VISIBLE);
            i += 1;
        }

        String[] q = {null};
        View.OnClickListener knopokdo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deladd = new Intent(MainActivity.this, DeleteRedactActivity.class);
                q[0] = String.valueOf(v.getId());
                int k = 0;
                int id = 0;
                while (k < 10) {
                    if (smenabuttons[k].getId() == v.getId()) {
                        id = k;
                        break;
                    }
                    else {
                        k+= 1;
                    }
                }
                //Log.d("IDvSpis", String.valueOf(id));
                deladd.putExtra("DelID", idOfSmensinBut[id]);
                deladd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(deladd);
            }
        };
        smenabuttons[0].setOnClickListener(knopokdo);
        smenabuttons[1].setOnClickListener(knopokdo);
        smenabuttons[2].setOnClickListener(knopokdo);
        smenabuttons[3].setOnClickListener(knopokdo);
        smenabuttons[4].setOnClickListener(knopokdo);
        smenabuttons[5].setOnClickListener(knopokdo);
        smenabuttons[6].setOnClickListener(knopokdo);
        smenabuttons[7].setOnClickListener(knopokdo);
        smenabuttons[8].setOnClickListener(knopokdo);
        smenabuttons[9].setOnClickListener(knopokdo);


        CheckWorkDaysBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialog = new CustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("zagol", CheckWorkDaysBut.getText().toString());
                args.putString("razrab", "Студенты 3 курса РГППУ:\nЧенский Сергей Валерьевич,\nКотенев Владислав Николаевич,\nКлимова Полина Андреевна");
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");

                Cursor cursor = databaseMain.query(DBHelper.WORK_DAYS, null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    int idId = cursor.getColumnIndex(WORK_ID);
                    int workdata = cursor.getColumnIndex(WORK_DATA);
                    int worktime = cursor.getColumnIndex(WORK_TIME);
                    int workzp = cursor.getColumnIndex(WORK_ZP);
                    int sverh = cursor.getColumnIndex(WORK_CHAI);
                    int zagl = cursor.getColumnIndex(WORK_ZAGL);
                    do {
                        Log.d("WORKD", cursor.getString(zagl) + " ID = " + cursor.getInt(idId) + " data = " + cursor.getString(workdata) +
                                " time = " + cursor.getInt(worktime) + " sum = " + cursor.getInt(workzp) + " chai = " + cursor.getInt(sverh));
                    } while (cursor.moveToNext());
                } else
                    Log.d("ТАБЛИЦА РАБ.ДНЕЙ", "V ETOI TABLICE NET STROK");
            }
        });

        holidaysBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialog1 = new CustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("zagol", holidaysBut.getText().toString());
                args.putString("razrab", "Основные праздничные дни:\n1 января - Новый год," +
                        "\n7 января - Рождество Христово," +
                        "\n23 февраля - День защитники Отечества," +
                        "\n8 марта - Международный женский день," +
                        "\n1 мая - Праздник Весны и Труда," +
                        "\n9 мая - День Победы," +
                        "\n12 июня - День России," +
                        "\n4 ноября - День народного единства");
                dialog1.setArguments(args);
                dialog1.show(getSupportFragmentManager(), "custom");
            }
        });

        clockbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialog2 = new CustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("zagol", clockbut.getText().toString());
                args.putString("razrab", "Установлен 24-часовой формат времени.\nДля настройки формата времени необходимо доработать приложение.");
                dialog2.setArguments(args);
                dialog2.show(getSupportFragmentManager(), "custom");
            }
        });
        politicsbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialog3 = new CustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("zagol", politicsbut.getText().toString());
                args.putString("razrab", "Мы ничего не крадем.");
                dialog3.setArguments(args);
                dialog3.show(getSupportFragmentManager(), "custom");
            }
        });

        massagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialog4 = new CustomDialogFragment();
                Bundle args = new Bundle();
                args.putString("zagol", massagebut.getText().toString());
                args.putString("razrab", "Данная функция недоступна(удивительно)... Скажи так.");
                dialog4.setArguments(args);
                dialog4.show(getSupportFragmentManager(), "custom");
            }
        });

        prevMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.minusMonths(1);
                ShowWorkDays.setVisibility(View.GONE);
                AddOnBut.setVisibility(View.GONE);
                setMonthView();
            }
        });

        nextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.plusMonths(1);
                ShowWorkDays.setVisibility(View.GONE);
                AddOnBut.setVisibility(View.GONE);
                setMonthView();
            }
        });

        AddOnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("OTP", GlobalDate);
                Intent intent = new Intent(MainActivity.this, AddWorkDay_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ChosenDate", GlobalDate);
                startActivity(intent);
            }
        });

        ShowWorkDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, ShowWorkDaysDialog.class);
                inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inte.putExtra("ChosenDate", GlobalDate);
                startActivity(inte);
            }
        });

    }

    String[] months = new String[] {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    String[] final_months = new String[] {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        int dateNum = 0;
        while (dateNum < 12) {
            if (months[dateNum].equals(monthYearFromDate(selectedDate).split(" ")[0])) {
                monthYearText.setText(final_months[dateNum] + " " + monthYearFromDate(selectedDate).split(" ")[1]);
                dateNum += 1;
            }
            dateNum += 1;

        }
        //Log.d("MMM", monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(7);
        String CheckYearMonth = firstOfMonth.toString();
        char[] dst = new char[8];
        CheckYearMonth.getChars(0, 8, dst, 0);
        //Log.d("CHECK1", String.valueOf(dst));
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        dbHelper = new DBHelper(this);
        SQLiteDatabase databaseMain = dbHelper.getWritableDatabase();

        Cursor cursor = databaseMain.query(DBHelper.WORK_DAYS, null, null, null, null, null, null);
        String Q = "";
        for (int  i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            }
            else {
                if (String.valueOf(i - dayOfWeek).length() == 1) {
                    Q = "0" + (i - dayOfWeek);
                }
                else {
                    Q = String.valueOf(i - dayOfWeek);
                }
                ArrayList<String> checkcheck = new ArrayList<String>();
                if (cursor.moveToFirst()){

                    int workdata = cursor.getColumnIndex(WORK_DATA);

                    do {
                        if (cursor.getString(workdata).equals(String.valueOf(dst) + Q)) {
                            //Log.d("WORKD", cursor.getString(workdata) + " = " + String.valueOf(dst) + Q);
                            checkcheck.add(String.valueOf(dst) + Q);
                        }
                    } while (cursor.moveToNext());
                }
                if (checkcheck.contains(String.valueOf(dst) + Q)) {
                    daysInMonthArray.add(Q + "\n" + "\uD83D\uDCBC");
                }
                else {
                    daysInMonthArray.add(Q);
                }
                //Log.d("CHECK2", String.valueOf(dst) + Q);
            }
        }
        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText) {
        String selectedMonth = monthYearFromDate(selectedDate).split(" ")[0];
        int i = 0;
        while (i < 12){
            if (months[i].equals(selectedMonth)) {
                break;
            }
            else {
                i += 1;
            }
        }
        String month = "";
        if (String.valueOf(i + 1).length() < 2) {
            month += "0" + (i + 1);
        }
        else {
            month = String.valueOf(i + 1);
        }
        String date = "";
        if (dayText.length() < 2) {
            date += "0" + dayText;
        }
        else {
            date = dayText;
        }
        GlobalDate = monthYearFromDate(selectedDate).split(" ")[1] + "-" + month + "-" + date.substring(0, 2);
        String message = "Выбрана дата " + dayText.substring(0, 2) + " " + monthYearFromDate(selectedDate);
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
        AddOnBut.setVisibility(View.VISIBLE);
        ShowWorkDays.setVisibility(View.VISIBLE);
    }
}
