<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Toll
  Date: 5/05/2018
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Statistic</title>
</head>
<body>
<h1>Statistic</h1>
<h2>Current depth = ${stat.currentDepth}</h2>
<table border="1">
    <tr>
        <th>Proceeded</th>
        <th>Found pages</th>
        <th>Error urls</th>
        <th colspan="1">Action</th>
    </tr>
    <tr>
        <td>${stat.proceededUrls.size()}</td>
        <td>${stat.searchingResults.size()}</td>
        <td>${stat.errorUrls.size()}</td>
        <td><a href="/stop">Stop</a></td>
    </tr>
</table>
<script src="//code.jquery.com/jquery-1.6.2.min.js"></script>
<script>
    function reload() {
        location.reload(true);
    }
    setTimeout(reload, 2000);
</script>
</body>
</html>
