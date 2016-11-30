package activity_and_fragment;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import utility.ArrayAdapterListView;
import utility.Data;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentBCFPerformance extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bcf_performance,
				container, false);
		if (container == null) {
			return null;
		}
		TextView textViewDate = (TextView) rootView
				.findViewById(R.id.table_date);
		TextView textViewNetAssetValue = (TextView) rootView
				.findViewById(R.id.table_netassetvalue);
		TextView textViewTurnOverRate = (TextView) rootView
				.findViewById(R.id.table_turnoverrate);
		TextView textViewTotalPosition = (TextView) rootView
				.findViewById(R.id.table_totalposition);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date maxDate1 = new Date(0, 0, 0);

		for (int i = 0; i < Data.portfolioStatisticsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioStatisticsBCF
						.get(i).date);
				if (tempDate.after(maxDate1)) {
					maxDate1 = tempDate;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Data.portfolioStatisticsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioStatisticsBCF
						.get(i).date);
				if (tempDate.compareTo(maxDate1) == 0) {
					DecimalFormat format1 = new DecimalFormat("#0,000");
					DecimalFormat format2 = new DecimalFormat("#0.00");
					DecimalFormat format3 = new DecimalFormat("#0");
					textViewDate.setText(rootView.getResources().getString(
							R.string.asOf)
							+ " " + tempDate.toGMTString());
					textViewNetAssetValue
							.setText(format1.format(Data.portfolioStatisticsBCF
									.get(i).net_asset_value));
					textViewTurnOverRate
							.setText(format2.format(Data.portfolioStatisticsBCF
									.get(i).turnover_rate));
					textViewTotalPosition
							.setText(format3.format(Data.portfolioStatisticsBCF
									.get(i).total_position));
				}
			} catch (ParseException e) {
				Log.d("Loi Parse", e.getMessage());
			}
		}
		ArrayList<Integer> listColor = colorGenerator();
		LinearLayout layoutChart = (LinearLayout) rootView
				.findViewById(R.id.fund_performance_chart);
		ArrayList<String> ib_industries = new ArrayList<String>();
		ArrayList<Double> ib_percentages = new ArrayList<Double>();
		Date maxDate = new Date(0, 0, 0);
		for (int i = 0; i < Data.industryBreakdownBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.industryBreakdownBCF
						.get(i).date);
				if (tempDate.after(maxDate)) {
					maxDate = tempDate;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Data.industryBreakdownBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.industryBreakdownBCF
						.get(i).date);
				if (tempDate.compareTo(maxDate) == 0) {
					if (Data.settings[0] == 0) {
						ib_industries
								.add(Data.industryBreakdownBCF.get(i).industry);
					} else {
						ib_industries
								.add(Data.industryBreakdownBCF.get(i).industry_vn);
					}
					ib_percentages
							.add(Data.industryBreakdownBCF.get(i).percentage);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		drawChart(rootView, layoutChart, ib_industries, ib_percentages,
				listColor);
		ArrayAdapterListView customArrayAdapter = new ArrayAdapterListView(
				getActivity(), ib_industries, ib_percentages);
		ListView listView = (ListView) rootView
				.findViewById(R.id.fund_performance_table);
		listView.setAdapter(customArrayAdapter);
		setListViewHeightBasedOnChildren(listView);
		ArrayList<String> pd_assets = new ArrayList<String>();
		ArrayList<Double> pd_percentages = new ArrayList<Double>();
		Date maxDate2 = new Date(0, 0, 0);
		for (int i = 0; i < Data.portfolioDetailsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioDetailsBCF
						.get(i).date);
				if (tempDate.after(maxDate2)) {
					maxDate2 = tempDate;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < Data.portfolioDetailsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioDetailsBCF
						.get(i).date);
				if (tempDate.equals(maxDate2)) {
					if (Data.settings[0] == 0) {
						pd_assets.add(Data.portfolioDetailsBCF.get(i).asset);
					} else {
						pd_assets.add(Data.portfolioDetailsBCF.get(i).asset_vn);
					}

					pd_percentages.add(Data.portfolioDetailsBCF.get(i).percentage);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		LinearLayout layoutChart3 = (LinearLayout) rootView
				.findViewById(R.id.portfolio_details_chart);
		drawChart(rootView, layoutChart3, pd_assets, pd_percentages, listColor);
		ArrayAdapterListView customArrayAdapter3 = new ArrayAdapterListView(
				getActivity(), pd_assets, pd_percentages);
		ListView listView3 = (ListView) rootView
				.findViewById(R.id.portfolio_details_table);
		listView3.setAdapter(customArrayAdapter3);
		setListViewHeightBasedOnChildren(listView3);
		ArrayList<String> companies = new ArrayList<String>();
		ArrayList<Double> percentages = new ArrayList<Double>();
		Date maxDate3 = new Date(0, 0, 0);
		Log.d("Size of top holding", Data.portfolioTopHoldingsBCF.size() + "");
		for (int i = 0; i < Data.portfolioTopHoldingsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioTopHoldingsBCF
						.get(i).date);
				Log.d("Date holding", tempDate.toGMTString() + Data.portfolioTopHoldingsBCF.get(i).company_name);

				if (tempDate.after(maxDate3)) {
					maxDate3 = tempDate;
				}
			} catch (ParseException e) {
				Log.d("Loi parse", e.getMessage());
			}
		}
		Log.d("Max date holding", maxDate3.toGMTString());
		for (int i = 0; i < Data.portfolioTopHoldingsBCF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.portfolioTopHoldingsBCF
						.get(i).date);
				if (tempDate.equals(maxDate3)) {
					if (Data.settings[0] == 0) {
						companies
								.add(Data.portfolioTopHoldingsBCF.get(i).company_name);
						percentages
								.add(Data.portfolioTopHoldingsBCF.get(i).percentage);
					} else {
						companies
								.add(Data.portfolioTopHoldingsBCF.get(i).company_name_vn);
						percentages
								.add(Data.portfolioTopHoldingsBCF.get(i).percentage);
					}

				}
			} catch (ParseException e) {
				Log.d("Loi parse", e.getMessage());
			}
		}
		ArrayAdapterListView customArrayAdapter2 = new ArrayAdapterListView(
				getActivity(), companies, percentages);
		ListView listView2 = (ListView) rootView
				.findViewById(R.id.top_five_table);
		listView2.setAdapter(customArrayAdapter2);
		setListViewHeightBasedOnChildren(listView2);
		return rootView;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter != null) {
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.setLayoutParams(new ViewGroup.LayoutParams(0, 0));

				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	private void drawChart(View rootView, LinearLayout layoutChart,
			ArrayList<String> itemsChart, ArrayList<Double> valuesChart,
			ArrayList<Integer> listColor) {
		CategorySeries distributionSeriesChart = new CategorySeries(" General ");
		for (int i = 0; i < valuesChart.size(); ++i) {
			distributionSeriesChart.add(itemsChart.get(i), valuesChart.get(i));
		}

		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		for (int i = 0; i < valuesChart.size(); ++i) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			int colorSize = listColor.size();
			seriesRenderer.setColor(listColor.get(i % colorSize));
			seriesRenderer.setChartValuesTextSize(50f);

			// Adding the renderer of a slice to the renderer of the pie chart
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
		setChartSettings(defaultRenderer);
		GraphicalView multipleChartView = ChartFactory.getPieChartView(
				getActivity(), distributionSeriesChart, defaultRenderer);
		// Adding the pie chart to the custom layout
		layoutChart.addView(multipleChartView);
	}

	private ArrayList<Integer> colorGenerator() {
		ArrayList<Integer> listColor = new ArrayList<Integer>();
		listColor.add(Color.rgb(000, 126, 214));
		listColor.add(Color.rgb(131, 153, 235));
		listColor.add(Color.rgb(142, 108, 239));
		listColor.add(Color.rgb(156, 070, 208));
		listColor.add(Color.rgb(224, 030, 132));
		listColor.add(Color.rgb(255, 000, 000));
		listColor.add(Color.rgb(255, 115, 000));
		listColor.add(Color.rgb(255, 175, 000));
		listColor.add(Color.rgb(255, 236, 000));
		listColor.add(Color.rgb(213, 243, 011));
		listColor.add(Color.rgb(82, 215, 38));
		listColor.add(Color.rgb(27, 170, 47));
		listColor.add(Color.rgb(45, 203, 117));
		listColor.add(Color.rgb(38, 215, 174));
		listColor.add(Color.rgb(124, 221, 221));
		listColor.add(Color.rgb(95, 182, 212));
		listColor.add(Color.rgb(151, 217, 255));
		return listColor;
	}

	private void setChartSettings(DefaultRenderer defaultRenderer) {
		defaultRenderer.setChartTitleTextSize(20);
		defaultRenderer.setBackgroundColor(Color.BLACK);
		defaultRenderer.setShowLegend(false);
		defaultRenderer.setDisplayValues(false);
		defaultRenderer.setInScroll(false);
		defaultRenderer.setPanEnabled(false);
		defaultRenderer.setStartAngle(-90);
		defaultRenderer.setZoomButtonsVisible(false);
		defaultRenderer.setLabelsColor(Color.WHITE);
		defaultRenderer.setLabelsTextSize(15);
	}
}