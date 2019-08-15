<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">

    <script src="<c:url value="/resources/assets/scripts/image.js" />"> </script>

    <title>Oneil - My Albums - Manage</title>
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
                <h2>Click on a media to manage its options</h2>
                <a href="/gallery/album/${album.album.name}">Direct Album Link</a>
                <div class="form">
                <form:form action="/gallery/changealbum/${album.album.name}" method="post" class="login-form">
                    <p> Album Name: <input type="text" name="newAlbumName" value="${album.album.name}"> </p>
                    <p>Show Unlisted media: <select name="showUnlistedImages">
                        <option value="${album.album.showUnlistedImages}">Current: ${album.album.showUnlistedImages}</option>
                        <option value="false">False</option>
                        <option value="true">True</option>
                    </select>
                    </p>
                    <button>Save</button>
                </form:form >
                </div>
                <c:forEach items="${album.media}" var="media">
                    <div class="imageBox">
                        <p>${media.name} - ${media.dateAdded}</p>
                        <c:if test="${media.mediaType == 'image'}" >
                            <a href="${pageContext.request.contextPath}/gallery/myimages/${media.fileName}" > <img class="imgstyle lazy" data-src="${pageContext.request.contextPath}/gallery/media/thumbnail?mediaID=${media.id}" /></a>
                        </c:if>
                        <c:if test="${media.mediaType == 'video'}" >
                            <a href="${pageContext.request.contextPath}/gallery/myimages/${media.fileName}" > <video class="imgstyle lazy" data-src="${pageContext.request.contextPath}/gallery/media/${media.id}" controls width="400" height="400"></video></a>
                        </c:if>                    </div>
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