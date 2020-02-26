package com.example.myfirstaidkit;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
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
import java.util.HashMap;
import java.util.List;

class TreatmentsListAdapter<T> extends BaseExpandableListAdapter {

    Context context;
//    List<T> data;
    int style;
    private LayoutInflater inflater;

    private List<Treatment> expandableListTitle;
    private HashMap<Treatment, List<Medicine>> expandableListDetail;

    DataBaseOperations dbo;

    public TreatmentsListAdapter(Context context, int layoutId, List<Treatment> expandableListTitle, HashMap<Treatment, List<Medicine>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.style = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(style, null);
        }

        Treatment treatment = (Treatment) getGroup(groupPosition);

        TextView listTitleTextView = convertView.findViewById(R.id.treatment_item_header);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(treatment.getName());
        return convertView;

//        View vi = convertView;
//        us = DataBaseOperations.get_Instance(context);
//        if (vi == null) {
//            vi = inflater.inflate(style, null);
//        }
//
//        TextView header = vi.findViewById(R.id.treatment_item_header);
//        TextView text = vi.findViewById(R.id.treatment_item_text);
//        header.setText(((Treatment) data.get(groupPosition)).getName());
//        text.setText(((Treatment) data.get(groupPosition)).getIdUser());
//
//        ImageButton editImageView = vi.findViewById(R.id.treatment_item_edit);
//        editImageView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Gson gson = new Gson();
//
//                String treatment = null;
//                Treatment t = (Treatment) data.get(groupPosition);
//
//                String medicines = null;
//                List<MedTretRel> list_rel = us.getRelations_treatmentId(t.getId());
//                List<Medicine> list_med = new ArrayList<>();
//                for (MedTretRel mtr : list_rel ) {
//                    list_med.add(us.getMedicine_medicineId(mtr.getIdMedicine()));
//                }
//
//                String relations = null;
//
//                try {
//                    treatment = new JSONObject(gson.toJson(data.get(groupPosition))).toString();
//                    medicines = new JSONArray(gson.toJson(list_med)).toString();
//                    relations = new JSONArray(gson.toJson(list_rel)).toString();
//                } catch (Exception e) { }
//
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("isTreatmentEdit", true);
//                bundle.putString("treatment", treatment);
//                bundle.putString("medicines", medicines);
//                bundle.putString("relations", relations);
//
//                Navigation.findNavController(v).navigate(R.id.action_treatments_to_treatment_edit, bundle);
//            }
//        });
//
//        ImageButton deleteImageView = vi.findViewById(R.id.treatment_item_delete);
//        deleteImageView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                new ApiCallThread<String>(new AsyncResponse<String>(){
//                    @Override
//                    public String apiCall(Object... params) {
//                        return us.deleteTreatment((Treatment) params[1]);
//                    }
//
//                    @Override
//                    public void processFinish(View v, String result){
//                        remove(data.get(groupPosition));
//                        notifyDataSetChanged();
//                    }
//                }).execute(v, data.get(groupPosition));
//            }
//        });
//
//        return vi;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(style, null);
        }
        final Medicine medicine = (Medicine) getChild(groupPosition, childPosition);

        TextView expandedListTextView = convertView.findViewById(R.id.treatment_item_header);
        expandedListTextView.setText(medicine.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}