package activity_and_fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import utility.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vcbf.R;

public class FragmentFundLogin extends Fragment {

	static String TAG = "ExelLog";
	Button writeExcelButton, readExcelButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup layoutContainer = container;
		final LayoutInflater layoutInflater = inflater;
		final View rootView = layoutInflater.inflate(
				R.layout.fragment_fund_login, layoutContainer, false);
		final TextView username = (TextView) rootView
				.findViewById(R.id.username);
		final TextView password = (TextView) rootView
				.findViewById(R.id.password);
		final Button buttonLogin = (Button) rootView
				.findViewById(R.id.login_button);
		final Button buttonLogout = (Button) rootView
				.findViewById(R.id.logout_button);
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getUserInformation(username.getText().toString(), password
						.getText().toString(), buttonLogin, rootView);
			}
		});
		buttonLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LinearLayout loginLayout = (LinearLayout) rootView
						.findViewById(R.id.login_layout);
				loginLayout.setVisibility(LinearLayout.VISIBLE);
				LinearLayout infoLayout = (LinearLayout) rootView
						.findViewById(R.id.info_layout);
				infoLayout.setVisibility(LinearLayout.INVISIBLE);
				Data.user.clear();
				Data.writeUser();
			}
		});
		if (!Data.user.isEmpty()) {
			getUserInformation(Data.user.get(0).username,
					Data.user.get(0).password, buttonLogin, rootView);
		}
		return rootView;
	}

	private void getUserInformation(final String username,
			final String password, final Button button, final View rootView) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
						.getApplicationContext().getSystemService(
								Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					try {
						HttpClient client = new DefaultHttpClient();
						HttpResponse response = client.execute(new HttpGet(
								Data.webServiceLink + "authenticate&username="
										+ username + "&password=" + password));
						StatusLine sl = response.getStatusLine();
						if (sl.getStatusCode() == HttpStatus.SC_OK) {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							response.getEntity().writeTo(out);
							out.close();
							final String response_string = out.toString();
							Log.d("Response ne", response_string);
							if (!response_string.equalsIgnoreCase("\"false\"")) {
								button.post(new Runnable() {
									public void run() {
										LinearLayout loginLayout = (LinearLayout) rootView
												.findViewById(R.id.login_layout);
										loginLayout
												.setVisibility(LinearLayout.INVISIBLE);
										LinearLayout infoLayout = (LinearLayout) rootView
												.findViewById(R.id.info_layout);
										infoLayout
												.setVisibility(LinearLayout.VISIBLE);
										ObjectMapper mapper = new ObjectMapper();
										TypeFactory typeFactory = mapper
												.getTypeFactory();
										CollectionType collectionType = typeFactory
												.constructCollectionType(
														List.class,
														Data.UserData.class);
										try {
											Data.user = mapper.readValue(
													response_string,
													collectionType);
											if (!Data.user.isEmpty()) {
												TextView textViewInvestorName = (TextView) rootView
														.findViewById(R.id.table_investor_name);
												TextView textViewFolioNo = (TextView) rootView
														.findViewById(R.id.table_investor_folio_no);
												TextView textViewInvestorHolding = (TextView) rootView
														.findViewById(R.id.table_investor_holding);
												TextView textViewInvestorNAV = (TextView) rootView
														.findViewById(R.id.table_investor_nav);
												TextView textViewInvestorValue = (TextView) rootView
														.findViewById(R.id.table_investor_value);
												TextView textViewInvestorType = (TextView) rootView
														.findViewById(R.id.table_investor_type);
												TextView textViewDate = (TextView) rootView
														.findViewById(R.id.table_date);

												DateFormat dateFormatter = new SimpleDateFormat(
														"yyyy-MM-dd");
												Date maxDate1 = new Date(0, 0,
														0);
												for (int i = 0; i < Data.portfolioStatisticsTBF
														.size(); ++i) {
													try {
														Date tempDate = dateFormatter
																.parse(Data.portfolioStatisticsTBF
																		.get(i).date);
														if (tempDate
																.after(maxDate1)) {
															maxDate1 = tempDate;
														}
													} catch (ParseException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}
												}
												textViewDate.setText(rootView
														.getResources()
														.getString(
																R.string.asOf)
														+ " "
														+ maxDate1
																.toGMTString());

												DecimalFormat formatDouble = new DecimalFormat(
														"#0,000.00");
												textViewInvestorName.setText(Data.user
														.get(0).investor_name);
												textViewFolioNo.setText(Data.user
														.get(0).investor_folio_number);
												textViewInvestorHolding.setText(formatDouble.format(Data.user
														.get(0).investor_holding));
												textViewInvestorNAV.setText(formatDouble.format(Data.user
														.get(0).investor_nav));
												textViewInvestorValue.setText(formatDouble.format(Data.user
														.get(0).investor_value));
												textViewInvestorType
														.setText(Data.user.get(0).investor_type);

											}
										} catch (JsonParseException e) {
											Log.d("Loi parse JSON test",
													e.getMessage());
										} catch (JsonMappingException e) {
											Log.d("Loi parse Map",
													e.getMessage());
										} catch (IOException e) {
											Log.d("Loi input output",
													e.getMessage());
										}
										Data.writeUser();
									}
								});
							} else {
								button.post(new Runnable() {
									public void run() {
										button.setText("Try again");
									}
								});
							}
						} else {
							button.post(new Runnable() {
								public void run() {
									button.setText("Try again");
								}
							});
							response.getEntity().getContent().close();
						}
					} catch (ClientProtocolException e) {
						Log.d("Loi protocol", e.getMessage());
						button.post(new Runnable() {
							public void run() {
								button.setText("Try again");
							}
						});
					} catch (IOException e) {
						Log.d("Loi IO", e.getMessage());
						button.post(new Runnable() {
							public void run() {
								button.setText("Again");
							}
						});
					}
				}
			}
		}).start();
	}
}