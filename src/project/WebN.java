package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Remove;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class WebN {

	private static int maxchild = 1;

	public String url;
	public String name;
	public float subScore;
	public float TotalScore;

	private WebN parent;
	private ArrayList<WebN> children;

	private String content;

	public WebN(String url) {
		this(url, "Unknown");
	}

	public WebN(String url, String name) {
		this.url = url;
		this.name = name;
		this.children = new ArrayList<WebN>();
	}

	public WebN getParent() {
		return parent;
	}

	public ArrayList<WebN> getChildren() {
		return children;
	}

	public int getDepth() {
		if (getParent() == null) {
			return 0;
		}
		return getParent().getDepth() + 1;
	}

	public String getName() {
		return name;
	}

	public String getURL() {
		return url;
	}
	
	public float getTotalScore(){
		return TotalScore;
	}
	private String fetchContent() throws IOException {
		String retVal = "";
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		conn.setRequestProperty("User-agent", "Chrome/7.0.517.44");
		InputStream in = conn.getInputStream();
		InputStreamReader ir = new InputStreamReader(in, "utf-8");
		BufferedReader br = new BufferedReader(ir);

		String line = null;
		while ((line = br.readLine()) != null) {
			retVal += line;
		}

		return retVal;

	}

	public void assess(List<Keyword> keywords, int level) {
		if (level < 2) {

			try {

				if (content == null) {
					content = fetchContent();
				}

				Document doc = Jsoup.parse(content);

				Elements titles = doc.select("title");
				if (titles.size() > 0) {

					name = titles.get(0).text();

				}

				for (Keyword k : keywords) {

					int counter = countKeyword(k.name);
					subScore += counter * k.weight;
					k.count += counter;
				}

				findChild(doc, keywords, level);

			} catch (IOException e) {

				name = "PageNotFound";
				subScore = -100;
			}
		} else if (level == 2) {
			for (Keyword k : keywords) {

				int counter;
				try {
					counter = countKeyword(k.name);
					subScore += counter * k.weight;
					k.count += counter;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public int countKeyword(String keyword) throws IOException {
		if (content == null) {
			content = fetchContent();

		}
		int retVal = 0;
		int fromIdx = 0;
		int found = -1;
		while ((found = content.indexOf(keyword, fromIdx)) != -1) {
			retVal++;
			fromIdx = found + keyword.length();

		}

		return retVal;
	}

	public void findChild(Document doc, List<Keyword> keywords, int level) {

		Elements links = doc.select("a[href]");

		for (Element link : links) {
			if (getChildren().size() == WebN.maxchild) {
				break;
			}

			String href = link.attr("href").toLowerCase().trim();

			if (href.startsWith("javascript:")) {

				continue;
			}
			if (!href.startsWith("http")) {

				continue;
			}

			WebN child = new WebN(href);
			this.children.add(child);
			child.parent = this;

		}

		for (WebN childWeb : getChildren()) {
			childWeb.assess(keywords, level + 1);
		}

	}

}
