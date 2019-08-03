<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/admin.css" />" rel="stylesheet">

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
            <div>
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

            <div class="user-info">
                <table>
                    <tr>
                        <th>Total Media uploaded</th>
                        <th>Album Count</th>
                    </tr>
                    <tr>
                        <td class = "user-data">${mediaCount}</td>
                        <td class = "user-data">${albumCount}</td>
                    </tr>
                    <tr>
                        <td class = "user-data"><a href="/gallery/${user.username}/media">View Media</a> </td>
                        <td class = "user-data"><a href="/gallery/${user.username}/albums">View Albums</a> </td>
                    </tr>
                    <tr>
                        <td class = "user-data"><a href="/admin/user/${user.username}/hideMedia">Hide All Media</a> </td>
                        <td class = "user-data"><a href="#">Delete All Media</a> </td>

                    </tr>
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