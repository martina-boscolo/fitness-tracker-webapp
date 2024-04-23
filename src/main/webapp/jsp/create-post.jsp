<%--
  Created by IntelliJ IDEA.
  User: Martina
  Date: 13/04/2024
  Time: 19:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create Post Form</title>
</head>

<body>
<h1>Create Post Form</h1>

<form method="post" action="<c:url value="/create-post"/>">


    <label for="userID">User ID:</label>
    <input id="userID" name="userId" type="number"/><br/>

    <label for="textContentID">Text Content:</label>
    <textarea id="textContentID" name="textContent"></textarea><br/>

    <label for="imagePathID">Image Path:</label>
    <input id="imagePathID" name="imagePath" type="text"/><br/>

    <label for="likeCountID">Like Count:</label>
    <input id="likeCountID" name="likeCount" type="number"/><br/>


    <label for="commentCountID">Comment Count:</label>
    <input id="commentCountID" name="commentCount" type="number"/><br/>

    <label for="postDateID">Post Date:</label>
    <input id="postDateID" name="postDate" type="date"/><br/><br/>

    <button type="submit">Submit</button><br/>
    <button type="reset">Reset the form</button>

</form>
</body>
</html>
