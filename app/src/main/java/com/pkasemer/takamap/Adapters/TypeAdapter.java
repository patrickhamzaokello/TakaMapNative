package com.pkasemer.takamap.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pkasemer.takamap.Models.Type;
import com.pkasemer.takamap.R;
import com.pkasemer.takamap.Utils.FilterCallBack;

import java.util.ArrayList;

public class TypeAdapter extends ArrayAdapter<Type> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    FilterCallBack mCallback;

    public TypeAdapter(Context context, ArrayList<Type> types,FilterCallBack mcallback) {
        super(context, R.layout.checkbox_filter, types);
        mCallback = mcallback;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Type type = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.checkbox_filter, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(type.getName());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.filterCallback(type.getName());
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}