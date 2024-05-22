package Package.WC;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePeriod_Activity extends AppCompatActivity {


    DatePicker first;
    DatePicker second;

    Button SaveDiapBut;

    Button BackBackBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_period_page);

        BackBackBut = findViewById(R.id.Back_But1);
        SaveDiapBut = findViewById(R.id.SaveDiapBut);
        first = (DatePicker) findViewById(R.id.Data1);
        second = findViewById(R.id.Data2);


        BackBackBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SaveDiapBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayString1 = "";
                String dayString2 = "";
                Integer day1 = first.getDayOfMonth();
                Integer day2 = second.getDayOfMonth();
                if (day1 < 10) {
                    dayString1 = "0" + String.valueOf(day1);
                } else {
                    dayString1 = String.valueOf(day1);
                }
                if (day2 < 10) {
                    dayString2 = "0" + String.valueOf(day2);
                } else {
                    dayString2 = String.valueOf(day2);
                }

                String monthString1 = "";
                String monthString2 = "";
                Integer month1 = first.getMonth();
                Integer month2 = second.getMonth();
                if (month1 < 10) {
                    monthString1 = "0" + String.valueOf(month1 + 1);
                } else {
                    monthString1 = String.valueOf(month1 + 1);
                }
                if (month2 < 10) {
                    monthString2 = "0" + String.valueOf(month2 + 1);
                } else {
                    monthString2 = String.valueOf(month2 + 1);
                }

                String fullDay1 = first.getYear() + "-" + monthString1 + "-" + dayString1;
                String fullDay2 = second.getYear() + "-" + monthString2 + "-" + dayString2;
                Intent intent = new Intent(ChangePeriod_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("firstDate", fullDay1);
                intent.putExtra("secondDate", fullDay2);
                startActivity(intent);
                Log.d("fullDate", fullDay1 + " ----- " + fullDay2);
            }
        });
    }
}