<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://localhost:8080/cycleK-1.0.0/css/style.css">
    <link rel="stylesheet" href="http://localhost:8080/cycleK-1.0.0/css/social.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.0/font/bootstrap-icons.min.css"
          rel="stylesheet">
    <title>Social</title>
</head>
<body>
<%@ include file="../html/template/nav.html" %>
<body>
<div class="container">
    <br>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" style="color: white !important" id="home-tab" data-bs-toggle="tab"
                    data-bs-target="#body-post" type="button" role="tab" aria-controls="home-tab-pane"
                    aria-selected="true" onclick="loadContent('body-post')">Explore
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" style="color: white !important" id="profile-tab" data-bs-toggle="tab"
                    data-bs-target="#my-post" type="button" role="tab" aria-controls="profile-tab-pane"
                    aria-selected="false" onclick="loadContent('my-post')">My posts
            </button>
        </li>
    </ul>

    <div id="body-post" class="content">
        <div id="postsContainer-body-post"></div>
    </div>
    <div id="my-post" class="content">
        <div id="postsContainer-my-post"></div>
    </div>
</div>
<div class="modal fade" id="new-post" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this post?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" id="confirmDelete">Delete</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="commentModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Comments</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>
<div class="modal fade" id="likeModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Likes</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body"></div>
        </div>
    </div>
</div>
<button id="add-post" type="button" class="btn btn-light" data-url="post/create-post-form.jsp">
    <i class="fa-solid fa-plus"></i>
</button>

</body>

<%@ include file="../html/template/footer.html" %>

<!-- Our Scripts -->
<script src="http://localhost:8080/cycleK-1.0.0/js/utils.js"></script>
<script src="http://localhost:8080/cycleK-1.0.0/js/social/social.js"></script>
<!-- Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>

<!-- FontAwesome -->
<script src="https://kit.fontawesome.com/0c02fb7b66.js" crossorigin="anonymous"></script>
<!-- Jquery -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<!-- Popper -->
<script src="https://unpkg.com/@popperjs/core@2/dist/umd/popper.js"></script>

</body>
