package com.jolpai.doctorsdiary.IO.activity;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.jolpai.doctorsdiary.R;
import com.jolpai.doctorsdiary.Realm_Model.PlanForMonth;
import com.jolpai.doctorsdiary.Worker.CalendarProcessor;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class MonthlyPlan extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    TextView txtDateIntent,txtDateContact;
    EditText editTextPersonName;
    static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_plan);

        txtDateIntent =(TextView) findViewById(R.id.txtDateIntent);
        txtDateContact=(TextView) findViewById(R.id.txtDateContact);
        editTextPersonName =(EditText)findViewById(R.id.editTextPersonName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        android.widget.Spinner spn_label = (android.widget.Spinner) findViewById(R.id.spinner_label);
        fragmentManager =getFragmentManager();

        String[] items = new String[3];

            items[0] = "Participant";
            items[1] = "Volunteer";
            items[2] = "Member";


        ArrayAdapter<String> adapter = new ArrayAdapter<>(MonthlyPlan.this, R.layout.x_row_span, items);
        adapter.setDropDownViewResource(R.layout.x_row_spn_dropdown);
        spn_label.setAdapter(adapter);

        txtDateIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MonthlyPlan.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

       // txtDateContact.setOnClickListener(this);

        saveData();
    }


    public void saveData(){
        Realm.init(this);

        Realm realm =Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){

            @Override
            public void execute(Realm bgRealm) {
                PlanForMonth planForMonth = bgRealm.createObject(PlanForMonth.class);
                planForMonth.setAppUser("Tanim Reja");
                planForMonth.setPlanDate("10/24/2016");
                planForMonth.setMonth("10");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.e("","");
            }

        },new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
            }
        });

        RealmResults<PlanForMonth> result = realm.where(PlanForMonth.class).findAll();
        PlanForMonth pfm= result.get(0);

        Log.e("realm",pfm.getAppUser()+"   "+result.size());


    }

    @Override
    public void onResume() {
        super.onResume();

        //DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");


        //if(dpd != null) dpd.setOnDateSetListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (v.getId()){
            case R.id.txtDateContact:

               // txtDateIntent.setText(date);
                //
                String name="Tanim";
                break;
            case R.id.txtDateIntent:


                break ;

        }
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        int id = view.getId();
        switch (id){
            case R.id.txtDateContact:

                txtDateIntent.setText(date);
                    //
            break;
            case R.id.txtDateIntent:            //

                txtDateContact.setText(date);
            break ;

        }


    }

    /**
     * @param view      The view associated with this listener.
     * @param hourOfDay The hour that was set.
     * @param minute    The minute that was set.
     * @param second    The second that was set
     */
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    static class  datepiaker implements DatePickerDialog.OnDateSetListener {

        public void showDialog(){

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.show(fragmentManager, "Datepickerdialog");
        }

        /**
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        }
    }



}
