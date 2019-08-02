<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
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
                        <a href="${pageContext.request.contextPath}/gallery/images/${media.fileName}" > <img src="${pageContext.request.contextPath}/gallery/images/thumbnail/${media.fileName}" /></a>
                    </c:if>
                    <c:if test="${media.mediaType == 'video'}" >
                        <a href="${pageContext.request.contextPath}/gallery/images/${media.fileName}" > <video src="${pageContext.request.contextPath}/gallery/images/${media.fileName}" controls width="400" height="400"></video></a>
                    </c:if>
                    <p><a href="${pageContext.request.contextPath}/gallery/delete/${media.fileName}">Delete Image</a></p>
                    <form:form action="${pageContext.request.contextPath}/gallery/update/${media.fileName}" method="post" modelAttribute="GalleryUpload">
                        <input type="hidden" name="id" placeholder="${media.id}">
                        <p>Image Name: <form:input type="text" name="imageName" placeholder="${media.name}" path="name"/></p>
                        <p>Link Status: <form:select name="privacy" path="privacy">
                            <option hidden value="${media.linkStatus}"></option>
                            <option value="unlisted">Unlisted</option>
                            <option value="public">Public</option>
                            <option value="private">Private</option>
                        </form:select>
                        </p>
                        <p>Album: <form:select id="albumoption" name="albumName" onchange="checkAlbum()" path="albumName">
                            <option value="none">None</option>
                            <option value="new">new</option>
                            <c:forEach items="${albums}" var="album">
                                <option value="${album.name}">${album.name}</option>
                            </c:forEach>
                        </form:select></p>
                        <div id="newAlbumOptions" style="display: none;">
                            <p>Album Name: <form:input type="text" name="newalbumName" placeholder="Album name" path="newalbumName"/></p>
                            <p>Show Unlisted media: <form:select name="showUnlistedImages" path="showUnlistedImages">
                                <option value="false">False</option>
                                <option value="true">True</option>
                            </form:select>
                            </p>
                        </div>
                        <input type="submit" value="Save">
                    </form:form>
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