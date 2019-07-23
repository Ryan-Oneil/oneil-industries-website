<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<ul id="navBar">
			<li><a class="active" href="${pageContext.request.contextPath}/">Home</a></li>
			<li><a href="#">About Us</a></li>
			<li><a href="#">Contact</a></li>
			<li><a href="${pageContext.request.contextPath}/gallery">Images</a></li>
			<li><a href="#">Services</a></li>
		</ul>
		</nav>
		<div class="maindiv">
			<div class="mainpage">
				
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