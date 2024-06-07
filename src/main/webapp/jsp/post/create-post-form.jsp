<!-- Author: Martina Boscolo Bacheto -->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create Post Form</title>
    <link rel="stylesheet" href="http://localhost:8080/cycleK-1.0.0/css/social.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>


    <body>
    <div class="modal-header">
        <h5 class="modal-title" id="ModalLabel">Create a new Post</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
    </div>

<form method="POST" enctype="multipart/form-data" action="<c:url value="/create-post"/>">

    <div class="modal-body">


        <textarea id="textContentID" name="textContent" class="form-control" placeholder="Write your post..."  aria-label="With textarea"></textarea>
        <br/>

          <label for="photoID">If you want, add a photo to your post!</label>
    <br/>
          <input id="photoID" name="photo" type="file" accept="image/png, image/jpeg, .jpg, .jpeg, .png"/><br/><br/>
    </div>
    <div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
    <button type="submit" class="btn btn-primary">Submit</button>
    </div>
          </form>


          <!-- Our Scripts -->
          <script src="http://localhost:8080/cycleK-1.0.0/js/utils.js"></script>
          <script src="http://localhost:8080/cycleK-1.0.0/js/social/social.js"></script>
          <!-- Bootstrap -->
          <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                  integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                  crossorigin="anonymous"></script>

          <!-- FontAwesome -->
    </body>
</html>
