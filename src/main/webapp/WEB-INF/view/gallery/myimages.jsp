<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
	<title>Oneil - Gallery</title>
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
		<div class="gallerySubMenu">
			<nav class="galNav">
				<ul id="navBar" class="subMenu">
					<li><a href="${pageContext.request.contextPath}/gallery/upload">Upload</a></li>
					<li><a href="${pageContext.request.contextPath}/gallery/myimages">My Images</a></li>
					<li><a href="#">Logout</a></li>
				</ul>
			</nav>
		</div>
		<div class="mainpage">
			<div class="images">
				<h1>Click on a image to manage its options</h1>
				<c:forEach items="${userimages}" var="image">
					<div class="imageBox">
						<p>${image.name} ${image.dateAdded}</p>
						<a href="${pageContext.request.contextPath}/gallery/myimages/${image.fileName}"> <img src="${pageContext.request.contextPath}/gallery/images/thumbnail/${image.fileName}" /></a>
					</div>
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