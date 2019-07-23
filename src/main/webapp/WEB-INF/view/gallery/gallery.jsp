<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Oneil - Images</title>
</head>
<body>
    <c:forEach items="${images}" var="image">
        <p>This is an image test</p>
        <p>The image path is ${image.name} and ${image.path}</p>
        <img src="${pageContext.request.contextPath}/gallery/images/${image.name}" />
    </c:forEach>
</body>
</html>
