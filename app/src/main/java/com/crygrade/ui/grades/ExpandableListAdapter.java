package com.crygrade.ui.grades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.crygrade.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter<T> extends BaseExpandableListAdapter {
	private Context context;
	private List<String> listDataHeader;
	private HashMap<String, List<T>> listHashMap;

	ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<T>> listHashMap) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listHashMap = listHashMap;
	}

	@Override
	public int getGroupCount() {
		return listHashMap.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return listHashMap.get(listDataHeader.get(groupPosition)).size();
	}

	@Override
	public String getGroup(int groupPosition) {
		return listDataHeader.get(groupPosition);
	}

	@Override
	public T getChild(int groupPosition, int childPosition) {
		return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = getGroup(groupPosition);
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			assert inflater != null;
			convertView = inflater.inflate(R.layout.grades_group, null);
		}
		TextView headerText = convertView.findViewById(R.id.grades_groupHeader);
		headerText.setText(headerTitle);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		String childInfo = getChild(groupPosition, childPosition).toString();
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			assert inflater != null;
			convertView = inflater.inflate(R.layout.grades_item, null);
		}
		TextView childText = convertView.findViewById(R.id.grades_groupItem);
		childText.setText(childInfo);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
