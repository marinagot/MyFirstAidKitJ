package com.example.myfirstaidkit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TreatmentsListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    List<T> data;
    int style;
    private static LayoutInflater inflater = null;

    DataBaseOperations us;

    public TreatmentsListAdapter(Context context, int layoutId, List<T> data) {
        // TODO Auto-generated constructor stub
        super(context, 0 , data);
        this.context = context;
        this.data = data;
        this.style = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        us = DataBaseOperations.get_Instance(getContext());
        if (vi == null) {
            vi = inflater.inflate(style, null);
        }

        TextView header = vi.findViewById(R.id.treatment_item_header);
        TextView text = vi.findViewById(R.id.treatment_item_text);
        header.setText(((Treatment) data.get(position)).getName());
        text.setText(((Treatment) data.get(position)).getIdUser());

        ImageButton editImageView = vi.findViewById(R.id.treatment_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Gson gson = new Gson();

                String treatment = null;
                Treatment t = (Treatment) data.get(position);

                String medicines = null;
                List<MedTretRel> list_rel = us.getRelations_treatmentId(t.getId());
                List<Medicine> list_med = new ArrayList<>();
                for (MedTretRel mtr : list_rel ) {
                    list_med.add(us.getMedicine_medicineId(mtr.getIdMedicine()));
                }

                String relations = null;

                try {
                    treatment = new JSONObject(gson.toJson(data.get(position))).toString();
                    medicines = new JSONArray(gson.toJson(list_med)).toString();
                    relations = new JSONArray(gson.toJson(list_rel)).toString();
                } catch (Exception e) { }

                Bundle bundle = new Bundle();
                bundle.putBoolean("isTreatmentEdit", true);
                bundle.putString("treatment", treatment);
                bundle.putString("medicines", medicines);
                bundle.putString("relations", relations);

                Navigation.findNavController(v).navigate(R.id.action_treatments_to_treatment_edit, bundle);
            }
        });

        ImageButton deleteImageView = vi.findViewById(R.id.treatment_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ApiCallThread<String>(new AsyncResponse<String>(){
                    @Override
                    public String apiCall(Object... params) {
                        return us.deleteTreatment((Treatment) params[1]);
                    }

                    @Override
                    public void processFinish(View v, String result){
                        remove(data.get(position));
                        notifyDataSetChanged();
                    }
                }).execute(v, data.get(position));
            }
        });

        return vi;
    }
}