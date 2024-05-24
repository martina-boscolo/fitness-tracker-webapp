window.onload = function () {
    const url = "http://localhost:8080/cycleK-1.0.0/rest/post"; // Replace with your actual endpoint
    console.log("Request URL: %s.", url);
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const posts = JSON.parse(xhr.responseText)["resource-list"];
            const postsContainer = document.getElementById('postsContainer');

            posts.forEach((postObj) => {
                let post = postObj.post;

                // Create a new div for the card
                let card = document.createElement('div');
                card.className = 'card border-light mb-3';
                card.style.maxWidth = '30rem'; // Increase the max-width to make the card larger
                card.style.margin = '0 auto'; // Add auto margins to center the card

                // Create the card header
                let cardHeader = document.createElement('div');
                cardHeader.className = 'card-header';
                cardHeader.innerText = `User: ${post.userId}`; // Replace with your actual header

                // Create the card body
                let cardBody = document.createElement('div');
                cardBody.className = 'card-body';

                let postDate = new Date(post.postDate);
                let options = {month: '2-digit', day: '2-digit'};
                let date = postDate.toLocaleDateString('it-IT', options);
                let hour = postDate.getHours();
                let minutes = postDate.getMinutes().toString().padStart(2, '0');
                // Create the card text
                let cardText = document.createElement('p');
                cardText.className = 'card-text';
                cardText.innerText = `${post.textContent}\n`; // Replace with your actual text

                // Create the card title
                let cardDate = document.createElement('div');
                cardDate.className = 'card-date';
                cardDate.innerText = `Published: ${date} at ${hour}:${minutes}`;
                cardDate.style.display = 'flex';
                cardDate.style.justifyContent = 'flex-end';
                cardDate.style.fontSize = '0.8rem';


                let cardFooter = document.createElement('div');
                cardFooter.className = 'card-footer';
                cardFooter.style.display = 'flex';
                cardFooter.style.justifyContent = 'space-between';


                let likeComment = document.createElement('div')

                let like = document.createElement('button');
                like.type = 'button';
                like.className = 'btn btn-link icon';


                let isLike = false;
                like.addEventListener('click', function () {
                    if (isLike === true) {
                        // If the button is already pressed, mark it as not pressed
                        heartIcon.classList.remove('pressed');
                        deleteLike(post.postId);
                        isLike = false;
                    } else {
                        // If the button is not pressed, mark it as pressed
                        addLike(post.postId);
                        heartIcon.classList.add('pressed');
                        isLike = true;

                    }
                    countLikes(post.postId, function (likesCount) {
                        countLike.innerText = ` ${likesCount} likes`;
                    });

                });

                //like.addEventListener('dblclick', function () {
                //    addLike(post.postId);
                //});

                let comment = document.createElement('button');
                comment.type = 'button';
                comment.className = 'btn btn-link icon';
                comment.setAttribute('data-bs-toggle', 'collapse');
                comment.setAttribute('data-bs-target', '#collapseExample');
                comment.setAttribute('aria-expanded', 'true');
                comment.setAttribute('aria-controls', 'collapseExample')
                comment.addEventListener('click', function () {
                    let commentSection = document.querySelector('commentSection');

                    listComments(post.postId);

                    // Check if the div is already visible
                    let collapseDiv = document.getElementById('collapseExample');
                    // Create the new elements
                    if (collapseDiv) {
                        // If the div is visible, hide it
                        collapseDiv.remove();
                    } else {
                        // If the div is not visible, show it
                        collapseDiv = document.createElement('div');
                        collapseDiv.className = 'collapse show';
                        collapseDiv.id = 'collapseExample';
                        collapseDiv.style = '';
                        let cardDiv = document.createElement('div');
                        cardDiv.className = 'card card-body';


                        // Append the new elements
                        collapseDiv.appendChild(cardDiv);
                        card.appendChild(collapseDiv);
                    }
                });

                let count = document.createElement('div')

                let countLike = document.createElement('button');
                countLike.type = 'button';
                countLike.className = 'btn btn-link';


                countLikes(post.postId, function (likesCount) {
                    countLike.innerText = ` ${likesCount} likes`;
                });


                let countComment = document.createElement('button');
                countComment.type = 'button';
                countComment.className = 'btn btn-link ';
                countComments(post.postId, function (commentsCount) {
                    countComment.innerText = ` ${commentsCount} comments`;
                });


                countComment.addEventListener('click', function () {
                    listComments(post.postId);
                });

                countLike.addEventListener('click', function () {
                    listLikes(post.postId);
                });


                let heartIcon = document.createElement('i');
                heartIcon.className = 'fa-solid  fa-heart';


                let commentIcon = document.createElement('i');
                commentIcon.className = 'fa-solid fa-comment';


                let plusIcon = document.createElement('i');
                plusIcon.className = 'fa-solid fa-plus';


                cardBody.appendChild(cardText);
                card.appendChild(cardHeader);
                card.appendChild(cardBody);
                card.appendChild(cardDate);
                card.appendChild(cardFooter);
                cardFooter.appendChild(likeComment);
                likeComment.appendChild(like);
                likeComment.appendChild(comment);
                cardFooter.appendChild(count);
                count.appendChild(countComment);
                count.appendChild(countLike);
                like.appendChild(heartIcon);
                comment.appendChild(commentIcon);
                postsContainer.appendChild(card);
            });
        }
    };

    xhr.open("GET", url, true);
    xhr.send();

    showDiv(event, 'body-post');

}


