package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query
 */
@WebServlet(name = "QueryServlet", urlPatterns = { "/Query" })

public class Query extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Query() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*public void redirect(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException  
	{  
	        response.setContentType("text/html; charset=UTF-8"); 
	        ServletContext sc = getServletContext(); 
	        RequestDispatcher rd = null;  
	        rd = sc.getRequestDispatcher("/index.jsp");
	        rd.forward(request, response); }  
	*/
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Keyword> keywords = new ArrayList<Keyword>();
	
		keywords.add(new Keyword("dessert", 200));
		keywords.add(new Keyword("甜點", 200));
		keywords.add(new Keyword("下午茶", 100));
		keywords.add(new Keyword("咖啡廳", 100));
		keywords.add(new Keyword("cafe", 100));
		keywords.add(new Keyword("甜點店", 100));
		keywords.add(new Keyword("好吃", 150));
		keywords.add(new Keyword("店家", 10));
		keywords.add(new Keyword("訂位", 10));
		keywords.add(new Keyword("sweet", 10));
		keywords.add(new Keyword("delicious", 150));
		keywords.add(new Keyword("維基百科", -1000));
		keywords.add(new Keyword("Wiki", -1000));
		keywords.add(new Keyword("購物商城", -100));
			
			BeatGoogle beatGoogle = new BeatGoogle(keywords);
			
			
			String searchKeyword=request.getParameter("searchWord");
			
			response.setCharacterEncoding("utf-8");
			response.setHeader("content-type", "text/html;charset=UTF-8");			
			PrintWriter webWriter=new PrintWriter(response.getOutputStream());
           
			webWriter.println("<h1>"+searchKeyword+"</h1>");
	
			beatGoogle.search(searchKeyword,webWriter);
		
			webWriter.flush();
			webWriter.close();
		}
	*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ArrayList<Keyword> keywords = new ArrayList<Keyword>();

		keywords.add(new Keyword("dessert", 200));
		keywords.add(new Keyword("甜點", 200));
		keywords.add(new Keyword("下午茶", 100));
		keywords.add(new Keyword("咖啡廳", 100));
		keywords.add(new Keyword("cafe", 100));
		keywords.add(new Keyword("甜點店", 100));
		keywords.add(new Keyword("好吃", 150));
		keywords.add(new Keyword("店家", 10));
		keywords.add(new Keyword("訂位", 10));
		keywords.add(new Keyword("sweet", 10));
		keywords.add(new Keyword("delicious", 150));
		keywords.add(new Keyword("維基百科", -1000));
		keywords.add(new Keyword("Wiki", -1000));
		keywords.add(new Keyword("購物商城", -100));

		BeatGoogle beatGoogle = new BeatGoogle(keywords);

		String searchKeyword = request.getParameter("searchWord");

		response.setCharacterEncoding("utf-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter webWriter = new PrintWriter(response.getOutputStream());

		webWriter.println("<html>");
		webWriter.println("<body>");

		webWriter.println("<h1><b>" + searchKeyword + "</b></h1>");
		beatGoogle.search(searchKeyword, webWriter);

		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		for (WebN nodeweb : beatGoogle.inorderWebNodes) {
			if (nodeweb.getName().indexOf("PageNotFound") == -1 && nodeweb.getTotalScore() > 0) {
				   sb.append("<tr><td><a href=\"" + nodeweb.getURL() +"\">"+ nodeweb.getName() + "</a></td><td>" + nodeweb.getURL()
						+ "</td></tr>");
				}
		}
		sb.append("</table></body>");
		webWriter.println(sb.toString());

		webWriter.flush();
		webWriter.close();
	}

}
