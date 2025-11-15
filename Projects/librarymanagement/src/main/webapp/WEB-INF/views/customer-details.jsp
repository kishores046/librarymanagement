<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 15:16
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
        <h2>Account: ${customer.name}</h2>
        <p>Membership: ${customer.membership.membershipType} (Status: ${customer.membership.status})</p>

        <h3>Active Borrows</h3>
        <c:if test="${empty activeBorrows}">
            <p>No active borrows.</p>
        </c:if>

        <c:forEach var="r" items="${activeBorrows}">
            <div style="border:1px solid #ccc;padding:8px;margin:8px 0;">
                <p>Borrow ID: ${r.borrowId}</p>
                <p>Book ID: ${r.bookId}</p>
                <p>Borrowed On: ${r.borrowDate}</p>
                <p>Due Date: ${r.dueDate}</p>
                <p>Status: ${r.borrowStatus}</p>
            </div>
        </c:forEach>

    </div>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />
