package utility;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vcbf.R;

public class ArrayAdapterListView extends BaseAdapter {
	private Context context;
	private ArrayList<String> categories;
	private ArrayList<Double> values;

	public ArrayAdapterListView(Context context, ArrayList<String> categories,
			ArrayList<Double> values) {
		this.context = context;
		this.categories = categories;
		this.values = values;
	}

	public int getCount() {
		return categories.size();
	}

	public Object getItem(int i) {
		return null;
	}

	public long getItemId(int i) {
		return 0;
	}

	private class Holder {
		TextView textViewCategory;
		TextView textViewValue;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_row, null);
		Holder holder = new Holder();
		holder.textViewCategory = (TextView) view.findViewById(R.id.left_text);
		holder.textViewValue = (TextView) view.findViewById(R.id.right_text);
		if(categories.get(position).length() > 25)
		{
			holder.textViewCategory.setText(categories.get(position).substring(0, 24) + "...");	
		}
		else
		{
			holder.textViewCategory.setText(categories.get(position));
		}
		
		
		holder.textViewValue.setText(String.valueOf(values.get(position) + "%"));

		return view;
	}
}
