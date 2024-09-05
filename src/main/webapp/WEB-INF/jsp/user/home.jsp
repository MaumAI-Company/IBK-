<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib
uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>UserPage</title>
</head>

<body>
    <h1>Hello ${user} user</b></h1>
    <p>user role is ${roles}</b></p>
    <a href="/admin">admin</a>
    <a href="/mindslab/logout">logout</a>
</body>

</html>