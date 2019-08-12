<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/loginstyle.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/assets/scripts/album.js" />"> </script>


    <title>Oneil - Manage Image</title>
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
                <div class="imageUpload">
                    <h1>${media.name} - ${media.dateAdded}</h1>
                    <c:if test="${media.mediaType == 'image'}" >
                        <a href="${pageContext.request.contextPath}/gallery/media?mediaID=${media.id}" > <img class="imgstyle" src="${pageContext.request.contextPath}/gallery/media/thumbnail?mediaID=${media.id}" /></a>
                    </c:if>
                    <c:if test="${media.mediaType == 'video'}" >
                        <a href="${pageContext.request.contextPath}/gallery/media?mediaID=${media.id}" > <video class="imgstyle" src="${pageContext.request.contextPath}/gallery/media?mediaID=${media.id}" controls height="400" width="400"></video> </a>
                    </c:if>
                    <div class="form">
                    <form:form action="${pageContext.request.contextPath}/gallery/update/${media.fileName}" method="post" modelAttribute="GalleryUpload" class="login-form">
                        <input type="hidden" name="id" placeholder="${media.id}">
                        <p>Image Name: <form:input type="text" name="imageName" placeholder="${media.name}" path="name"/></p>
                        <p>Link Status: <form:select name="privacy" path="privacy">
                            <option value="${media.linkStatus}">Current: ${media.linkStatus}</option>
                            <option value="unlisted">Unlisted</option>
                            <option value="public">Public</option>
                            <option value="private">Private</option>
                        </form:select>
                        </p>
                        <p>Album: <form:select id="albumoption" name="albumName" onchange="checkAlbum()" path="albumName">
                            <c:if test="${GalleryUpload.albumName != null}">
                                <option value="${GalleryUpload.albumName}">Current: ${GalleryUpload.albumName}</option>
                            </c:if>
                            <option value="none">None</option>
                            <option value="new">new</option>
                            <c:forEach items="${albums}" var="album">
                                <option value="${album.name}">${album.name}</option>
                            </c:forEach>
                        </form:select></p>
                        <div id="newAlbumOptions" style="display: none;">
                            <p>Album Name: <form:input type="text" name="newAlbumName" placeholder="Album name" path="newAlbumName"/></p>
                            <p>Show Unlisted media: <form:select name="showUnlistedImages" path="showUnlistedImages">
                                <option value="false">False</option>
                                <option value="true">True</option>
                            </form:select>
                            </p>
                        </div>
                        <button style="margin-bottom: 20px !important;">Save</button>
                        <a class="deleteButton" href="${pageContext.request.contextPath}/gallery/delete/${media.fileName}">Delete Image</a>

                    </form:form>
                    </div>
                </div>
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