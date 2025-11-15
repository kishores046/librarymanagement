<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />
<h2>Register</h2>

<form:form modelAttribute="signupForm" action="${pageContext.request.contextPath}/customer/signup" method="post">
    <div>
        <label>Name</label><br/>
        <form:input path="name"/>
        <form:errors path="name" cssClass="error"/>
    </div>

    <div>
        <label>Email</label><br/>
        <form:input path="email"/>
        <form:errors path="email" cssClass="error"/>
    </div>

    <div>
        <label>Phone</label><br/>
        <form:input path="phone"/>
        <form:errors path="phone" cssClass="error"/>
    </div>

    <div>
        <label>Membership</label><br/>
        <form:select path="membershipType">
            <form:option value="REGULAR" label="REGULAR"/>
            <form:option value="ELITE" label="ELITE"/>
            <form:option value="PREMIUM" label="PREMIUM"/>
            <form:option value="OCCASIONAL" label="OCCASIONAL"/>
        </form:select>
        <form:errors path="membershipType" cssClass="error"/>
    </div>

    <button type="submit">Sign up</button>
</form:form>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp" />


