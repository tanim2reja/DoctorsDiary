package com.jolpai.doctorsdiary.IO.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.jolpai.doctorsdiary.App;
import com.jolpai.doctorsdiary.R;
import com.jolpai.doctorsdiary.IO.custom_view.MyStyle;
import com.jolpai.doctorsdiary.Worker.GetData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

import static com.jolpai.doctorsdiary.App.TAG;
import static com.jolpai.doctorsdiary.App.currentTime;

public class DailyReport extends AppCompatActivity {
    LinearLayout llReportHeader,llDate;
    RippleView llFooterDate;

    RecyclerView horizontalRecycler;
    LinearLayoutManager horizontalLayoutManager;
    int rowRecyclerView;
    int reportPortrait,reportLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(App.TAG,"DailyReport");

        hideStatusBar();
        checkOrientation();
        //initialize();

    }


    private void hideStatusBar() {
        if(Build.VERSION.SDK_INT >= 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void initialize() {
        Log.e(App.TAG, App.currentTime());

        horizontalRecycler = (RecyclerView) findViewById(R.id.horizontalRecycler);

        final ArrayList<com.jolpai.doctorsdiary.Realm_Model.DailyReport> horizontalList=new ArrayList<>();
        final RealmResults<com.jolpai.doctorsdiary.Realm_Model.DailyReport> reportList =
                GetData.getDataFromRealm(this, (Class)com.jolpai.doctorsdiary.Realm_Model.DailyReport.class);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR));
        c.set(Calendar.MONTH, c.get(Calendar.MONTH));

         int year = c.get(Calendar.YEAR);
         int month = c.get(Calendar.MONTH)+1;
         int dd = c.get(Calendar.DAY_OF_MONTH);
        int numDays = c.getActualMaximum(Calendar.DATE);

        for (int i=0;i<reportList.size();i++){

            horizontalList.add(reportList.get(i));
            if(i==reportList.size()-1){
                for(;i<numDays;i++){
                    com.jolpai.doctorsdiary.Realm_Model.DailyReport report=new com.jolpai.doctorsdiary.Realm_Model.DailyReport();
                    report.setToDay(i+1);
                    report.setYear(year);
                    report.setMonth(month);
                    horizontalList.add(report);
                }
            }
        }
        if(reportList.size()==0){
            for(int i=0;i<numDays;i++){
                com.jolpai.doctorsdiary.Realm_Model.DailyReport report=new com.jolpai.doctorsdiary.Realm_Model.DailyReport();
                report.setToDay(i+1);
                report.setYear(year);
                report.setMonth(month);
                horizontalList.add(report);
            }
        }

        horizontalRecycler.setLayoutManager(horizontalLayoutManager);
        horizontalRecycler.setAdapter( new Recycler_View_Adapter(horizontalList,this));


        Log.e(App.TAG, App.currentTime());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapter = new HorizontalRecyclerAdapter(horizontalList,DailyReport.this);
               // horizontalRecycler.setAdapter( new HorizontalRecyclerAdapter(horizontalList,DailyReport.this));

                Log.e(App.TAG,"call adapter"+ App.currentTime());

            }
        }, 100);
 

    }

    // check the device orientation and choose layout file.
    private void checkOrientation(){

        int orientation= DailyReport.this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_report_portrait);
            rowRecyclerView=R.layout.row_portrait;
            horizontalLayoutManager = new LinearLayoutManager(DailyReport.this,LinearLayoutManager.VERTICAL,false);

        }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_report_landscape);
            rowRecyclerView=R.layout.row_landscape;
           horizontalLayoutManager = new LinearLayoutManager(DailyReport.this,LinearLayoutManager.HORIZONTAL,false);
        }
    }
    private void setWidthAndHeight(){
        llReportHeader = (LinearLayout) findViewById(R.id.llReportHeader);

        llDate =(LinearLayout)findViewById(R.id.llDate);
        llFooterDate =(RippleView) findViewById(R.id.llFooterDate);


       final LinearLayout layout = llDate; //(LinearLayout)findViewById(R.id.YOUD VIEW ID);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();

            }
        });

        ViewGroup.LayoutParams params = llFooterDate.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        // params.height = 111;
        // params.width = 100;
        llFooterDate.setLayoutParams(params);

    }

    public  void pdfGenerate(View v){
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
    }

    @Override
    public void onResume(){
        super.onResume();
        initialize();
    }

