<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
	<script src="<c:url value="/resources/assets/scripts/image.js" />"> </script>

	<title>Oneil - Images</title>
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
			<li><a href="#">About Us</a></li>
			<li><a href="#">Contact</a></li>
			<li><a class="active" href="${pageContext.request.contextPath}/gallery">Images</a></li>
			<li><a href="#">Services</a></li>
		</ul>
	</nav>
	<div id="maindiv">
		<div class="gallerySubMenu">
			<nav class="galNav">
				<ul class="subMenu navBar">
					<li><a href="${pageContext.request.contextPath}/gallery/upload">Upload</a></li>
					<li><a href="${pageContext.request.contextPath}/gallery/myimages">My Images</a></li>
					<li><a href="${pageContext.request.contextPath}/gallery/myalbums">My Albums</a></li>
					<li><form:form action="${pageContext.request.contextPath}/logout" class="logoutForm" method="post">
						<input type="submit" class="logoutButton" value="Logout"/>
					</form:form></li>
				</ul>
			</nav>
		</div>
		<div class="mainpage">
			<div class="images">
				<h1>Click on a media to manage its options</h1>
				<c:forEach items="${usermedias}" var="media">
					<div class="imageBox">
						<p>${media.name} ${media.dateAdded}</p>
						<c:if test="${media.mediaType == 'image'}" >
							<a href="${pageContext.request.contextPath}/gallery/myimages/${media.fileName}" > <img class= "lazy" data-src="${pageContext.request.contextPath}/gallery/images/thumbnail/${media.fileName}" /></a>
						</c:if>
						<c:if test="${media.mediaType == 'video'}" >
							<a href="${pageContext.request.contextPath}/gallery/myimages/${media.fileName}" > <video class="lazy" data-src="${pageContext.request.contextPath}/gallery/images/${media.fileName}" controls width="400" height="400"></video></a>
						</c:if>					</div>
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