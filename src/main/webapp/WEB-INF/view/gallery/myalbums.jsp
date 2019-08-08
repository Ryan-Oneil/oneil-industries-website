<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
	<title>Oneil - My Albums</title>
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
				<h2>Click on a album to manage its options</h2>
				<c:forEach items="${albums}" var="albums">
					<div class="album">
						<p>${albums.album.name}</p>
						<a href="${pageContext.request.contextPath}/gallery/managealbum/${albums.album.name}">
							<c:if test="${not empty albums.media}">
								<img src="${pageContext.request.contextPath}/gallery/images/thumbnail/${albums.media.get(0).fileName}" />
							</c:if>
							<c:if test="${empty albums.media}">
								<img src=<c:url value="/resources/assets/images/noimage.png"/> />
							</c:if>
						</a>
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