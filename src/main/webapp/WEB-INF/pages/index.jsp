<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Start page</title>
</head>
<body>
<h1>
    Insert initial parameters:
</h1>
<form:form action="startApp" method="post">
    <table border="1">
        <tr>
            <td>initial url</td>
            <td><input type="text" name="initialUrl" ></td>
        </tr>
        <tr>
            <td>target String</td>
            <td><input type="text" name="targetString"></td>
        </tr>
        <tr>
            <td>thread number</td>
            <td><input type="number" name="threadNumber"></td>
        </tr>
        <tr>
            <td>depth Level</td>
            <td><input type="number" name="depthLevel"></td>
        </tr>
    </table>
    <input type="submit" value="Submit">
</form:form>
</body>
</html>
