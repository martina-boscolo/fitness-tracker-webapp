<%--
  Created by IntelliJ IDEA.
  User: Martina
  Date: 13/04/2024
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Post Creation Result</title>
</head>
<body>
<h1>Post Creation Result</h1>
<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<c:if test='${not empty post && !message.error}'>
    <ul>


        <li> postId :<c:out value="${post.postId       }"/></li>
        <li> userId :<c:out value="${post.userId       }"/></li>
        <li> textContent :<c:out value="${post.textContent  }"/></li>
        <li> imagePath :<c:out value="${post.imagePath    }"/></li>
        <li> likeCount :<c:out value="${post.likeCount    }"/></li>
        <li> commentCount :<c:out value="${post.commentCount }"/></li>
        <li> postDate :<c:out value="${post.postDate     }"/></li>


    </ul>
</c:if>

</body>
</html>