function listComments(postId) {
    console.log("schiacciaaa")

    // Define the modal
    let modal = document.getElementById('myModal');

    // Fetch the comments
    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/comment`; // Use the postId in the URL
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const comments = JSON.parse(xhr.responseText)["resource-list"];

            // Clear the modal body
            let modalBody = modal.querySelector('.modal-body');
            modalBody.innerHTML = '';

            // Create a new div for each comment and append it to the modal body
            comments.forEach((commentObj) => {
                let comment = commentObj.comment;

                let commentDiv = document.createElement('div');
                commentDiv.className = 'comment';
                commentDiv.innerText = `User ${comment.userId}: ${comment.commentText}`; // Display the user ID and comment text

                modalBody.appendChild(commentDiv);
            });
        }
    };
    xhr.open("GET", url, true);
    xhr.send();

    // Use Bootstrap's modal method to show the modal
    let bsModal = new bootstrap.Modal(modal);
    bsModal.show();

    modal.addEventListener('hidden.bs.modal', function () {
        // Manually remove the modal backdrop
        let backdrop = document.querySelector('.modal-backdrop');
        if (backdrop) {
            backdrop.remove();
        }
    });
}

function listLikes(postId) {
    console.log("listLikes")

    // Define the modal
    let modal = document.getElementById('likeModal');

    // Fetch the comments
    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/like`; // Use the postId in the URL
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const likes = JSON.parse(xhr.responseText)["resource-list"];

            // Clear the modal body
            let modalBody = modal.querySelector('.modal-body');
            modalBody.innerHTML = '';

            // Create a new div for each comment and append it to the modal body
            likes.forEach((likeObj) => {
                let like = likeObj.like;

                let likeDiv = document.createElement('div');
                likeDiv.className = 'Likes';
                likeDiv.innerText = `User: ${like.userId}`; // Display the user ID and comment text

                modalBody.appendChild(likeDiv);
            });
        }
    };
    xhr.open("GET", url, true);
    xhr.send();

    // Use Bootstrap's modal method to show the modal
    let bsModal = new bootstrap.Modal(modal);
    bsModal.show();

    modal.addEventListener('hidden.bs.modal', function () {
        // Manually remove the modal backdrop
        let backdrop = document.querySelector('.modal-backdrop');
        if (backdrop) {
            backdrop.remove();
        }
    });
}

function countLikes(postId, callback) {
    console.log("countLikes")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/like/count`; // Use the postId in the URL
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const likesCount = JSON.parse(xhr.responseText);

            callback(likesCount);
        }
    };
    xhr.open("GET", url, true);
    xhr.send();
}

function countComments(postId, callback) {
    console.log("countComments")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/comment/count`;
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const commentsCount = JSON.parse(xhr.responseText);

            callback(commentsCount);
        }
    };
    xhr.open("GET", url, true);
    xhr.send();
}

function addLike(postId) {
    console.log("addLike")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/like`;
    const xhr = new XMLHttpRequest();

    const body = {
        "like": {
            "userId": 1,
            "postId": postId
        }
    };

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 201) {
            const commentsCount = JSON.parse(xhr.responseText);
            const response = JSON.parse(xhr.responseText);
            console.log(response);


        }
    };
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(body));
}

function addComments(postId) {
    console.log("addComments")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/comment`;
    const xhr = new XMLHttpRequest();

    const body = {
        "comment": {
            "postId": postId,
            "userId": 1,
            "commentText": "new comment"
        }
    };

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 201) {
            const commentsCount = JSON.parse(xhr.responseText);
            const response = JSON.parse(xhr.responseText);
            console.log(response);


        }
    };
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(body));
}

function deleteLike(postId) {
    console.log("deleteLike")


}

function showDiv(event, id) {
    // Prevent the default action of the link
    event.preventDefault();

    // Hide all divs
    document.getElementById('body-post').style.display = 'none';
    document.getElementById('my-post').style.display = 'none';

    // Show the specific div
    document.getElementById(id).style.display = 'block';
}