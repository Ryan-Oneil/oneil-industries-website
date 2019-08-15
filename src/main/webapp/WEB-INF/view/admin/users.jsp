<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/admin.css" />" rel="stylesheet">

    <title>Oneil - Admin - Users</title>
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
        <ul class="navBar">
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>
            <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            <li><a href="${pageContext.request.contextPath}/gallery">Images</a></li>
            <li><a href="${pageContext.request.contextPath}/services">Services</a></li>
            <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
        </ul>
    </nav>
    <div id="maindiv">
        <div class="gallerySubMenu">
            <nav class="galNav">
                <ul class="subMenu navBar">
                    <li><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
                    <li><form:form action="${pageContext.request.contextPath}/logout" class="logoutForm" method="post">
                        <input type="submit" class="logoutButton" value="Logout"/>
                    </form:form></li>
                </ul>
            </nav>
        </div>
        <div class="mainpage">
            <div style="overflow-x:auto;">
                <table class="users-table">
                    <tr>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Enabled</th>
                        <th>Option</th>
                    </tr>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.authorities.get(0).authority}</td>
                            <td>${user.enabled}</td>
                            <td><a href="${pageContext.request.contextPath}/admin/user/${user.username}">Manage</a> </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
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