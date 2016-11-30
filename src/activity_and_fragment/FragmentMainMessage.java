package activity_and_fragment;

import java.util.ArrayList;
import java.util.Collections;

import utility.Data;
import utility.MyCustomMessage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentMainMessage extends Fragment {
	final private ArrayList<MyCustomMessage> listMyCustomMessages = new ArrayList<MyCustomMessage>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main_message,
				container, false);
		final ListView listViewMessages = (ListView) rootView
				.findViewById(R.id.list_messages);
		final Button buttonDelete = (Button) rootView
				.findViewById(R.id.delete_button);
		final Button buttonCancel = (Button) rootView
				.findViewById(R.id.cancel_button);
		initialiseList();
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						switch (arg1) {
						case DialogInterface.BUTTON_NEGATIVE:
							ArrayList<MyCustomMessage> listDeleteMessages = new ArrayList<MyCustomMessage>();
							for (int i = 0; i < listMyCustomMessages.size(); ++i) {
								if (listMyCustomMessages.get(i).isSelected()) {
									listDeleteMessages.add(listMyCustomMessages
											.get(i));
								}
							}
							ArrayList<Data.MessageData> remindingMessagesToDelete = new ArrayList<Data.MessageData>();
							ArrayList<Data.MessageData> informationMessagesToDelete = new ArrayList<Data.MessageData>();
							for (int i = 0; i < listDeleteMessages.size(); ++i) {
								MyCustomMessage messageToDelete = listDeleteMessages
										.get(i);
								boolean found = false;
								for (int j = 0; j < Data.storedRemindingMessages
										.size(); ++j) {
									Data.MessageData message = Data.storedRemindingMessages
											.get(j);
									if (message.title
											.equalsIgnoreCase(messageToDelete
													.getHeader())
											&& message.content
													.equalsIgnoreCase(messageToDelete
															.getMessage())
											&& message.date
													.equalsIgnoreCase(messageToDelete
															.getDate())) {
										remindingMessagesToDelete.add(message);
										found = true;
										break;
									}
								}
								if (!found) {
									for (int j = 0; j < Data.informationMessages
											.size(); ++j) {
										Data.MessageData message = Data.informationMessages
												.get(j);
										if (message.title
												.equalsIgnoreCase(messageToDelete
														.getHeader())
												&& message.content
														.equalsIgnoreCase(messageToDelete
																.getMessage())
												&& message.date
														.equalsIgnoreCase(messageToDelete
																.getDate())) {
											informationMessagesToDelete
													.add(message);
											break;
										}
									}
								}
							}
							for (int i = 0; i < remindingMessagesToDelete
									.size(); ++i) {
								Data.storedRemindingMessages
										.remove(remindingMessagesToDelete
												.get(i));
							}
							for (int i = 0; i < informationMessagesToDelete
									.size(); ++i) {
								Data.informationMessages
										.remove(informationMessagesToDelete
												.get(i));
							}
							Data.writeStoredRemindingMessages();
							Data.writeInformationMessage();
							initialiseList();
							MessageArrayAdapter messageArrayAdapter = new MessageArrayAdapter(
									getActivity(),
									R.layout.fragment_main_message,
									listMyCustomMessages);
							listViewMessages.setAdapter(messageArrayAdapter);
							buttonCancel.callOnClick();
							listViewMessages.invalidateViews();
							break;
						}
					}
				};
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						FragmentMainMessage.this.getActivity());
				alertDialogBuilder
						.setMessage("Are you sure you want to delete selected messages?");
				alertDialogBuilder.setPositiveButton("No", dialogClickListener);
				alertDialogBuilder
						.setNegativeButton("Yes", dialogClickListener);
				alertDialogBuilder.show();
			}
		});

		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((LinearLayout) buttonDelete.getParent())
						.setVisibility(LinearLayout.INVISIBLE);
				buttonDelete.setVisibility(Button.INVISIBLE);
				buttonCancel.setVisibility(Button.INVISIBLE);
				for (int i = 0; i < listMyCustomMessages.size(); ++i) {
					listMyCustomMessages.get(i).setSelected(false);
				}
				ArrayList<MyCustomMessage> list = ((MessageArrayAdapter) listViewMessages
						.getAdapter()).getMyCustomMessageList();
				for (int i = 0; i < list.size(); ++i) {
					list.get(i).setVisible(false);
				}
				listViewMessages.invalidateViews();
			}
		});
		final MessageArrayAdapter messageArrayAdapter = new MessageArrayAdapter(
				getActivity(), R.layout.fragment_main_message,
				listMyCustomMessages);
		listViewMessages.setAdapter(messageArrayAdapter);
		listViewMessages.setLongClickable(true);
		listViewMessages
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						((LinearLayout) buttonDelete.getParent())
								.setVisibility(LinearLayout.VISIBLE);
						buttonDelete.setVisibility(Button.VISIBLE);
						buttonCancel.setVisibility(Button.VISIBLE);
						ArrayList<MyCustomMessage> listMyCustomMessages = ((MessageArrayAdapter) listViewMessages
								.getAdapter()).getMyCustomMessageList();
						for (int i = 0; i < listMyCustomMessages.size(); ++i) {
							listMyCustomMessages.get(i).setVisible(true);
						}
						listViewMessages.invalidateViews();
						return true;
					}
				});

		final Button buttonContactHoChiMinh = (Button) rootView
				.findViewById(R.id.hochiminh_contact);
		LayoutInflater layoutInflaterHoChiMinh = (LayoutInflater) getActivity()
				.getBaseContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		View popupViewHoChiMinh = layoutInflaterHoChiMinh.inflate(
				R.layout.layout_popup_contact, null);
		final PopupWindow popupWindowHoChiMinh = new PopupWindow(
				popupViewHoChiMinh, 600, LayoutParams.WRAP_CONTENT);
		popupWindowHoChiMinh.setBackgroundDrawable(new BitmapDrawable());
		popupWindowHoChiMinh.setFocusable(true);

		TextView textViewContactHoChiMinhTitle = (TextView) popupViewHoChiMinh
				.findViewById(R.id.tv_title);
		textViewContactHoChiMinhTitle.setText("VCBF Ho Chi Minh Contact");
		TextView textViewContactHoChiMinh = (TextView) popupViewHoChiMinh
				.findViewById(R.id.tv_content);
		textViewContactHoChiMinh.setText(Html.fromHtml(getResources()
				.getString(R.string.contact_us_hochiminh)));
		textViewContactHoChiMinh
				.setMovementMethod(new ScrollingMovementMethod());

		final Button buttonContactHaNoi = (Button) rootView
				.findViewById(R.id.hanoi_contact);
		LayoutInflater layoutInflaterHaNoi = (LayoutInflater) getActivity()
				.getBaseContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		View popupViewHaNoi = layoutInflaterHaNoi.inflate(
				R.layout.layout_popup_contact, null);
		final PopupWindow popupWindowHaNoi = new PopupWindow(popupViewHaNoi,
				600, LayoutParams.WRAP_CONTENT);
		popupWindowHaNoi.setBackgroundDrawable(new BitmapDrawable());
		popupWindowHaNoi.setFocusable(true);

		TextView textViewContactHaNoiTitle = (TextView) popupViewHaNoi
				.findViewById(R.id.tv_title);
		textViewContactHaNoiTitle.setText("VCBF Ha Noi Contact");
		TextView textViewContactHaNoiContent = (TextView) popupViewHaNoi
				.findViewById(R.id.tv_content);
		textViewContactHaNoiContent.setText(Html.fromHtml(getResources()
				.getString(R.string.contact_us_hanoi)));
		textViewContactHaNoiContent
				.setMovementMethod(new ScrollingMovementMethod());

		final View layoutFadeout = inflater.inflate(
				R.layout.layout_popup_blur_background,
				(ViewGroup) rootView.findViewById(R.id.fadePopup));
		final PopupWindow fadePopup = new PopupWindow(layoutFadeout,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);

		buttonContactHoChiMinh.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fadePopup.setFocusable(false);
				fadePopup.showAtLocation(layoutFadeout, Gravity.CENTER, 0, 0);
				popupWindowHoChiMinh.showAtLocation(rootView, Gravity.CENTER,
						0, 0);
			}
		});

		popupWindowHoChiMinh.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				fadePopup.dismiss();
			}
		});

		buttonContactHaNoi.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fadePopup.setFocusable(false);
				fadePopup.showAtLocation(layoutFadeout, Gravity.CENTER, 0, 0);
				popupWindowHaNoi.showAtLocation(rootView, Gravity.CENTER, 0, 0);

			}
		});

		popupWindowHaNoi.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				fadePopup.dismiss();
			}
		});
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	private void initialiseList() {
		Collections.sort(Data.storedRemindingMessages);
		Collections.sort(Data.informationMessages);
		listMyCustomMessages.clear();
		for (int i = 0; i < Data.storedRemindingMessages.size(); ++i) {
			if (Data.settings[0] == 0) {
				listMyCustomMessages.add(new MyCustomMessage(

				Data.storedRemindingMessages.get(i).title,
						Data.storedRemindingMessages.get(i).content,
						Data.storedRemindingMessages.get(i).date, false));
			} else {
				listMyCustomMessages.add(new MyCustomMessage(

				Data.storedRemindingMessages.get(i).title_vn,
						Data.storedRemindingMessages.get(i).content_vn,
						Data.storedRemindingMessages.get(i).date, false));
			}

		}
		for (int i = 0; i < Data.informationMessages.size(); ++i) {
			if (Data.settings[0] == 0) {
				listMyCustomMessages.add(new MyCustomMessage(
						Data.informationMessages.get(i).title,
						Data.informationMessages.get(i).content,
						Data.informationMessages.get(i).date, false));
			} else {
				listMyCustomMessages.add(new MyCustomMessage(
						Data.informationMessages.get(i).title_vn,
						Data.informationMessages.get(i).content_vn,
						Data.informationMessages.get(i).date, false));
			}

		}
	}

	private class MessageArrayAdapter extends ArrayAdapter<MyCustomMessage> {
		private ArrayList<MyCustomMessage> my_custom_message_list;

		public MessageArrayAdapter(Context context, int textViewResourceID,
				ArrayList<MyCustomMessage> MyCustomMessageList) {
			super(context, textViewResourceID, MyCustomMessageList);
			this.my_custom_message_list = new ArrayList<MyCustomMessage>();
			this.my_custom_message_list.addAll(MyCustomMessageList);
		}

		public ArrayList<MyCustomMessage> getMyCustomMessageList() {
			return my_custom_message_list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder1 = new ViewHolder();
			Log.v("ConvertView", String.valueOf(position));
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.layout_message_list,
						null);
				holder1.message = (TextView) convertView
						.findViewById(R.id.content);
				holder1.header = (TextView) convertView
						.findViewById(R.id.header);
				if (holder1 == null) {
					Log.d("Null check", "holder");
				}
				if (convertView == null) {
					Log.d("Null check", "convert view");
				}
				holder1.cb = (CheckBox) convertView
						.findViewById(R.id.cb_message);
				holder1.cb.setSelected(false);
				convertView.setTag(holder1);

			}
			ViewHolder holder2 = (ViewHolder) convertView.getTag();
			final MyCustomMessage mcm = my_custom_message_list.get(position);
			LayoutInflater layoutInflaterMessage = (LayoutInflater) getActivity()
					.getBaseContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			final View popupViewMessage = layoutInflaterMessage.inflate(
					R.layout.layout_popup_contact, null);
			final PopupWindow popupMessage = new PopupWindow(popupViewMessage,
					600, LayoutParams.WRAP_CONTENT);
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

			holder2.cb
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							mcm.setSelected(arg1);
						}
					});

			holder2.header.setText(mcm.getHeader());
			holder2.message.setText(mcm.getMessage());
			holder2.cb.setChecked(mcm.isSelected());
			if (mcm.isVisible()) {
				holder2.cb.setVisibility(CheckBox.VISIBLE);
			} else {
				holder2.cb.setVisibility(CheckBox.INVISIBLE);
			}
			holder2.message.setTag(mcm);
			return convertView;
		}

		private class ViewHolder {
			TextView header;
			TextView message;
			CheckBox cb;
		}
	}
}