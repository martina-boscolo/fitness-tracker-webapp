<!-- Author: Martina Boscolo Bacheto -->

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

<form method="POST" enctype="multipart/form-data" action="<c:url value="social.jsp"/>">

    <label for="userID">User Id:</label>
    <input id="userID" name="userId" type="text"/><br/>

    <label for="textContentID">Text Content:</label>
    <input id="textContentID" name="textContent" type="text"/><br/>

    <label for="photoID">Photo:</label>
    <input id="photoID" name="photo" type="file" accept="image/png, image/jpeg, .jpg, .jpeg, .png"/><br/><br/>

    <button type="submit">Submit</button>
    <br/>
    <button type="reset">Reset the form</button>

</form>
</body>
</html>
