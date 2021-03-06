package me.nickpierson.StatsCalculatorFree.reference;

import me.nickpierson.StatsCalculator.R;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FreeBasicReferenceAdapter extends ArrayAdapter<FreeReferenceListItem> {

	private int resource;

	public FreeBasicReferenceAdapter(Context context, int resource) {
		super(context, resource);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listItemView = LayoutInflater.from(getContext()).inflate(resource, null);

		TextView title = (TextView) listItemView.findViewById(R.id.basic_reference_tvTitle);
		ImageView ivFormula = (ImageView) listItemView.findViewById(R.id.basic_reference_ivFormula);

		FreeReferenceListItem listItem = getItem(position);
		title.setText(listItem.getTitle());
		title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		ivFormula.setBackgroundResource(listItem.getImageId());

		return listItemView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
