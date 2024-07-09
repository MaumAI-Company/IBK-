<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib
uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>adminPage</title>
</head>

<body>
    <h1>ADMIN PAGE</h1>
    <h1>Hello ${user} admin</b></h1>
    <p>user role is ${roles[0]}</b></p>
    <p>only access ADMIN</p>
    <a href="/mindslab/logout">logout</a>
</body>

</html>