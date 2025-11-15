<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Search Results</title></head>
<body>

<h2>Search Results</h2>

<c:if test="${empty books}">
    <p>No books found.</p>
</c:if>

<c:forEach var="b" items="${books}">
    <div style="border:1px solid #ccc; padding:10px; margin:10px">
        <h3>${b.bookName}</h3>
        <p>Author: ${b.authorName}</p>
        <p>Genre: ${b.genre}</p>
        <p>Available Copies: ${b.availableCopies}</p>

        <c:if test="${b.availableCopies > 0}">
            <form action="../borrow" method="post">
                <input type="hidden" name="bookId" value="${b.bookId}">
                <button type="submit">Borrow</button>
            </form>
        </c:if>

        <c:if test="${b.availableCopies == 0}">
            <p style="color:red">Not Available</p>
        </c:if>
    </div>
</c:forEach>

<a href="../borrow-options">Back</a>

</body>
</html>

