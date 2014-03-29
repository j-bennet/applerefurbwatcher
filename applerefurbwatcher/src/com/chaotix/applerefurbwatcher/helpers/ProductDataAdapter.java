package com.chaotix.applerefurbwatcher.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.chaotix.applerefurbwatcher.R;
import com.chaotix.applerefurbwatcher.data.ProductData;
import com.chaotix.applerefurbwatcher.interfaces.ICountListener;

public class ProductDataAdapter extends ArrayAdapter<ProductData> implements Filterable {

	private static final String tag = "ProductDataAdapter";
	private static final String wordSeparator = "\\s*,\\s*";
	
	private Context context;
	private int layoutResourceId;
	private ProductData[] data = null;
	private ProductData[] originalData = null;
	private ICountListener listener = null;
	
	Filter filter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults filterResults = new FilterResults();
			ArrayList<ProductData> tempList = new ArrayList<ProductData>();
			
			if (constraint != null && constraint.length() > 0) {
				
				String[] words = constraint.toString().split(wordSeparator);
				
				for (int i = 0; i < originalData.length; i++) {
					ProductData item = originalData[i];
					if (isMatch(item, words)) {
						tempList.add(item);
					}
					i++;
				}
				
				filterResults.values = tempList;
				filterResults.count = tempList.size();
			}
			else {
				// we'll check for null and revert to original data
				filterResults.values = null;
				filterResults.count = originalData.length;
			}
			
			
			return filterResults;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void publishResults(CharSequence contraint, FilterResults results) {
			
			if (results.values != null) {
				ArrayList<ProductData> dataList = (ArrayList<ProductData>) results.values;
				data = dataList.toArray(new ProductData[dataList.size()]);
				listener.updateCount(data.length);
			}
			else {
				data = originalData;
				listener.updateCount(data.length);
			}
			
			if (results.count > 0) {
				notifyDataSetChanged();
			}
			else {
				notifyDataSetInvalidated();
			}
		}
		
		private boolean isMatch(ProductData item, String[] words) {
			for (String word : words) {
				Pattern pattern = Pattern.compile("\\b" + word, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				
				boolean headerMatch = pattern.matcher(item.Header).find();
				boolean specsMatch = pattern.matcher(item.Specs).find();
				
				if (!headerMatch && !specsMatch)
					return false;
			}
			return true;
		}
	};
	
	public ProductDataAdapter(Context context, int layoutResourceId, ProductData[] data, ICountListener listener) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.originalData = data;
		this.listener = listener;
		this.listener.updateCount(data.length);
	}
	
	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public ProductData getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProductDataHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ProductDataHolder();
			holder.txtHeader = (TextView) row.findViewById(R.id.txtHeader);
			holder.txtSpecs = (TextView) row.findViewById(R.id.txtSpecs);
			holder.txtPrice = (TextView) row.findViewById(R.id.txtPrice);
			holder.txtSavings = (TextView) row.findViewById(R.id.txtSavings);

			row.setTag(holder);
		}
		else {
			holder = (ProductDataHolder) row.getTag();
		}
		
		//if (position < data.length) {
			ProductData item = data[position];
			holder.txtHeader.setText(item.Header);
			holder.txtSpecs.setText(item.Specs);
			holder.txtPrice.setText(Html.fromHtml(item.Price));
			if (item.Savings != null) {
				holder.txtSavings.setText(Html.fromHtml(item.Savings));
			}
		//}

		return row;
	}

	private static class ProductDataHolder {
		TextView txtHeader;
		TextView txtSpecs;
		TextView txtPrice;
		TextView txtSavings;
	}
}