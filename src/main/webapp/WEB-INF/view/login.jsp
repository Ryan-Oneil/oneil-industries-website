<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">
	<script src="<c:url value="/resources/assets/scripts/login.js" />"> </script>

	<title>Oneil - Login</title>

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
		<ul id="navBar">
			<li><a href="${pageContext.request.contextPath}/">Home</a></li>
			<li><a href="#">About Us</a></li>
			<li><a href="#">Contact</a></li>
			<li><a class="active" href="${pageContext.request.contextPath}/gallery">Images</a></li>
			<li><a href="#">Services</a></li>
		</ul>
	</nav>
	<div class="maindiv">
		<div class="mainpage">
			<div class="login-page">
				<div class="form">
					<form:form action="${pageContext.request.contextPath}/register" method="POST" class="register-form">

						<input type="text" name="username" placeholder="name"/>
						<input type="password" name="password" placeholder="password"/>
						<input type="email" name="email" placeholder="email address"/>
						<button>create</button>
						<p class="message">Already registered? <a href="#" onclick="changeFormOption()">Sign In</a></p>
					</form:form>
					<form:form action="${pageContext.request.contextPath}/authenticateUser" method="POST" class="login-form">
						<c:if test="${param.error != null}">
							<div class="alert alert-danger col-xs-offset-1 col-xs-10">
								Invalid username and password.
							</div>
						</c:if>

						<c:if test="${param.logout != null}">
							<div class="alert alert-success col-xs-offset-1 col-xs-10">
								You have been logged out.
							</div>
						</c:if>
						<input name="username" type="text" placeholder="username"/>
						<input name="password" type="password" placeholder="password"/>
						<button>login</button>
						<p class="message">Not registered? <a href="#" onclick="changeFormOption()">Create an account</a></p>
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