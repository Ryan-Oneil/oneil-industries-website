<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/assets/css/imagestyle.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/assets/scripts/album.js" />"> </script>
    <title>Oneil - Upload</title>
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
    <div class="maindiv">
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
            <div class="imageUpload">
                <form:form action="${pageContext.request.contextPath}/gallery/upload" method="POST" enctype="multipart/form-data" modelAttribute="GalleryUpload">
                    <div class="details">
                        <p>Image: <form:input type="file" name="media" path="file"/> </p>
                        <p>Name: <form:input type="text" name="name" path="name"/></p>
                        <p>Link Status: <form:select name="privacy" path="privacy">
                            <form:option value="unlisted">Unlisted</form:option>
                            <form:option value="public">Public</form:option>
                            <form:option value="private">Private</form:option>
                        </form:select>
                        </p>
                        <p>Album: <form:select id="albumoption" name="albumName" onchange="checkAlbum()" path="albumName">
                            <form:option value="none">None</form:option>
                            <form:option value="new">new</form:option>
                            <c:forEach items="${albums}" var="album">
                                <form:option value="${album.name}">${album.name}</form:option>
                            </c:forEach>
                        </form:select></p>
                        <div id="newAlbumOptions" style="display: none;">
                            <p>Album Name: <form:input type="text" name="newAlbumName" placeholder="Album name" path="newAlbumName"/></p>
                            <p>Show Unlisted media: <form:select name="showUnlistedImages" path="showUnlistedImages">
                                <form:option value="false">False</form:option>
                                <form:option value="true">True</form:option>
                            </form:select>
                            </p>
                        </div>
                    </div>
                    <input type="submit" value="Submit" class="submit" />
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
</body>
</html>