package com.example.myfirstaidkit;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.TakeHours;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

class TreatmentsListAdapter<T> extends BaseExpandableListAdapter {

    Context context;
//    List<T> data;
    private int groupLaoyutId;
    private int childLayoutId;
    private LayoutInflater inflater;

    private List<Treatment> expandableListTitle;
    private HashMap<Treatment, List<Medicine>> expandableListDetail;

    DataBaseOperations dbo;

    public TreatmentsListAdapter(Context context, int groupLayoutId, int childLayoutId, List<Treatment> expandableListTitle, HashMap<Treatment, List<Medicine>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.groupLaoyutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
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
        dbo = DataBaseOperations.get_Instance(context);
        if (convertView == null) {
            convertView = inflater.inflate(groupLaoyutId, null);
        }

        final Treatment t = (Treatment) getGroup(groupPosition);

        TextView header = convertView.findViewById(R.id.treatment_item_header);
//        TextView text = convertView.findViewById(R.id.treatment_item_text);
        header.setText(t.getName());
//        text.setVisibility(View.GONE);
//        text.setText(t.getIdUser());
        ImageButton editImageView = convertView.findViewById(R.id.treatment_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Gson gson = new Gson();
                String treatment = null;
                String medicines = null;
                List<MedTretRel> list_rel = dbo.getRelations_treatmentId(t.getId());
                List<Medicine> list_med = new ArrayList<>();

                for (MedTretRel mtr : list_rel ) {
                    list_med.add(dbo.getMedicine_medicineId(mtr.getIdMedicine()));
                }

                String relations = null;
                try {
                    treatment = new JSONObject(gson.toJson(t)).toString();
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

        ImageButton deleteImageView = convertView.findViewById(R.id.treatment_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ApiCallThread<String>(new AsyncResponse<String>(){
                    @Override
                    public String apiCall(Object... params) {
                        return dbo.deleteTreatment((Treatment) params[1]);
                    }

                    @Override
                    public void processFinish(View v, String result) {
                        expandableListTitle.remove(t);
                        expandableListDetail.remove(t);
                        notifyDataSetChanged();
                    }
                }).execute(v, t);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
                convertView = inflater.inflate(childLayoutId, null);
        }
        final Medicine medicine = (Medicine) getChild(groupPosition, childPosition);
        List<MedTretRel> rels = dbo.getRelations_treatmentId(expandableListTitle.get(groupPosition).getId());
        MedTretRel rel = rels.get(childPosition);
        List<TakeHours> hours = rel.getHours();

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
        final Long current = cal.getTimeInMillis();
        Collections.sort(hours, new Comparator<TakeHours>(){
            public int compare(TakeHours hour1, TakeHours hour2) {
                // ## Ascending order
                Long current1 = hour1.getHour() - current;
                Long current2 = hour2.getHour() - current;

                if (current1 > 0 && current2 < 0) {
                    return hour2.getHour().compareTo(hour1.getHour());
                }
                return hour1.getHour().compareTo(hour2.getHour());
//                if (current1 < 0 && current2 < 0) {
//                    return hour1.getHour().compareTo(hour2.getHour());
//                } else if (current1 > 0 && current2 < 0) {
//                    return hour2.getHour().compareTo(hour1.getHour());
//                } else if (current1 < 0 && current2 > 0) {
//                    return hour1.getHour().compareTo(hour2.getHour());
//                } else if (current1 > 0 && current2 > 0) {
//                    return hour1.getHour().compareTo(hour2.getHour());
//                }
            }
        });

        String hora = new SimpleDateFormat("HH:mm").format(hours.get(0).getHour() - current);

        TextView expandedListHeader = convertView.findViewById(R.id.treatment_item_child_header);
        expandedListHeader.setText(medicine.getName());
        TextView expandedListText = convertView.findViewById(R.id.treatment_item_child_text);
        expandedListText.setText("Proxima toma en " + hora + " horas");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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