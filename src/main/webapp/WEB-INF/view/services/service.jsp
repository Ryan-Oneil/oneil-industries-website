<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <title>Oneil - Home</title>
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
            <li><a class="active" href="${pageContext.request.contextPath}/services">Services</a></li>
        </ul>
    </nav>
    <div id="maindiv">
        <div class="mainpage">
            <p><h2>Oneil Industries a community dedicated to banter. </h2></p>

            <h2>Voice Services</h2>

            <div class="teamspeakList">
                <a href="ts3server://oneilindustries.biz/?port=9987"><h2>Connect to Teamspeak</h2></a>

                <c:forEach items="${teamspeakChannels}" var="channel">
                    <c:if test="${channel.parentChannelID == 0}">
                        <div class="channel">
                            <c:if test="${channel.name != ''}">
                                <p><img src="<c:url value="/resources/assets/images/teamspeakChat.png"/>">${channel.name}</p>
                            </c:if>
                        </div>
                    </c:if>
                    <c:if test="${channel.parentChannelID != 0}">
                        <div class="subChannel">
                            <c:if test="${channel.name != ''}">
                                <p><img src="<c:url value="/resources/assets/images/teamspeakChat.png"/>">${channel.name}</p>
                            </c:if>
                        </div>
                    </c:if>

                    <c:if test="${channel.usersInChannel.size() > 0}">
                        <c:forEach items="${channel.usersInChannel}" var="client">
                            <div class="user">
                                <p><img src="<c:url value="/resources/assets/images/user.png"/>">${client.nickname}</p>
                            </div>
                        </c:forEach>
                    </c:if>

                </c:forEach>
            </div>

            <div class="teamspeakList">

                <a href="https://discord.gg/TSYe5vX"><h2>Connect to Discord</h2></a>

                <c:forEach items="${discordCategories}" var="category">
                    <c:if test="${category.voiceChannels.size() > 0}">
                        <div class="channel">
                            <p><img src="<c:url value="/resources/assets/images/teamspeakChat.png"/>">${category.name}</p>

                            <c:forEach items="${category.voiceChannels}" var="channel">
                                <div class="subChannel">
                                    <p><img src="<c:url value="/resources/assets/images/teamspeakChat.png"/>">${channel.name}</p>

                                    <c:if test="${channel.members.size() > 0}">
                                        <c:forEach items="${channel.members}" var="client">
                                            <div class="user">
                                                <p><img src="<c:url value="/resources/assets/images/user.png"/>">${client.user.name}</p>
                                            </div>
                                        </c:forEach>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </c:forEach>
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