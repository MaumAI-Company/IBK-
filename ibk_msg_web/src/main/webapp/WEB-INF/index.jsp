<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>View Test Page</title>
</head>
<script type="text/javascript" src="/js/ics_common.js"></script>

<body>
  <!--
  <h2>Hello! ${name}</h2>
  <a href="/main">
    <spring:message code="com.mindslab.hello" />
  </a>
  <div>JSP List Test</div>
  <c:forEach var="item" items="${list}" varStatus="idx">
    ${idx.index}, ${item} <br />
  </c:forEach>
  -->
  <h1>Hello World</h1>
  <p>index page!</p>

  <a href="/login">login</a>
</body>

</html>