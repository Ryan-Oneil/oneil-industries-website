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
            <div class="imageUpload">
                <form:form action="${pageContext.request.contextPath}/gallery/upload" method="POST" enctype="multipart/form-data">
                    <div class="details">
                        <p>Image: <input type="file" name="image"/></p>
                        <p>Name: <input type="text" name="name"/></p>
                        <p>Link Status: <select name="privacy">
                            <option value="unlisted">Unlisted</option>
                            <option value="public">Public</option>
                            <option value="private">Private</option>
                        </select>
                        </p>
                        <p>Album: <select id="albumoption" name="albumName" onchange="checkAlbum()">
                            <option value="none">None</option>
                            <!-- Add some JS here for creation of new albums -->
                            <option value="new">new</option>
                            <c:forEach items="${albums}" var="album">
                                <option value="${album.name}">${album.name}</option>
                            </c:forEach>
                        </select></p>
                        <div id="newAlbumOptions" style="display: none;">
                            <p>Album Name: <input type="text" name="newalbumName" placeholder="Album name"></p>
                            <p>Show Unlisted images: <select name="showUnlistedImages">
                                <option value="false">False</option>
                                <option value="true">True</option>
                            </select>
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