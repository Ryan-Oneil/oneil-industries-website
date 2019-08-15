<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">

	<title>Oneil - Contact</title>

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
					<form:form action="${pageContext.request.contextPath}/contact" method="POST" modelAttribute="ContactForm" class="login-form" >
						<form:errors path="name" />
						<form:input name="name" type="text" placeholder="Name" path="name"/>
						<form:errors path="email" />
						<form:input name="email" type="text" placeholder="Email" path="email"/>
						<form:errors path="subject" />
						<form:input name="subject" type="text" placeholder="Subject" path="subject"/>
						<form:errors path="message" />
						<form:textarea name="message" placeholder="message" path="message"/>
						<button>Send</button>
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