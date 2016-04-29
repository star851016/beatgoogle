package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearch {
	public String queryStr;
	public String url;
	public String content;

	public GoogleSearch(String queryStr) throws IOException {
		this.queryStr = queryStr;
		this.url = "http://www.google.com/search?q=" + URLEncoder.encode(queryStr+" 甜點", "UTF-8") + "&num=20&oe=utf8";
	}

	private String fetchContent() throws IOException {
		String retVal = "";
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		conn.setRequestProperty("User-agent", "Chrome/47.0.2526.106");
		InputStream in = conn.getInputStream();
		InputStreamReader inReader = new InputStreamReader(in, "utf-8");
		BufferedReader bufReader = new BufferedReader(inReader);

		String line = null;
		while ((line = bufReader.readLine()) != null) {
			retVal += line;
		}

		return retVal;

	}

	public HashMap<String, String> getResults(PrintWriter webWriter) throws IOException {

		if (content == null) {
			content = fetchContent();
		}

		HashMap<String, String> retVal = new HashMap<String, String>();

		Document doc = Jsoup.parse(content);

		Elements lis = doc.select("div.g");

		for (Element li : lis) {
			try {
				Element h3 = li.select("h3.r").get(0);
				String title = h3.text();

				Element cite = li.select("cite").get(0);
				String citeUrl = cite.text();

				retVal.put(title, citeUrl);

			} catch (IndexOutOfBoundsException e) {

			}

		}

		return retVal;
	}
}