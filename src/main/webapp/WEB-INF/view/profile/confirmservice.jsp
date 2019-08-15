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
            <li><a class="active" href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>
            <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            <li><a href="${pageContext.request.contextPath}/gallery">Images</a></li>
            <li><a href="${pageContext.request.contextPath}/services">Services</a></li>
            <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
        </ul>
    </nav>
    <div id="maindiv">
        <div class="mainpage">
            <p><h2>${msg}</h2></p>
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