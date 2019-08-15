<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/admin.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">


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
            <div class="form">
                <form:form action="${pageContext.request.contextPath}/admin/updateUser/${user.username}" method="post" modelAttribute="user" class="login-form">
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
                <button>Save</button>
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

                <div class="serviceProfile">
                    <h2>Teamspeak Profile</h2>

                    <table>
                        <tr>
                            <th>Teamspeak Name</th>
                            <th>Teamspeak UUID</th>
                            <th>Manage</th>
                        </tr>
                        <c:if test="${not empty teamspeakProfiles}">
                            <c:forEach items="${teamspeakProfiles}" var="teamspeakProfile">
                                <tr>
                                    <td>${teamspeakProfile.teamspeakName}</td>
                                    <td>${teamspeakProfile.uuid}</td>
                                    <td><a href="${pageContext.request.contextPath}/profile/serviceDelete?uuid=${teamspeakProfile.id}&service=teamspeak">Delete</a> </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>

                    <h2>Discord Profile</h2>

                    <table>
                        <tr>
                            <th>Discord Name</th>
                            <th>Discord UUID</th>
                            <th>Manage</th>
                        </tr>
                        <c:if test="${not empty discordProfiles}">
                            <c:forEach items="${discordProfiles}" var="discordProfile">
                                <tr>
                                    <td>${discordProfile.discordName}</td>
                                    <td>${discordProfile.uuid}</td>
                                    <td><a href="${pageContext.request.contextPath}/profile/serviceDelete?uuid=${discordProfile.id}&service=discord">Delete</a> </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
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