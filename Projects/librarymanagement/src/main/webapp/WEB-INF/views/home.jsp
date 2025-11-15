<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<div style="max-width:1000px;margin:20px auto;display:flex;">
    <div style="width:200px;padding-right:20px;">
        <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />
    </div>
    <div style="flex:1;">
        <!-- page content here -->
    </div>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />

<html>
<head><title>Library Home</title></head>
<body>

<h2>Welcome, ${customer.name}!</h2>

<h3>Your Membership: ${customer.membership.membershipType}</h3>

<ul>
    <li><a href="${pageContext.request.contextPath}/borrow-options.jsp">Borrow Book</a></li>
    <li><a href="${pageContext.request.contextPath}/return-book.jsp">Return Book</a></li>
    <li><a href="customer/details">View Your Details & Active Borrows</a></li>
    <li><a href="${pageContext.request.contextPath}/logout.jsp">Logout</a></li>
</ul>

</body>
</html>
