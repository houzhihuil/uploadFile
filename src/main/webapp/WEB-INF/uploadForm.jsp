<%--
  Created by IntelliJ IDEA.
  User: PHILIP
  Date: 2023-03-26
  Time: 8:39 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="uploadFile" method="POST" enctype="multipart/form-data">
    <input type="file" name="file" />
    <input type="submit" value="Upload" />
</form>

</body>
</html>
