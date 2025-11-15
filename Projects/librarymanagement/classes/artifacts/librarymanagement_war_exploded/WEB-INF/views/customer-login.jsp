<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head><title>Customer Login</title></head>
<body>

<h2>Library Login</h2>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<form action="${pageContext.request.contextPath}/customer/login" method="post">
    <label>Customer ID:</label>
    <label>
        <input type="text" name="customerId" required>
    </label><br><br>
    <button type="submit">Login</button>
</form>

<p>Not registered? <a href="signup">Sign Up</a></p>

</body>
</html>