// inner Adapter class for recyclerview
    public class Recycler_View_Adapter  extends RecyclerView.Adapter<Recycler_View_Adapter.View_Holder> {
        private List<com.jolpai.doctorsdiary.Realm_Model.DailyReport> list;
        private Context context;

        public Recycler_View_Adapter(List<com.jolpai.doctorsdiary.Realm_Model.DailyReport> list, Context context) {
            this.list = list;
            this.context = context;
        }


        public class View_Holder extends RecyclerView.ViewHolder{

            RippleView llFooterDate;
            TextView txtDate;
            TextView
                    txtProfessionalWorkd,
                    txtAcademicStudy,
                    txtHadithStudy,
                    txtLiteratureStudy,
                    txtSalatWithJamaat,
                    txtParticipantIntentContact,
                    txtVolunteerIntentContact,
                    txtMemberIntentContact,
                    txtContact,
                    txtBookDistribution,
                    txtSocietyWork;
            CheckBox
                    checkQuranStudy,
                    checkFamilyMeeting,
                    checkVisit,
                    checkReportKeeping,
                    checkSelfAssessment;

            public View_Holder (View itemView){
                super(itemView);
                txtDate =(TextView) itemView.findViewById(R.id.txtDate);
                txtProfessionalWorkd=(TextView) itemView.findViewById(R.id.txtProfessionalWork);
                txtAcademicStudy=(TextView) itemView.findViewById(R.id.txtAcademicStudy);
                txtHadithStudy=(TextView) itemView.findViewById(R.id.txtHadithStudy);
                txtLiteratureStudy=(TextView) itemView.findViewById(R.id.txtLiteratureStudy);
                txtSalatWithJamaat=(TextView) itemView.findViewById(R.id.txtSalatWithJamaat);
                txtParticipantIntentContact=(TextView) itemView.findViewById(R.id.txtParticipantIntentContact);
                txtVolunteerIntentContact=(TextView) itemView.findViewById(R.id.txtVolunteerIntentContact);

                txtMemberIntentContact=(TextView) itemView.findViewById(R.id.txtMemberIntentContact);
                txtContact=(TextView) itemView.findViewById(R.id.txtContact);
                txtBookDistribution=(TextView) itemView.findViewById(R.id.txtBookDistribution);
                txtSocietyWork=(TextView) itemView.findViewById(R.id.txtSocietyWork);

                checkQuranStudy=(CheckBox) itemView.findViewById(R.id.checkQuranStudy);
                checkFamilyMeeting=(CheckBox) itemView.findViewById(R.id.checkFamilyMeeting);
                checkVisit=(CheckBox) itemView.findViewById(R.id.checkVisit);
                checkReportKeeping=(CheckBox) itemView.findViewById(R.id.checkReportKeeping);
                checkSelfAssessment=(CheckBox) itemView.findViewById(R.id.checkSelfAssessment);
                llFooterDate=(RippleView) this.itemView.findViewById(R.id.llFooterDate);

            }
        }

        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View v = LayoutInflater.from(parent.getContext()).inflate(rowRecyclerView, parent, false);
            View_Holder holder = new View_Holder(v);

            return holder;

        }

        @Override
        public void onBindViewHolder(final View_Holder holder, int position) {

            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            final com.jolpai.doctorsdiary.Realm_Model.DailyReport dailyReport = list.get(position);
            holder.txtDate.setText(dailyReport.getToDay()+"");
            holder.txtDate.setBackground(new MyStyle().getShape());

            if(dailyReport.getProfessionalWork() == 0) {
                holder.txtProfessionalWorkd.setText("");
            }else{
                holder.txtProfessionalWorkd.setText(dailyReport.getProfessionalWork() + "");
            }

            if(dailyReport.getAcademicStudy() == 0) {
                holder.txtAcademicStudy.setText("");

            }else{
                holder.txtAcademicStudy.setText(dailyReport.getAcademicStudy() + "");
            }

            if(dailyReport.isQuranStudy()){
                holder.checkQuranStudy.setChecked(dailyReport.isQuranStudy());
                holder.checkQuranStudy.setVisibility(View.VISIBLE);
            }else{
                holder.checkQuranStudy.setChecked(false);
                holder.checkQuranStudy.setVisibility(View.GONE);
            }

            if(dailyReport.getHadithStudy()==0){
                holder.txtHadithStudy.setText("");
            }else{
                holder.txtHadithStudy.setText(dailyReport.getHadithStudy()+"");
            }


            if(dailyReport.getLiteratureStudy()==0){
                holder.txtLiteratureStudy.setText("");
            }else{
                holder.txtLiteratureStudy.setText(dailyReport.getLiteratureStudy()+"");
            }

            if(dailyReport.getSalatwithJamaat() == 0){
                holder.txtSalatWithJamaat.setText("");
            }else{
                holder.txtSalatWithJamaat.setText(dailyReport.getSalatwithJamaat()+"");
            }

            if(dailyReport.getParticipantIntentContact()==0){
                holder.txtParticipantIntentContact.setText("");
            }else{
                holder.txtParticipantIntentContact.setText(dailyReport.getParticipantIntentContact()+"");
            }

            if(dailyReport.getVolunteerIntentContact()==0){
                holder.txtVolunteerIntentContact.setText("");
            }else{
                holder.txtVolunteerIntentContact.setText(dailyReport.getVolunteerIntentContact()+"");
            }

            if(dailyReport.getMemberIntentContact()==0){
                holder.txtMemberIntentContact.setText("");
            }else{
                holder.txtMemberIntentContact.setText(dailyReport.getMemberIntentContact()+"");
            }

            if(dailyReport.getContact()==0){
                holder.txtContact.setText("");
            }else{
                holder.txtContact.setText(dailyReport.getContact()+"");
            }

            if(dailyReport.getBookDistribution()==0){
                holder.txtBookDistribution.setText("");
            }else{
                holder.txtBookDistribution.setText(dailyReport.getBookDistribution()+"");
            }

            if(dailyReport.isFamilyMeeting()){
                holder.checkFamilyMeeting.setChecked(dailyReport.isFamilyMeeting());
                holder.checkFamilyMeeting.setVisibility(View.VISIBLE);
            }else{
                holder.checkFamilyMeeting.setChecked(false);
                holder.checkFamilyMeeting.setVisibility(View.GONE);
            }

            if(dailyReport.getSocietyWork()==0.0){
                holder.txtSocietyWork.setText("");
            }else{
                holder.txtSocietyWork.setText(dailyReport.getSocietyWork()+"");
            }

            if(dailyReport.isVisit()) {
                holder.checkVisit.setChecked(dailyReport.isVisit());
            }else{
                holder.checkVisit.setChecked(false);
                holder.checkVisit.setVisibility(View.GONE);
            }

            if(dailyReport.isReportKeeping()) {
                holder.checkReportKeeping.setChecked(dailyReport.isReportKeeping());
                holder.checkReportKeeping.setVisibility(View.VISIBLE);
            }else{
                holder.checkReportKeeping.setChecked(dailyReport.isReportKeeping());
                holder.checkReportKeeping.setVisibility(View.GONE);
            }

            if(dailyReport.isSelfAssessment()) {
                holder.checkSelfAssessment.setChecked(dailyReport.isSelfAssessment());
                holder.checkSelfAssessment.setVisibility(View.VISIBLE);
            }else{
                holder.checkSelfAssessment.setChecked(false);
                holder.checkSelfAssessment.setVisibility(View.GONE);
            }



            // Log.e(TAG,holder.toString());
            Log.e(TAG,"onBindViewHolder"+position);
            Log.e(TAG, currentTime());
            // holder.description.setText(list.get(position).description);
            // holder.imageView.setImageResource(list.get(position).imageId);

            //animate(holder);
            holder.llFooterDate.setTag(dailyReport);
            holder.llFooterDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,holder.txtDate.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,ReportAddEditComment.class);
                    com.jolpai.doctorsdiary.Realm_Model.DailyReport report=(com.jolpai.doctorsdiary.Realm_Model.DailyReport)holder.llFooterDate.getTag();

                    intent.putExtra("year",report.getYear()+"");
                    intent.putExtra("month",report.getMonth()+"");
                    intent.putExtra("day",report.getToDay()+"");
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


    }

}