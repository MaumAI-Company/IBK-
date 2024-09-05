<%@ page import="java.net.URLDecoder" %>
<%

	request.setCharacterEncoding("UTF-8");

	String urlName = "returnURL=";
	String returnURL = getNextUrl(request.getQueryString(), urlName);

	try {
		returnURL = URLDecoder.decode(returnURL, "UTF-8");
	} catch(Exception e) {}
	
	returnURL = returnURL.replaceAll("\\$", "\\&");
	System.out.println(" ### returnURL : "+returnURL);

	/************************************************************
	 *	returnURL 설정
	 ************************************************************/	
	if (!returnURL.trim().equals("")) session.setAttribute("returnURL", returnURL);	
	response.sendRedirect("./business.jsp");

%>

<%!
	public String getNextUrl(String fullPath, String urlName) {
		if(null == fullPath) return "";
		int idx = fullPath.indexOf(urlName);
		String nextURL = "";
		if(idx > -1)
			nextURL =  fullPath.substring(idx+urlName.length());
		
		return nextURL;
	}
%>  
