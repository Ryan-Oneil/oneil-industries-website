<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/resources/assets/css/style.css" />" rel="stylesheet">
    <title>Oneil - About</title>
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
            <li><a class="active" href="${pageContext.request.contextPath}/about">About Us</a></li>
            <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            <li><a href="${pageContext.request.contextPath}/gallery">Images</a></li>
            <li><a href="${pageContext.request.contextPath}/services">Services</a></li>
        </ul>
    </nav>
    <div id="maindiv">
        <div class="mainpage">
            <h1>Oneil Industries</h1>
            <p>What started as a random joke on a Garry's Mod server. Now turned into a simplistic friendly community that provides the necessary hosted services to its members.</p>

            <h2>Founding Members</h2>
            <table class="memberTable">

                <tr>
                    <th>Name</th>
                    <th>Title</th>
                </tr>

                <tr>
                    <td>Ryan O'Neil</td>
                    <td>CEO</td>
                </tr>

                <tr>
                    <td>Samusen</td>
                    <td>COO</td>
                </tr>

                <tr>
                    <td>Ady</td>
                    <td>CIO</td>
                </tr>

                <tr>
                    <td>Jack Bushross</td>
                    <td>CBO</td>
                </tr>
            </table>
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