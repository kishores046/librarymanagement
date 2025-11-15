<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Borrow Successful</title></head>
<body>

<h2>Book Borrowed Successfully!</h2>

<p>Borrow ID: ${record.borrowId}</p>
<p>Due Date: ${record.dueDate}</p>

<a href="home">Back to Home</a>

</body>
</html>
