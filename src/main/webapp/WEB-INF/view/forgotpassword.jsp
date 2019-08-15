<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">

    <title>Oneil - Forgot Password</title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
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
        <div class="mainpage">
            <div class="login-page">
                <div class="form">
                    <c:if test="${message != null}">
                        <p>${message}</p>
                    </c:if>
                    <form:form action="${pageContext.request.contextPath}/forgotPassword" method="POST" class="login-form" >
                        <p class="message">Enter your registered email address to reset password</p>
                        <input name="email" type="text" placeholder="email address"/>
                        <button>Submit</button>
                    </form:form>
                </div>
            </div>
        </div>
    </div>

    <div class="footer">
        <footer>
            &copy; Oneil Industries. All rights reserved.
        </footer>
    </div>
</div>
</body>

</html>