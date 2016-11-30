package activity_and_fragment;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import utility.Data;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentTBFNAV extends Fragment {
	private List<double[]> xValues = new ArrayList<double[]>();
	private List<double[]> yValues = new ArrayList<double[]>();

	private XYMultipleSeriesRenderer multipleRendererChart;
	private GraphicalView multipleChartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saved_instance_state) {

		final View rootView = inflater.inflate(R.layout.fragment_tbf_nav,
				container, false);

		if (container == null) {
			return null;
		}

		FrameLayout layoutFirstMonth = (FrameLayout) rootView
				.findViewById(R.id.layout_first_month);
		TextView textViewDate1 = (TextView) layoutFirstMonth
				.findViewById(R.id.table_date);
		TextView textViewNAVUnit1 = (TextView) layoutFirstMonth
				.findViewById(R.id.table_navunit);
		TextView textViewNAVChange1 = (TextView) layoutFirstMonth
				.findViewById(R.id.table_navchange);
		TextView textViewHighestNAV1 = (TextView) layoutFirstMonth
				.findViewById(R.id.table_highestnavunit);
		TextView textViewLowestNAV1 = (TextView) layoutFirstMonth
				.findViewById(R.id.table_lowestnavunit);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date maxDate1 = new Date(0, 0, 0);

		for (int i = 0; i < Data.weeklyFundPriceTBF.size(); i++) {
			try {
				Date tempDate = dateFormatter.parse(Data.weeklyFundPriceTBF
						.get(i).date);
				if (tempDate.after(maxDate1)) {
					maxDate1 = tempDate;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("Max date weekly", maxDate1.toGMTString());

		for (int i = 0; i < Data.weeklyFundPriceTBF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.weeklyFundPriceTBF
						.get(i).date);
				if (tempDate.equals(maxDate1)) {
					DecimalFormat format0 = new DecimalFormat("#0,000.00");
					DecimalFormat format2 = new DecimalFormat("#0.00");

					textViewDate1.setText(rootView.getResources().getString(
							R.string.asOf)
							+ " " + tempDate.toGMTString());
					textViewNAVUnit1.setText(format0
							.format(Data.weeklyFundPriceTBF.get(i).nav_unit));
					textViewNAVChange1.setText(format2
							.format(Data.weeklyFundPriceTBF.get(i).nav_change));
					textViewHighestNAV1
							.setText(format0.format(Data.weeklyFundPriceTBF
									.get(i).highest_nav_unit));
					textViewLowestNAV1
							.setText(format0.format(Data.weeklyFundPriceTBF
									.get(i).lowest_nav_unit));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		FrameLayout layoutSecondMonth = (FrameLayout) rootView
				.findViewById(R.id.layout_second_month);
		TextView textViewDate2 = (TextView) layoutSecondMonth
				.findViewById(R.id.table_date);
		TextView textViewNAVUnit2 = (TextView) layoutSecondMonth
				.findViewById(R.id.table_navunit);
		TextView textViewNAVChange2 = (TextView) layoutSecondMonth
				.findViewById(R.id.table_navchange);
		TextView textViewHighestNAV2 = (TextView) layoutSecondMonth
				.findViewById(R.id.table_highestnavunit);
		TextView textViewLowestNAV2 = (TextView) layoutSecondMonth
				.findViewById(R.id.table_lowestnavunit);

		Date maxDate2 = new Date(0, 0, 0);

		for (int i = 0; i < Data.weeklyFundPriceTBF.size(); i++) {
			try {
				Date tempDate = dateFormatter.parse(Data.weeklyFundPriceTBF
						.get(i).date);
				if (tempDate.after(maxDate2) && !tempDate.equals(maxDate1)) {
					maxDate2 = tempDate;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < Data.weeklyFundPriceTBF.size(); ++i) {
			try {
				Date tempDate = dateFormatter.parse(Data.weeklyFundPriceTBF
						.get(i).date);
				if (tempDate.equals(maxDate2)) {
					DecimalFormat format0 = new DecimalFormat("#0,000.00");
					DecimalFormat format2 = new DecimalFormat("#0.00");

					textViewDate2.setText(rootView.getResources().getString(
							R.string.asOf)
							+ " " + tempDate.toGMTString());
					textViewNAVUnit2.setText(format0
							.format(Data.weeklyFundPriceTBF.get(i).nav_unit));
					textViewNAVChange2.setText(format2
							.format(Data.weeklyFundPriceTBF.get(i).nav_change));
					textViewHighestNAV2
							.setText(format0.format(Data.weeklyFundPriceTBF
									.get(i).highest_nav_unit));
					textViewLowestNAV2
							.setText(format0.format(Data.weeklyFundPriceTBF
									.get(i).lowest_nav_unit));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final ArrayList<Date> listDate = new ArrayList<Date>();
		for (int i = 0; i < Data.weeklyFundPriceTBF.size(); i++) {
			try {
				Date tempDate = dateFormatter.parse(Data.weeklyFundPriceTBF
						.get(i).date);

				listDate.add(tempDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Spinner spinner = (Spinner) rootView.findViewById(R.id.month_spinner);
		ArrayAdapter<Date> spinnerArrayAdapter = new ArrayAdapter<Date>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				listDate); // selected item will look like a spinner set from
							// XML
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (count != 0) {
					LayoutInflater layoutInflaterMessage = (LayoutInflater) getActivity()
							.getBaseContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);

					final View popupViewMessage = layoutInflaterMessage
							.inflate(R.layout.layout_popup_weekly_fund_price,
									null);

					final PopupWindow popupMessage = new PopupWindow(
							popupViewMessage, LayoutParams.MATCH_PARENT, 800);
					popupMessage.setBackgroundDrawable(new BitmapDrawable());

					popupMessage.setFocusable(true);

					FrameLayout layoutSecondMonth = (FrameLayout) popupViewMessage
							.findViewById(R.id.layout_month);
					TextView textViewDate = (TextView) layoutSecondMonth
							.findViewById(R.id.table_date);
					TextView textViewNAVUnit = (TextView) layoutSecondMonth
							.findViewById(R.id.table_navunit);
					TextView textViewNAVChange = (TextView) layoutSecondMonth
							.findViewById(R.id.table_navchange);
					TextView textViewHighestNAV = (TextView) layoutSecondMonth
							.findViewById(R.id.table_highestnavunit);
					TextView textViewLowestNAV = (TextView) layoutSecondMonth
							.findViewById(R.id.table_lowestnavunit);
					DateFormat dateFormatter = new SimpleDateFormat(
							"yyyy-MM-dd");
					for (int i = 0; i < Data.weeklyFundPriceTBF.size(); i++) {
						try {
							Date tempDate = dateFormatter
									.parse(Data.weeklyFundPriceTBF.get(i).date);
							if (tempDate.equals(listDate.get(position))) {
								DecimalFormat format0 = new DecimalFormat(
										"#0,000.00");
								DecimalFormat format2 = new DecimalFormat(
										"#0.00");
								textViewDate.setText(rootView.getResources()
										.getString(R.string.asOf)
										+ " "
										+ tempDate.toGMTString());
								textViewNAVUnit.setText(format0
										.format(Data.weeklyFundPriceTBF.get(i).nav_unit));
								textViewNAVChange.setText(format2
										.format(Data.weeklyFundPriceTBF.get(i).nav_change));
								textViewHighestNAV.setText(format0
										.format(Data.weeklyFundPriceTBF.get(i).highest_nav_unit));
								textViewLowestNAV.setText(format0
										.format(Data.weeklyFundPriceTBF.get(i).lowest_nav_unit));
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					final View popupViewFadeout = layoutInflaterMessage
							.inflate(R.layout.layout_popup_blur_background,
									null);
					final PopupWindow fadePopup = new PopupWindow(
							popupViewFadeout, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, false);

					fadePopup.setFocusable(false);
					fadePopup.showAtLocation(popupViewFadeout, Gravity.CENTER,
							0, 0);
					popupMessage.showAtLocation(rootView, Gravity.CENTER, 0, 0);

					popupMessage.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss() {
							// TODO Auto-generated method stub
							fadePopup.dismiss();
						}
					});
				}
				count++;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		TextView textViewDate = (TextView) rootView.findViewById(R.id.nav_date);
		double x[] = null;
		double y[] = null;
		if (!Data.navValuesTBF.isEmpty()) {
			Log.d("Not empty", "right");

			if (Data.navValuesTBF.get(0).length > 0) {
				if (Data.navValuesTBF.get(0).length < 12) {
					x = new double[Data.navValuesTBF.get(0).length];
					y = new double[Data.navValuesTBF.get(0).length];

					for (int i = 0; i < Data.navValuesTBF.get(0).length; i++) {
						x[i] = i;
						y[i] = Data.navValuesTBF.get(0)[i];
					}
					textViewDate
							.setText(rootView.getResources().getString(
									R.string.from)
									+ " "
									+ Data.navDatesTBF.get(0)[0].toString()
									+ " "
									+ rootView.getResources().getString(
											R.string.to)
									+ " "
									+ Data.navDatesTBF.get(0)[Data.navValuesTBF
											.get(0).length - 1].toString());
				} else {
					x = new double[12];
					y = new double[12];
					for (int i = 0; i < 12; i++) {
						x[i] = i;
						y[i] = Data.navValuesTBF.get(0)[i
								+ Data.navValuesTBF.get(0).length - 12];
					}
					textViewDate.setText(rootView.getResources().getString(
							R.string.from)
							+ " "
							+ Data.navDatesTBF.get(0)[0 + Data.navDatesTBF
									.get(0).length - 12].toString()
							+ " "
							+ rootView.getResources().getString(R.string.to)
							+ " "
							+ Data.navDatesTBF.get(0)[11 + Data.navDatesTBF
									.get(0).length - 12].toString());

				}
			}

		}

		if (x != null) {
			Log.d("Vo dc roi ne", "right");
			xValues.add(x);
			yValues.add(y);
			drawChart(rootView, xValues, yValues);
		} else {
			Log.d("Vo dc roi ne", "wrong");

		}

		return rootView;
	}

	private void drawChart(View rootView, List<double[]> xValues,
			List<double[]> yValues) {

		LinearLayout layoutChart = (LinearLayout) rootView
				.findViewById(R.id.fund_nav_chart);

		String[] titlesChart = new String[] { "NAV per Unit" };

		int[] colorsChart = new int[] { Color.GREEN };
		PointStyle[] stylesChart = new PointStyle[] { PointStyle.CIRCLE, };

		multipleRendererChart = buildRenderer(colorsChart, stylesChart);

		for (int i = 0; i < multipleRendererChart.getSeriesRendererCount(); i++) {
			((XYSeriesRenderer) multipleRendererChart.getSeriesRendererAt(i))
					.setFillPoints(true);
		}

		setChartSettings(multipleRendererChart);

		if (multipleChartView == null) {
			multipleChartView = ChartFactory.getLineChartView(
					this.getActivity(),
					mDataset(titlesChart, xValues, yValues),
					multipleRendererChart);
			layoutChart.addView(multipleChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		} else {
			multipleChartView.repaint();
		}
	}

	private double getMinNav() {
		if (yValues.isEmpty()) {
			return -1;
		} else {
			double min = yValues.get(0)[0];
			for (int i = 1; i < yValues.get(0).length; ++i) {
				if (yValues.get(0)[i] < min) {
					min = yValues.get(0)[i];
				}
			}
			return min;
		}

	}

	private double getMaxNav() {
		if (yValues.isEmpty()) {
			return -1;
		} else {
			double max = yValues.get(0)[0];
			for (int i = 1; i < yValues.get(0).length; ++i) {
				if (yValues.get(0)[i] > max) {
					max = yValues.get(0)[i];
				}
			}
			return max;
		}
	}

	private void setChartSettings(XYMultipleSeriesRenderer multipleRendererChart) {

		multipleRendererChart.setAxisTitleTextSize(15);
		multipleRendererChart.setChartTitleTextSize(20);
		multipleRendererChart.setLabelsTextSize(15);
		multipleRendererChart.setLegendTextSize(15);

		multipleRendererChart.setXTitle("Date");
		multipleRendererChart.setXAxisMin(0);
		multipleRendererChart.setXAxisMax(12);
		multipleRendererChart.setXLabels(20);
		multipleRendererChart.setXLabelsAlign(Align.CENTER);
		multipleRendererChart.setXLabelsColor(Color.WHITE);

		multipleRendererChart.setYTitle("Pricing");
		multipleRendererChart.setYAxisMin(getMinNav());
		multipleRendererChart.setYAxisMax(getMaxNav());
		multipleRendererChart.setYLabels(10);
		multipleRendererChart.setYLabelsAlign(Align.RIGHT);
		multipleRendererChart.setYLabelsColor(0, Color.WHITE);

		multipleRendererChart.setAxesColor(Color.WHITE);
		multipleRendererChart.setLabelsColor(Color.WHITE);

		multipleRendererChart.setShowGrid(true);

		multipleRendererChart.setMargins(new int[] { 25, 50, 15, 15 });
		multipleRendererChart.setMarginsColor(Color.BLACK);

		multipleRendererChart.setPanEnabled(false, false);
		multipleRendererChart.setZoomEnabled(false, false);

		multipleRendererChart.setApplyBackgroundColor(true);
		multipleRendererChart.setBackgroundColor(Color.BLACK);

		multipleRendererChart.setPointSize(4);

	}

	private XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		// TODO Auto-generated method stub
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	private void setRenderer(XYMultipleSeriesRenderer multipleRendererChart,
			int[] colors, PointStyle[] styles) {
		// TODO Auto-generated method stub

		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			multipleRendererChart.addSeriesRenderer(r);
		}
	}

	private XYMultipleSeriesDataset mDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {
		// TODO Auto-generated method stub
		XYMultipleSeriesDataset dataset1 = new XYMultipleSeriesDataset();
		addXYSeries(dataset1, titles, xValues, yValues, 0);
		return dataset1;
	}

	private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
			List<double[]> xValues, List<double[]> yValues, int scale) {
		// TODO Auto-generated method stub

		int length = titles.length;
		for (int i = 0; i < length; i++) {

			double[] xV = xValues.get(i);
			XYSeries series = new XYSeries(titles[i], scale);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}

	}

}