package com.androidmax.max.hr.Employee.requestpkg;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidmax.max.hr.Employee.infoPkg.importatnt;
import com.androidmax.max.hr.R;
import com.androidmax.max.hr.keys;

public class other_details extends Fragment {
    vacationClass vacationObj;
    TextView status, type,Date,title,desc,rejectedNote,rejectedNoteView, Note;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.other_details,null);
        status=(TextView) v.findViewById(R.id.status);
        type=(TextView) v.findViewById(R.id.type);
        Date=(TextView) v.findViewById(R.id.date);
        title=(TextView) v.findViewById(R.id.title);
        desc=(TextView) v.findViewById(R.id.desc);
        Note=(TextView) v.findViewById(R.id.note);
        rejectedNote=(TextView) v.findViewById(R.id.rejected_note);
        rejectedNoteView=(TextView) v.findViewById(R.id.rejectedNoteView);
        Bundle bundle= getArguments();
        getActivity().setTitle(getString(R.string.another_request));

        vacationObj=(vacationClass)bundle.getSerializable(keys.OTHER);
         setView();

        return v;
    }
    public void setView(){
        title.setText(vacationObj.getvAmount());
        desc.setText(vacationObj.getvDescription());
        importatnt.check(vacationObj.getNote(),Note);
        Date.setText(vacationObj.getvStartDate());
        type.setText(vacationObj.getvSlice());
        String statustxt= vacationObj.getvStatues();
      //  status.setText(statustxt);
        String rejected= vacationObj.getvRejectedNote();

        if(rejected.equals("null")){
            rejectedNoteView.setVisibility(View.GONE);
            rejectedNote.setVisibility(View.GONE);
        }
        else{
            rejectedNote.setText(rejected);

        }
        getStatues(statustxt,status);
    }
    public void getStatues(String status, TextView statusView){
        if(status.equals(getString(R.string.pending))){
            statusView.setText(getString(R.string.pending));
            Drawable img = getResources().getDrawable( R.drawable.warning );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            statusView.setBackgroundColor(getResources().getColor(R.color.yellow));



        }
        else   if(status.equals(getString(R.string.rejected))){
            statusView.setText(getString(R.string.rejected));
            Drawable img =getResources().getDrawable( R.drawable.false_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );

            statusView.setBackgroundColor(getResources().getColor(R.color.red));

        }
        else
        {
            statusView.setText(getString(R.string.accept));
            Drawable img =getResources().getDrawable( R.drawable.true_icon );
            img.setBounds( 0, 0, 60, 60 );
            statusView.setCompoundDrawables( img, null, null, null );
            // v_status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.true_icon, 0, 0, 0);
            statusView.setBackgroundColor(getResources().getColor(R.color.green));

        }

    }}