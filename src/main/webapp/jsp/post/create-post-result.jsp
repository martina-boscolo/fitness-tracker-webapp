<!-- Author: Martina Boscolo Bacheto -->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create Post</title>
</head>

<body>
<h1>Create Post</h1>
<hr/>

<c:import url="/jsp/post/include/show-message.jsp"/>

<c:if test="${not empty post && !message.error}">
    <ul>
        <li>postId: <c:out value="${post.postId}"/></li>
        <li>userId: <c:out value="${post.userId}"/></li>
        <li>textContent: <c:out value="${post.textContent}"/></li>

        <c:choose>
            <c:when test="${post.hasPhoto()}">

                <li>photo:
                    <ul>
                        <li>MIME media type: <c:out value="${post.photoMediaType}"/></li>
                        <li>size: <c:out value="${post.photoSize}"/></li>
                        <li>image: <br/>
                            <img
                                    src="<c:url value="/load-post-photo"><c:param name="postId" value="${post.postId}"/></c:url>"/>
                        </li>
                    </ul>
                </li>

            </c:when>

            <c:otherwise>
                <li>photo: not available</li>
            </c:otherwise>
        </c:choose>
        <li>postDate: <c:out value="${post.postDate}"/></li>

    </ul>
</c:if>
</body>
</html>
