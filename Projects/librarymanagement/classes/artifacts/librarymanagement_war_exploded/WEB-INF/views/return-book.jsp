<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Return Book</title></head>
<body>

<h2>Return a Book</h2>

<form action="return-book.jsp" method="post">
    <label>Enter Borrow ID:</label>
    <label>
        <input type="text" name="borrowId" required>
    </label>
    <button type="submit">Return</button>
</form>

</body>
</html>

