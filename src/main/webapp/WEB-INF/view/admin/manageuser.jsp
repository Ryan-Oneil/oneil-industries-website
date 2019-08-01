<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <title>Oneil - Update User</title>
</head>

<body>
<div class="main">
    <header>
        <div id="logo" >
            <h1>
                <a href="${pageContext.request.contextPath}/">Oneil Industries</a>
            </h1>
        </div>
    </header>
    <nav>
        <ul id="navBar">
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="#">About Us</a></li>
            <li><a href="#">Contact</a></li>
            <li><a href="${pageContext.request.contextPath}/gallery">Images</a></li>
            <li><a href="#">Services</a></li>
        </ul>
    </nav>
    <div class="maindiv">
        <div class="mainpage">
            <form:form action="${pageContext.request.contextPath}/admin/updateUser/${user.username}" method="post" modelAttribute="user">
                <p>Username: <form:input path="username" type="text" placeholder="${user.username}"/></p>
                <p>Email: <form:input path="email" type="text" placeholder="${user.email}"/> </p>
                <p>Role: <form:select path="role">
                    <form:option value="ROLE_UNREGISTERED"/>
                    <form:option value="ROLE_USER"/>
                    <form:option value="ROLE_ADMIN"/>
                </form:select></p>
                <p>Enabled: <form:select path="enabled" placeholder="${user.enabled}">
                    <form:option value="1">True</form:option>
                    <form:option value="0">False</form:option>
                </form:select></p>
            <input type="submit" value="Save">
            </form:form>
        </div>
    </div>


</div>

<div class="footer">
    <footer>
        &copy; Oneil Industries. All rights reserved.
    </footer>
</div>
</body>
</html>