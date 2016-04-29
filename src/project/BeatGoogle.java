package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class BeatGoogle {

	public ArrayList<WebN> inorderWebNodes;
	private ArrayList<Keyword> keywords;

	public BeatGoogle(ArrayList<Keyword> keywords) {
		this.keywords = keywords;

	}

	public void search(String queryStr, PrintWriter webWriter) throws IOException {

		GoogleSearch google = new GoogleSearch(queryStr);

		HashMap<String, String> getResults = google.getResults(webWriter);

		ArrayList<WebN> webNodes = new ArrayList<WebN>();

		for (Entry<String, String> getResult : getResults.entrySet()) {
			String url = getResult.getValue().toLowerCase();

			if (!url.startsWith("http") && url != null) {
				url = "http://" + url;
			}
			String name = getResult.getKey();

			webNodes.add(new WebN(url, name));

		}

		for (WebN nodeWeb : webNodes) {

			nodeWeb.assess(keywords, 0);

			calcTotalScore(nodeWeb);
		}

		inorderWebNodes = doQuickSort(webNodes);
		for (WebN nodeWeb : webNodes) {

			nodeWeb.assess(keywords, 0);

			calcTotalScore(nodeWeb);
		}

		inorderWebNodes = doQuickSort(webNodes);

	}

	private void calcTotalScore(WebN webNode) {

		for (WebN child : webNode.getChildren()) {
			calcTotalScore(child);
		}

		webNode.TotalScore = webNode.subScore;
		for (WebN child : webNode.getChildren()) {
			webNode.TotalScore += child.TotalScore;
		}

	}

	private ArrayList<WebN> doQuickSort(ArrayList<WebN> webs) {
		if (webs.size() < 2) {
			return webs;
		}

		ArrayList<WebN> retVal = new ArrayList<WebN>();

		int indexOfPivot = webs.size() / 2;
		WebN pivotWeb = webs.get(indexOfPivot);

		ArrayList<WebN> lt = new ArrayList<WebN>();
		ArrayList<WebN> eq = new ArrayList<WebN>();
		ArrayList<WebN> gt = new ArrayList<WebN>();

		for (int i = 0; i < webs.size(); i++) {
			WebN w = webs.get(i);
			if (w.TotalScore > pivotWeb.TotalScore) {
				gt.add(w);
			} else if (w.TotalScore < pivotWeb.TotalScore) {
				lt.add(w);
			} else {
				eq.add(w);
			}
		}

		retVal.addAll(doQuickSort(gt));
		retVal.addAll(eq);
		retVal.addAll(doQuickSort(lt));

		return retVal;

	}

}
