package activity_and_fragment;

import java.util.ArrayList;
import java.util.Calendar;

import utility.Data;
import utility.MyCustomMessage;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentMainNews extends Fragment {
	private final ArrayList<MyCustomMessage> listArticles = new ArrayList<MyCustomMessage>();
	private int selectedMonth = -1;
	private int selectedYear = 2000;
	private Spinner spinnerMonth;
	private View rootView;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle closedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main_news, container,
				false);
		Calendar today = Calendar.getInstance();
		listArticles.clear();
		for (int i = 0; i < Data.news.size(); ++i) {
			Data.NewsData news = Data.news.get(i);
			Log.d("Test all", Data.settings[0] + "");
			if (Data.settings[0] == 0) {
				listArticles.add(new MyCustomMessage(news.header, news.content,
						news.date, false));
			} else {
				listArticles.add(new MyCustomMessage(news.header_vn,
						news.content_vn, news.date, false));
			}
		}
		ListView listViewArticles = (ListView) rootView
				.findViewById(R.id.list_articles);
		NewsArrayAdapter newsArrayAdapter = new NewsArrayAdapter(getActivity(),
				R.layout.fragment_main_news, listArticles);
		listViewArticles.setAdapter(newsArrayAdapter);
		spinnerMonth = (Spinner) rootView.findViewById(R.id.spinner2);
		final Spinner spinnerYear = (Spinner) rootView
				.findViewById(R.id.spinner3);
		ArrayList<Integer> yearList = new ArrayList<Integer>();
		for (int i = 2000; i <= today.get(today.YEAR); ++i) {
			yearList.add(i);
		}
		ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(
				getActivity(), android.R.layout.simple_spinner_item, yearList);
		yearAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerYear.setAdapter(yearAdapter);
		selectedMonth = today.get(today.MONTH);
		spinnerMonth.setSelection(selectedMonth);
		selectedYear = today.get(today.YEAR);
		spinnerYear.setSelection(yearAdapter.getPosition(selectedYear));
		spinnerMonth.post(new Runnable() {

			@Override
			public void run() {
				spinnerMonth
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								selectedMonth = spinnerMonth
										.getSelectedItemPosition() + 1;
								selectedYear = (Integer) spinnerYear
										.getSelectedItem();
								initialiseList();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
			}

		});
		spinnerYear.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				spinnerYear
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								selectedMonth = spinnerMonth
										.getSelectedItemPosition() + 1;
								selectedYear = (Integer) spinnerYear
										.getSelectedItem();
								initialiseList();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
							}
						});
			}

		});
		Button btnAll = (Button) rootView.findViewById(R.id.all_button);
		btnAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listArticles.clear();
				for (int i = 0; i < Data.news.size(); ++i) {
					Data.NewsData news = Data.news.get(i);
					Log.d("Test all", Data.settings[0] + "");
					if (Data.settings[0] == 0) {
						listArticles.add(new MyCustomMessage(news.header,
								news.content, news.date, false));
					} else {
						listArticles.add(new MyCustomMessage(news.header_vn,
								news.content_vn, news.date, false));
					}

				}
				((ListView) getActivity().findViewById(R.id.list_articles))
						.setAdapter(new NewsArrayAdapter(getActivity(),
								R.layout.fragment_main_news, listArticles));
			}
		});
		return rootView;
	}

	private void initialiseList() {
		Log.d("List bi xoa ne", "True");
		listArticles.clear();
		for (int i = 0; i < Data.news.size(); ++i) {
			Data.NewsData news = Data.news.get(i);
			String[] dateTime = news.date.split(" ");
			String[] date = dateTime[0].split("-");
			if (Integer.parseInt(date[0]) == selectedYear
					&& Integer.parseInt(date[1]) == selectedMonth) {
				Log.d("Test init", Data.settings[0] + "");

				if (Data.settings[0] == 0) {
					listArticles.add(new MyCustomMessage(news.header,
							news.content, news.date, false));
				} else {
					listArticles.add(new MyCustomMessage(news.header_vn,
							news.content_vn, news.date, false));
				}
			} else if (Integer.parseInt(date[0]) < selectedYear
					|| (Integer.parseInt(date[0]) == selectedYear && Integer
							.parseInt(date[1]) < selectedMonth)) {
				break;
			}
		}
		((ListView) getActivity().findViewById(R.id.list_articles))
				.setAdapter(new NewsArrayAdapter(getActivity(),
						R.layout.fragment_main_news, listArticles));
	}

	private class NewsArrayAdapter extends ArrayAdapter<MyCustomMessage> {
		private ArrayList<MyCustomMessage> my_custom_message_list;

		public NewsArrayAdapter(Context context, int text_view_resouse_id,
				ArrayList<MyCustomMessage> MyCustomMessageList) {
			super(context, text_view_resouse_id, MyCustomMessageList);
			this.my_custom_message_list = MyCustomMessageList;
		}

		private class ViewHolder {
			TextView header;
			TextView message;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder1 = new ViewHolder();
			Log.v("ConvertView", String.valueOf(position));
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.layout_news_list, null);
				holder1.message = (TextView) convertView
						.findViewById(R.id.content);
				holder1.header = (TextView) convertView
						.findViewById(R.id.header);
				convertView.setTag(holder1);
			}
			ViewHolder holder2 = (ViewHolder) convertView.getTag();
			final MyCustomMessage mcm = my_custom_message_list.get(position);
			LayoutInflater layoutInflaterMessage = (LayoutInflater) getActivity()
					.getBaseContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			final View popupViewMessage = layoutInflaterMessage.inflate(
					R.layout.layout_popup_news, null);
			final PopupWindow popupMessage = new PopupWindow(popupViewMessage,
					600, 800);
			popupMessage.setBackgroundDrawable(new BitmapDrawable());
			popupMessage.setFocusable(true);
			TextView message = (TextView) popupViewMessage
					.findViewById(R.id.tv_content);
			TextView header = (TextView) popupViewMessage
					.findViewById(R.id.tv_title);
			String txt_message = mcm.getMessage();
			String txt_message_title = mcm.getHeader();
			message.setText(txt_message);
			header.setText(txt_message_title);
			message.setMovementMethod(new ScrollingMovementMethod());
			final View popupViewFadeout = layoutInflaterMessage.inflate(
					R.layout.layout_popup_blur_background, null);
			final PopupWindow fadePopup = new PopupWindow(popupViewFadeout,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
			holder2.message.setOnClickListener(new TextView.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					fadePopup.setFocusable(false);
					fadePopup.showAtLocation(popupViewFadeout, Gravity.CENTER,
							0, 0);
					popupMessage.showAtLocation(arg0, Gravity.CENTER, 0, 0);
				}
			});

			holder2.header.setOnClickListener(new TextView.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					fadePopup.setFocusable(false);
					fadePopup.showAtLocation(popupViewFadeout, Gravity.CENTER,
							0, 0);
					popupMessage.showAtLocation(arg0, Gravity.CENTER, 0, 0);
				}
			});

			popupMessage.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					fadePopup.dismiss();
				}
			});

			holder2.header.setText(mcm.getHeader());
			holder2.message.setText(mcm.getMessage());
			holder2.message.setTag(mcm);
			return convertView;
		}
	}
}