<%--
  Created by IntelliJ IDEA.
  User: Kishore
  Date: 15-11-2025
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Borrow Options</title></head>
<body>

<h2>Borrow a Book</h2>

<h3>Find by Genre</h3>
<form action="search/genre" method="get">
    <label>
        <select name="genre">
            <option>Programming</option>
            <option>AI</option>
            <option>Software Engineering</option>
            <option>Computer Science</option>
            <option>Project Management</option>
        </select>
    </label>
    <button type="submit">Search</button>
</form>

<h3>Find by Book Name</h3>
<form action="search/name" method="get">
    <label>
        <input type="text" name="name">
    </label>
    <button type="submit">Search</button>
</form>

<h3>Find by Author</h3>
<form action="search/author" method="get">
    <label>
        <input type="text" name="author">
    </label>
    <button type="submit">Search</button>
</form>

<a href="../home">Back</a>

</body>
</html>
