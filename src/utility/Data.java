package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Data {

	public static String webServiceLink = "http://10.0.3.2/web_service/api.php?request=";
	public static int count = 0;
	public static List<VersionData> version = new ArrayList<VersionData>();

	public static List<String[]> navDatesTBF = new ArrayList<String[]>();
	public static List<double[]> navValuesTBF = new ArrayList<double[]>();
	public static List<IndustryBreakdownData> industryBreakdownTBF = new ArrayList<IndustryBreakdownData>();
	public static List<PortfolioDetailData> portfolioDetailsTBF = new ArrayList<PortfolioDetailData>();
	public static List<PortfolioTopHoldingData> portfolioTopHoldingsTBF = new ArrayList<PortfolioTopHoldingData>();
	public static List<PortfolioStatisticData> portfolioStatisticsTBF = new ArrayList<PortfolioStatisticData>();
	public static List<WeeklyFundPriceData> weeklyFundPriceTBF = new ArrayList<WeeklyFundPriceData>();

	public static List<String[]> navDatesBCF = new ArrayList<String[]>();
	public static List<double[]> navValuesBCF = new ArrayList<double[]>();
	public static List<IndustryBreakdownData> industryBreakdownBCF = new ArrayList<IndustryBreakdownData>();
	public static List<PortfolioDetailData> portfolioDetailsBCF = new ArrayList<PortfolioDetailData>();
	public static List<PortfolioTopHoldingData> portfolioTopHoldingsBCF = new ArrayList<PortfolioTopHoldingData>();
	public static List<PortfolioStatisticData> portfolioStatisticsBCF = new ArrayList<PortfolioStatisticData>();
	public static List<WeeklyFundPriceData> weeklyFundPriceBCF = new ArrayList<WeeklyFundPriceData>();

	public static List<MessageData> remindingMessage = new ArrayList<MessageData>();
	public static List<MessageData> storedRemindingMessages = new ArrayList<MessageData>();
	public static List<NewsData> news = new ArrayList<NewsData>();
	public static List<UserData> user = new ArrayList<UserData>();
	public static List<MessageData> informationMessages = new ArrayList<MessageData>();
	public static int settings[] = { 0, 0 };
	public static Context context;

	public static void initialiseData() {
		ArrayList<VersionData> currentVersion = new ArrayList<VersionData>();
		version.add(new VersionData());
		BufferedReader reader = null;
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, VersionData.class);
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "version.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
				Log.d("Response ne", responseString);
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		try {
			currentVersion = mapper.readValue(responseString, collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}

		if (currentVersion.isEmpty()) {
			currentVersion.add(new VersionData());
			currentVersion.get(0).lastInformationMessageDate = "2000-01-01 00:00:00";
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			readRemindingMessageFromDatabase();
			getVersion();
		} else {
			version.clear();
			version.add(currentVersion.get(0));
		}
		currentVersion.get(0).version = -1;
		version.get(0).lastInformationMessageDate = currentVersion.get(0).lastInformationMessageDate;

		readWeeklyFundPriceFromDatabaseTBF();
		readPortfolioStatisticFromDatabaseTBF();
		readPortfolioTopHoldingFromDatabaseTBF();
		readPortfolioDetailFromDatabaseTBF();
		readNavFromDatabaseTBF();
		readIndustryBreakdownFromDatabaseTBF();

		readWeeklyFundPriceFromDatabaseBCF();
		readPortfolioStatisticFromDatabaseBCF();
		readPortfolioTopHoldingFromDatabaseBCF();
		readPortfolioDetailFromDatabaseBCF();
		readNavFromDatabaseBCF();
		readIndustryBreakdownFromDatabaseBCF();

		readNewsFromDatabase();
		readInformationMessageFromDatabase();

		try {
			mapper.writeValue(file, version);
		} catch (IOException e) {
			Log.d("Loi input output 1", e.getMessage());
		}

		readWeeklyFundPriceFromFileTBF();
		readPortfolioStatisticFromFileTBF();
		readPortfolioTopHoldingFromFileTBF();
		readPortfolioDetailFromFileTBF();
		readNavFromFileTBF();
		readIndustryBreakdownFromFileTBF();

		readWeeklyFundPriceFromFileBCF();
		readPortfolioStatisticFromFileBCF();
		readPortfolioTopHoldingFromFileBCF();
		readPortfolioDetailFromFileBCF();
		readNavFromFileBCF();
		readIndustryBreakdownFromFileBCF();

		readUser();
		readNewsFromFile();
		readRemindingMessageFromFile();
		readInformationMessageFromFile();
	}

	public static void getSettings() {
		try {
			String filename = "settings.dat";
			File settingsFile = context.getFileStreamPath(filename);
			if (settingsFile.exists()) {
				FileInputStream fis = context.openFileInput(filename);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i != -1;) {
					i = fis.read();
					if (i != -1)
						sb.append(Character.toString((char) i));
				}
				String settingsStr = sb.toString();
				settingsStr = settingsStr.trim();
				settingsStr = settingsStr.replaceAll("[^0-9]+", " ");
				int index = 0;
				for (String value : settingsStr.split(" ")) {
					settings[index] = Integer.parseInt(value);
					++index;
				}

				fis.close();
			} else {
				settings[0] = 0;
				settings[1] = 0;
			}
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}

	private static void getVersion() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getVersion"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				ObjectMapper mapper = new ObjectMapper();
				TypeFactory typeFactory = mapper.getTypeFactory();
				CollectionType collectionType = typeFactory
						.constructCollectionType(List.class, VersionData.class);
				version = mapper.readValue(responseString, collectionType);
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readNavFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "nav_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, NavData.class);
		List<NavData> data = null;
		try {
			data = mapper.readValue(responseString, collectionType);
			Collections.sort(data);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
		if (data != null) {
			String[] dates = new String[data.size()];
			double[] values = new double[data.size()];
			for (int i = 0; i < data.size(); ++i) {
				dates[i] = data.get(data.size() - i - 1).week;
				values[i] = data.get(data.size() - i - 1).nav;

			}

			navDatesTBF.add(dates);
			navValuesTBF.add(values);
		}

	}

	private static void readIndustryBreakdownFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "industry_breakdown_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, IndustryBreakdownData.class);
		try {
			industryBreakdownTBF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioDetailFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_detail_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioDetailData.class);
		try {
			portfolioDetailsTBF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioTopHoldingFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_top_holding_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioTopHoldingData.class);
		try {
			portfolioTopHoldingsTBF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioStatisticFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_statistic_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioStatisticData.class);
		try {
			portfolioStatisticsTBF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readWeeklyFundPriceFromFileTBF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "weekly_fund_price_tbf.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, WeeklyFundPriceData.class);
		try {
			weeklyFundPriceTBF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readNavFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "nav_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		String responseString = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getNavTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readIndustryBreakdownFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "industry_breakdown_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioIndustryBreakdownTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioDetailFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_detail_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioDetailTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioStatisticFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_statistic_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioStatisticTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioTopHoldingFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_top_holding_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioTopHoldingTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readWeeklyFundPriceFromDatabaseTBF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "weekly_fund_price_tbf.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getWeeklyFundPriceTBF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readNewsFromFile() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "news.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, NewsData.class);
		try {
			news = mapper.readValue(responseString, collectionType);
			Collections.sort(news);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	public static void readRemindingMessageFromFile() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "reminding_message.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, MessageData.class);
		try {
			storedRemindingMessages = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	public static void readInformationMessageFromFile() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "information_message.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, MessageData.class);
		try {
			informationMessages = mapper.readValue(responseString,
					collectionType);
			Collections.sort(informationMessages);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	public static void readUser() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "user.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
				Log.d("Response ne", responseString);
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, UserData.class);
		try {
			user = mapper.readValue(responseString, collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readNewsFromDatabase() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "news.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getNews"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readRemindingMessageFromDatabase() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getRemindingMessage"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				ObjectMapper mapper = new ObjectMapper();
				TypeFactory typeFactory = mapper.getTypeFactory();
				CollectionType collectionType = typeFactory
						.constructCollectionType(List.class, MessageData.class);
				try {
					remindingMessage = mapper.readValue(responseString,
							collectionType);
				} catch (JsonParseException e) {
					Log.d("Loi parse JSON", e.getMessage());
				} catch (JsonMappingException e) {
					Log.d("Loi parse Map", e.getMessage());
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readInformationMessageFromDatabase() {
		readInformationMessageFromFile();
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "information_message.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			if (version.get(0).lastInformationMessageDate == null) {
				response = client.execute(new HttpGet(webServiceLink
						+ "getInformationMessage&date=2000-01-01%2000:00:00"));
			} else {
				response = client.execute(new HttpGet(webServiceLink
						+ "getInformationMessage&date="
						+ version.get(0).lastInformationMessageDate.replaceAll(
								" ", "%20")));
			}
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);

				ArrayList<MessageData> currentInformationMessage = new ArrayList<MessageData>();
				ObjectMapper mapper = new ObjectMapper();
				TypeFactory typeFactory = mapper.getTypeFactory();
				CollectionType collectionType = typeFactory
						.constructCollectionType(List.class, MessageData.class);
				try {
					currentInformationMessage = mapper.readValue(
							responseString, collectionType);
					Collections.sort(currentInformationMessage);
					if (!currentInformationMessage.isEmpty()) {
						version.get(0).lastInformationMessageDate = currentInformationMessage
								.get(0).date;
					}
				} catch (JsonParseException e) {
					Log.d("Loi parse JSON", e.getMessage());
				} catch (JsonMappingException e) {
					Log.d("Loi parse Map", e.getMessage());
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
				for (int i = 0; i < currentInformationMessage.size(); ++i) {
					informationMessages.add(currentInformationMessage.get(i));
				}
				mapper.writeValue(file, informationMessages);
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	public static void writeStoredRemindingMessages() {
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "reminding_message.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(file, storedRemindingMessages);
		} catch (JsonGenerationException e) {
			Log.d("Loi JsonGeneration", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi JsonMapping", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}

	public static void writeInformationMessage() {
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "information_message.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(file, informationMessages);
		} catch (JsonGenerationException e) {
			Log.d("Loi JsonGeneration", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi JsonMapping", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}

	public static void writeUser() {
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "user.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(file, user);
		} catch (JsonGenerationException e) {
			Log.d("Loi JsonGeneration", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi JsonMapping", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi IO", e.getMessage());
		}
	}

	public static class NavData implements Comparable<NavData> {
		public String week;
		public double nav;

		@Override
		public int compareTo(NavData arg0) {
			String[] date1 = week.split("-");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(Integer.parseInt(date1[0]),
					Integer.parseInt(date1[1]) - 1, Integer.parseInt(date1[2]),
					0, 0, 0);
			String[] date2 = ((NavData) arg0).week.split("-");
			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(Integer.parseInt(date2[0]),
					Integer.parseInt(date2[1]) - 1, Integer.parseInt(date2[2]),
					0, 0, 0);
			return calendar2.compareTo(calendar1);
		}
	}

	public static class IndustryBreakdownData {
		public String date;
		public String industry;
		public String industry_vn;
		public double percentage;
	}

	public static class VersionData {
		public int version;
		public String lastInformationMessageDate;
	}

	public static class NewsData implements Comparable<NewsData> {
		public int id;
		public String header;
		public String content;
		public String header_vn;
		public String content_vn;
		public String date;

		@Override
		public int compareTo(NewsData arg0) {
			String[] dateTime1 = date.split(" ");
			String[] date1 = dateTime1[0].split("-");
			String[] time1 = dateTime1[1].split(":");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(Integer.parseInt(date1[0]),
					Integer.parseInt(date1[1]) - 1, Integer.parseInt(date1[2]),
					Integer.parseInt(time1[0]), Integer.parseInt(time1[1]),
					Integer.parseInt(time1[2]));
			String[] dateTime2 = ((NewsData) arg0).date.split(" ");
			String[] date2 = dateTime2[0].split("-");
			String[] time2 = dateTime2[1].split(":");
			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(Integer.parseInt(date2[0]),
					Integer.parseInt(date2[1]) - 1, Integer.parseInt(date2[2]),
					Integer.parseInt(time2[0]), Integer.parseInt(time2[1]),
					Integer.parseInt(time2[2]));
			return calendar2.compareTo(calendar1);
		}
	}

	public static class PortfolioDetailData {
		public String date;
		public String asset;
		public String asset_vn;
		public double percentage;
	}

	public static class PortfolioStatisticData {
		public String date;
		public double net_asset_value;
		public double turnover_rate;
		public double total_position;
	}

	public static class PortfolioTopHoldingData {
		public String date;
		public String company_name;
		public String company_name_vn;
		public double percentage;
	}

	public static class MessageData implements Comparable<MessageData> {
		public int id;
		public String title;
		public String content;
		public String title_vn;
		public String content_vn;
		public String date;

		@Override
		public int compareTo(MessageData another) {
			String[] dateTime1 = date.split(" ");
			String[] date1 = dateTime1[0].split("-");
			String[] time1 = dateTime1[1].split(":");
			Calendar calendar1 = Calendar.getInstance();
			calendar1.set(Integer.parseInt(date1[0]),
					Integer.parseInt(date1[1]) - 1, Integer.parseInt(date1[2]),
					Integer.parseInt(time1[0]), Integer.parseInt(time1[1]),
					Integer.parseInt(time1[2]));
			String[] dateTime2 = ((MessageData) another).date.split(" ");
			String[] date2 = dateTime2[0].split("-");
			String[] time2 = dateTime2[1].split(":");
			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(Integer.parseInt(date2[0]),
					Integer.parseInt(date2[1]) - 1, Integer.parseInt(date2[2]),
					Integer.parseInt(time2[0]), Integer.parseInt(time2[1]),
					Integer.parseInt(time2[2]));
			return calendar2.compareTo(calendar1);
		}
	}

	public static class WeeklyFundPriceData {
		public String date;
		public double nav_unit;
		public double nav_change;
		public double pop;
		public double highest_nav_unit;
		public double lowest_nav_unit;
	}

	public static class UserData {
		public String username;
		public String password;
		public String investor_name;
		public String investor_folio_number;
		public double investor_holding;
		public double investor_nav;
		public double investor_value;
		public String investor_type;
	}

	private static void readNavFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "nav_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, NavData.class);
		List<NavData> data = null;
		try {
			data = mapper.readValue(responseString, collectionType);
			Collections.sort(data);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
		if (data != null) {
			String[] dates = new String[data.size()];
			double[] values = new double[data.size()];
			for (int i = 0; i < data.size(); ++i) {
				dates[i] = data.get(data.size() - i - 1).week;
				values[i] = data.get(data.size() - i - 1).nav;

			}

			navDatesBCF.add(dates);
			navValuesBCF.add(values);
		}

	}

	private static void readIndustryBreakdownFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "industry_breakdown_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, IndustryBreakdownData.class);
		try {
			industryBreakdownBCF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioDetailFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_detail_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioDetailData.class);
		try {
			portfolioDetailsBCF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioTopHoldingFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_top_holding_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioTopHoldingData.class);
		try {
			portfolioTopHoldingsBCF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readPortfolioStatisticFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_statistic_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, PortfolioStatisticData.class);
		try {
			portfolioStatisticsBCF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readWeeklyFundPriceFromFileBCF() {
		BufferedReader reader = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "weekly_fund_price_BCF.txt");
		String responseString = "";
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while (temp != null) {
				try {
					temp = reader.readLine();
					if (temp == null) {
						break;
					}
					responseString = responseString + temp;
				} catch (IOException e) {
					Log.d("Loi input output", e.getMessage());
				}
			}
			try {
				reader.close();
			} catch (IOException e) {
				Log.d("Loi input output 2", e.getMessage());
			}
		} catch (FileNotFoundException e) {
			Log.d("Khong thay file", e.getMessage());
		}
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		CollectionType collectionType = typeFactory.constructCollectionType(
				List.class, WeeklyFundPriceData.class);
		try {
			weeklyFundPriceBCF = mapper.readValue(responseString,
					collectionType);
		} catch (JsonParseException e) {
			Log.d("Loi parse JSON", e.getMessage());
		} catch (JsonMappingException e) {
			Log.d("Loi parse Map", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input output", e.getMessage());
		}
	}

	private static void readNavFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "nav_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		String responseString = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getNavBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readIndustryBreakdownFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "industry_breakdown_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioIndustryBreakdownBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioDetailFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_detail_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioDetailBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioStatisticFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_statistic_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioStatisticBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readPortfolioTopHoldingFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "portfolio_top_holding_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getPortfolioTopHoldingBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}

	private static void readWeeklyFundPriceFromDatabaseBCF() {
		BufferedWriter writer = null;
		File dir = new File(context.getFilesDir() + "/VCBF/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, "weekly_fund_price_BCF.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				Log.d("Loi input output", e1.getMessage());
			}
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(webServiceLink
					+ "getWeeklyFundPriceBCF"));
			StatusLine sl = response.getStatusLine();
			if (sl.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				Log.d("Response ne", responseString);
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(responseString);
				writer.close();
			} else {
				response.getEntity().getContent().close();
			}
		} catch (ClientProtocolException e) {
			Log.d("Loi client protocol", e.getMessage());
		} catch (IOException e) {
			Log.d("Loi input out put", e.getMessage());
		}
	}
}
