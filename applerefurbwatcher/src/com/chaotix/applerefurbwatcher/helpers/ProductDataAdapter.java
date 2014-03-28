package com.chaotix.applerefurbwatcher.helpers;

import com.chaotix.applerefurbwatcher.R;
import com.chaotix.applerefurbwatcher.data.ProductData;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductDataAdapter extends ArrayAdapter<ProductData> {

	Context context;
	int layoutResourceId;
	ProductData data[] = null;

	public ProductDataAdapter(Context context, int layoutResourceId, ProductData[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProductDataHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ProductDataHolder();
			holder.txtSpecs = (TextView) row.findViewById(R.id.txtSpecs);
			holder.txtPrice = (TextView) row.findViewById(R.id.txtPrice);
			holder.txtSavings = (TextView) row.findViewById(R.id.txtSavings);

			row.setTag(holder);
		} else {
			holder = (ProductDataHolder) row.getTag();
		}

		ProductData item = data[position];
		holder.txtSpecs.setText(Html.fromHtml(item.Specs));
		holder.txtPrice.setText(Html.fromHtml(item.Price));
		if (item.Savings != null) {
			holder.txtSavings.setText(Html.fromHtml(item.Savings));
		}

		return row;
	}

	private static class ProductDataHolder {
		TextView txtSpecs;
		TextView txtPrice;
		TextView txtSavings;
	}
}