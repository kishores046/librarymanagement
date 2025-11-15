<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div style="background:#333;color:#fff;padding:10px;">
    <div style="max-width:1000px;margin:0 auto;display:flex;align-items:center;justify-content:space-between;">
        <div>
            <a href="${pageContext.request.contextPath}/home" style="color:white;text-decoration:none;">
                <strong>LibraryMgmt</strong>
            </a>
        </div>
        <div>
            <c:choose>
                <c:when test="${not empty sessionScope.customer}">
                    Hello, ${sessionScope.customer.name} |
                    <a href="${pageContext.request.contextPath}/customer/details" style="color:#ddd">My Account</a> |
                    <a href="${pageContext.request.contextPath}/logout" style="color:#ddd">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/customer/login" style="color:#ddd">Login</a> |
                    <a href="${pageContext.request.contextPath}/customer/signup" style="color:#ddd">Sign up</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

