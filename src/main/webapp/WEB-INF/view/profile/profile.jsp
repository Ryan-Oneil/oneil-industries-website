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
            <li><a class="active" href="${pageContext.request.contextPath}/profile">Profile</a></li>
        </ul>
    </nav>
    <div id="maindiv">
        <div class="mainpage">
            <div class="form userForm">
                <h2>User Details</h2>
                <form:form action="${pageContext.request.contextPath}/profile/update?User=${user.username}" method="post" class="login-form">
                    <input type="text" name="email" value="${user.email}">

                    <input type="password" name="password" placeholder="password">

                    <button>Update Details</button>
                </form:form>
            </div>

            <div class="form userForm">
                <form:form action="${pageContext.request.contextPath}/profile/servicesAdd" method="post" class="login-form">

                    <h2>Services</h2>

                    <label>Teamspeak</label>
                    <select name="teamspeak">
                        <option></option>
                        <c:forEach items="${teamspeakUsers}" var="teamspeakClient">
                            <option value="${teamspeakClient.uniqueIdentifier}">${teamspeakClient.nickname}</option>
                        </c:forEach>
                    </select>

                    <label>Discord</label>
                    <select name="discord">
                        <option></option>
                        <c:forEach items="${discordUsers}" var="discordClient">
                            <option value="${discordClient.idLong}">${discordClient.user.name}</option>
                        </c:forEach>
                    </select>

                    <button>Add Services Profile</button>
                </form:form>
            </div>

            <div class="serviceProfile">
                <h2>Teamspeak Profile</h2>

                <table>
                    <tr>
                        <th>Teamspeak Name</th>
                        <th>Teamspeak UUID</th>
                        <th>Manage</th>
                    </tr>
                    <c:if test="${not empty userTeamspeak}">
                        <c:forEach items="${userTeamspeak}" var="teamspeakProfile">
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
                    <c:if test="${not empty userDiscord}">
                        <c:forEach items="${userDiscord}" var="discordProfile">
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

<div class="footer">
    <footer>
        &copy; Oneil Industries. All rights reserved.
    </footer>
</div>
</body>
</html>