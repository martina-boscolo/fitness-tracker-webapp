let currentUserId = null;

function getUserId() {
    fetch("http://localhost:8080/cycleK-1.0.0/rest/user/id", {
        credentials: 'include'
    }) //modify with token
        .then(response => response.json())
        .then(data => {
            const user = data.user;
            console.log("user id", user.id);
            currentUserId = user.id;
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

document.addEventListener('DOMContentLoaded', function () {

    // Check if authToken cookie is present
    let Cookies = document.cookie;
    if (checkAuth()) {
        getUserId()
        loadContent('body-post');
    } else {
        // Redirect to login page
        window.location.href = LOGIN;
    }


});


function clearAllComponents() {
    // Get all the postsContainer elements
    const postsContainers = document.querySelectorAll('[id^="postsContainer-"]');

    // Iterate over each postsContainer and clear its content
    postsContainers.forEach((postsContainer) => {
        postsContainer.innerHTML = '';
    });
}

/*window.onload = function () {
    loadContent('body-post'); // or 'my-post'
};*/

function loadContent(currentVisualization) {
    console.log(currentVisualization)

    let url;
    if (currentVisualization === 'my-post') {
        url = `http://localhost:8080/cycleK-1.0.0/rest/post/user/${currentUserId}`;
    } else if (currentVisualization === 'body-post') {
        url = "http://localhost:8080/cycleK-1.0.0/rest/post";
    }

    console.log("Request URL: %s.", url);
    const xhr = new XMLHttpRequest();
    const postsContainer = document.getElementById('postsContainer-' + currentVisualization);
    clearAllComponents();
    //postsContainer.remove();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const posts = JSON.parse(xhr.responseText)["resource-list"];


            posts.forEach((postObj) => {
                let post = postObj.post;

                // Create a new div for the card
                let card = document.createElement('div');
                card.className = 'card border-light mb-3 postCard';
                card.style.maxWidth = '30rem'; // Increase the max-width to make the card larger
                card.style.margin = '0 auto'; // Add auto margins to center the card

                // Create the card header
                let cardHeader = document.createElement('div');
                cardHeader.className = 'card-header';
                let userName = document.createElement('div');
                userName.className = 'user-header';
                userName.innerText = `${post.username}`; // Replace with your actual header

                let base64String = post.photo;// Replace with your actual Base64 string
                let imageFormat = post.photoMediaType; // Replace with your actual image format

                if (base64String !== null) {
// Create a new image element
                    let img = document.createElement('img');

// Set the src attribute of the img element to the Base64 string
                    img.src = `data:image/${imageFormat};base64,${base64String}`;

// Append the img element to the body (or any other container element)
                    card.appendChild(img);
                }


                cardHeader.appendChild(userName);
                if (post.userId === currentUserId) {
                    let deleteCard = document.createElement('button');
                    deleteCard.type = 'button';
                    deleteCard.className = 'btn btn-link icon bin';

                    let bin = document.createElement('i');
                    bin.className = 'fa-solid fa-trash';

                    deleteCard.addEventListener('click', function () {
                        // Show the modal
                        let deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
                        deleteModal.show();

                        // Add a click event listener to the confirm delete button
                        document.getElementById('confirmDelete').addEventListener('click', function () {
                            // Delete the post
                            deletePost(post.postId, card);

                            // Remove the card from the DOM
                            card.remove();

                            // Hide the modal
                            deleteModal.hide();
                        });
                    });

                    cardHeader.appendChild(deleteCard);
                    deleteCard.appendChild(bin);
                }


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
                wasLiked(post.postId, function (liked) {

                    isLike = liked;

                });


                like.addEventListener('click', function () {
                    let promise;

                    if (isLike === true) {
                        // If the button is already pressed, mark it as not pressed
                        heartIcon.classList.remove('pressed');
                        promise = new Promise((resolve, reject) => {
                            deleteLike(currentUserId, post.postId, resolve, reject);
                        });
                        isLike = false;
                    } else {
                        // If the button is not pressed, mark it as pressed
                        promise = new Promise((resolve, reject) => {
                            addLike(post.postId, resolve, reject);
                        });
                        heartIcon.classList.add('pressed');
                        isLike = true;
                    }

                    promise.then(() => {
                        countLikes(post.postId, function (likesCount) {
                            console.log("contatooooo", likesCount)
                            countLike.innerText = ` ${likesCount} likes`;
                        });
                    });
                });

                //like.addEventListener('dblclick', function () {
                //    addLike(post.postId);
                //});

                let comment = document.createElement('button');
                comment.type = 'button';
                comment.className = 'btn btn-link icon';


                let collapseId = 'collapseComments' + post.postId;

                comment.setAttribute('data-bs-toggle', 'collapse');
                comment.setAttribute('data-bs-target', '#' + collapseId);
                comment.setAttribute('aria-expanded', 'true');
                comment.setAttribute('aria-controls', collapseId)
                comment.addEventListener('click', function () {
                    let collapseDiv = document.getElementById(collapseId);
                    // Create the new elements
                    if (collapseDiv) {
                        // If the div is visible, hide it
                        collapseDiv.remove();
                    } else {
                        // If the div is not visible, show it
                        collapseDiv = document.createElement('div');
                        collapseDiv.className = 'collapse show commentCollapse';
                        collapseDiv.id = collapseId;

                        card.appendChild(collapseDiv);
                    }

                    //addComment(post.postId, totalComments, collapseDiv)
                    // Create a new input for the comment
                    let commentDiv = document.createElement('div');
                    commentDiv.className = 'new-comment';

                    let commentInput = document.createElement('div');
                    commentInput.className = 'input-group mb-3';

                    let commentText = document.createElement('input');
                    commentText.type = 'text';
                    commentText.className = 'form-control';
                    commentText.placeholder = 'Add a new comment...';


                    let sendButton = document.createElement('button');
                    sendButton.type = 'button';
                    sendButton.className = 'btn btn-outline-secondary';
                    sendButton.innerText = 'Send';
                    sendButton.disabled = true;

                    commentText.addEventListener('input', function () {
                        sendButton.disabled = commentText.value.trim() === '';
                    });

                    // Trigger the input event once to handle the initial state
                    commentText.dispatchEvent(new Event('input'));


                    // Add a click event listener to the button
                    sendButton.addEventListener('click', function () {
                        // Call the function to add the comment
                        addCommentToPost(post.postId, post.commentCount, commentText.value)
                            .then(() => {
                                // After the comment is successfully added, update the count of comments
                                countComments(post.postId, function (commentsCount) {
                                    console.log(`Updated comments count: ${commentsCount}`);
                                    countComment.innerText = ` ${commentsCount} comments`;
                                });
                                // Clear the input
                                commentText.value = '';
                                // Disable the send button
                                sendButton.disabled = true;
                            })
                            .catch(error => {
                                console.error(`Failed to add comment: ${error}`);
                            });
                    });

                    collapseDiv.appendChild(commentDiv);
                    commentDiv.appendChild(commentInput);
                    commentInput.appendChild(commentText);
                    commentInput.appendChild(sendButton);

                });

                let count = document.createElement('div')

                let countLike = document.createElement('button');
                countLike.type = 'button';
                countLike.className = 'btn btn-link';
                countLike.innerText = ` ${post.likeCount} likes`;

                /*countLikes(post.postId, function (likesCount) {
                    countLike.innerText = ` ${likesCount} likes`;
                });*/
                countLike.addEventListener('click', function () {
                    listLikes(post.postId);
                });


                let countComment = document.createElement('button');
                countComment.type = 'button';
                countComment.className = 'btn btn-link ';
                countComment.innerText = ` ${post.commentCount} comments`;

                /* countComments(post.postId, function (commentsCount) {
                     countComment.innerText = ` ${commentsCount} comments`;
                 });*/


                countComment.addEventListener('click', function () {
                    listComments(post.postId);
                });


                let heartIcon = document.createElement('i');
                heartIcon.className = 'fa-solid  fa-heart heartIcon';
                wasLiked(post.postId, function (isLiked) {
                    if (isLiked) {
                        heartIcon.classList.add('pressed');
                    } else {
                        heartIcon.classList.remove('pressed');
                    }
                });


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
                count.appendChild(countLike);
                count.appendChild(countComment);
                like.appendChild(heartIcon);
                comment.appendChild(commentIcon);
                postsContainer.appendChild(card);
            });
        } else if (xhr.status === 401) {
            // Redirect to login page
            window.location.href = LOGIN;
        }
    };

    xhr.open("GET", url, true);
    xhr.send();

    showDiv(event, currentVisualization);

}

function wasLiked(postId, callback) {
    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/like`;
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const likes = JSON.parse(xhr.responseText)["resource-list"];
                let found = false;
                likes.forEach((likeObj) => {
                    let like = likeObj.like;
                    if (like.userId === currentUserId) {
                        console.log("Like from current user already exists.");
                        found = true;
                    }
                });
                callback(found);
            } else if (xhr.status === 401) {
                // Redirect to login page
                window.location.href = LOGIN;
            }
        }
    };
    xhr.open("GET", url, true);
    xhr.send();
}

function addComment(postId, totalComments, container) {
    // Create a new input for the comment
    let commentDiv = document.createElement('div');
    commentDiv.className = 'new-comment';

    let commentInput = document.createElement('div');
    commentInput.className = 'input-group mb-3';

    let commentText = document.createElement('input');
    commentText.type = 'text';
    commentText.className = 'form-control';
    commentText.placeholder = 'Add a new comment...';


    let sendButton = document.createElement('button');
    sendButton.type = 'button';
    sendButton.className = 'btn btn-outline-secondary';
    sendButton.innerText = 'Send';
    sendButton.disabled = true;

    commentText.addEventListener('input', function () {
        sendButton.disabled = commentText.value.trim() === '';
    });

    // Trigger the input event once to handle the initial state
    commentText.dispatchEvent(new Event('input'));


    // Add a click event listener to the button
    sendButton.addEventListener('click', function () {
        // Call the function to add the

        addCommentToPost(postId, post.commentCount, commentText.value);
        sendButton.disabled = true;
        // Clear the input
        commentText.value = '';

    });

    container.appendChild(commentDiv);
    commentDiv.appendChild(commentInput);
    commentInput.appendChild(commentText);
    commentInput.appendChild(sendButton);

}

function addCommentToPost(postId, totalComments, textComment) {
    return new Promise((resolve, reject) => {
        console.log("addComment")

        const url = `http://localhost:8080/cycleK-1.0.0/rest/post/comment`;
        const xhr = new XMLHttpRequest();

        console.log("text comment" + textComment);
        const body = {
            "comment": {
                "userId": currentUserId,
                "postId": postId,
                "commentText": textComment.valueOf()
            }
        };

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 201) {
                    resolve();
                } else if (xhr.status === 401) {
                    // Redirect to login page
                    window.location.href = LOGIN;
                } else {
                    reject(new Error(`HTTP request failed with status ${xhr.status}`));
                }
            }
        };
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify(body));
    });
}

function listComments(postId, container) {
    console.log("listComments")

    let modal = document.getElementById('commentModal');

    // Fetch the comments
    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}/comment`; // Use the postId in the URL
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const comments = JSON.parse(xhr.responseText)["resource-list"];

            let modalBody = modal.querySelector('.modal-body');
            modalBody.innerHTML = '';
            // Clear the container

            // Create a new div for each comment and append it to the container
            comments.forEach((commentObj) => {
                let comment = commentObj.comment;

                let commentDiv = document.createElement('div');
                commentDiv.className = 'commentText';
                commentDiv.innerText = `User ${comment.userId}:  ${comment.commentText}`;


                if (comment.userId === currentUserId) {
                    let deleteCommentButton = document.createElement('button');
                    deleteCommentButton.type = 'button';
                    deleteCommentButton.className = 'btn btn-link icon bin';

                    let binComm = document.createElement('i');
                    binComm.className = 'fa-solid fa-trash';

                    deleteCommentButton.addEventListener('click', function () {
                        deleteComment(comment.commentId, commentDiv);


                    });

                    commentDiv.appendChild(deleteCommentButton);
                    deleteCommentButton.appendChild(binComm);


                }


                modalBody.appendChild(commentDiv);

            });
        } else if (xhr.status === 401) {
            // Redirect to login page
            window.location.href = LOGIN;
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
        } else if (xhr.status === 401) {
            // Redirect to login page
            window.location.href = LOGIN;
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
        } else if (xhr.status === 401) {
            // Redirect to login page
            window.location.href = LOGIN;
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
        } else if (xhr.status === 401) {
            // Redirect to login page
            window.location.href = LOGIN;
        }
    };
    xhr.open("GET", url, true);
    xhr.send();
}

function addLike(postId, resolve, reject) {
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
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 201) {
                resolve();
            } else if (xhr.status === 401) {
                // Redirect to login page
                window.location.href = LOGIN;
            } else {
                reject(new Error(`HTTP request failed with status ${xhr.status}`));
            }
        }
    };
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(body));
}

function deleteLike(userId, postId, resolve, reject) {
    console.log("deleteLike")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/like/${userId}/${postId}`;
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {

                resolve();
            } else if (xhr.status === 401) {
                // Redirect to login page
                window.location.href = LOGIN;
            } else {
                reject(new Error(`HTTP request failed with status ${xhr.status}`));
            }
        }
    };
    xhr.open("DELETE", url, true);
    xhr.send();
}

function deletePost(postId) {
    console.log("deletePost")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/${postId}`;
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                resolve();
            } else if (xhr.status === 401) {
                // Redirect to login page
                window.location.href = LOGIN;
            } else {
                reject(new Error(`HTTP request failed with status ${xhr.status}`));
            }
        }
    };
    xhr.open("DELETE", url, true);
    xhr.send();
}

function deleteComment(commentId, commentDiv) {
    console.log("deleteComment")

    const url = `http://localhost:8080/cycleK-1.0.0/rest/post/comment/${commentId}`;
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Remove the commentDiv from the DOM
                commentDiv.remove();
            } else if (xhr.status === 401) {
                // Redirect to login page
                window.location.href = LOGIN;
            } else {
                console.error(`HTTP request failed with status ${xhr.status}`);
            }
        }
    };
    xhr.open("DELETE", url, true);
    xhr.send();
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

document.getElementById('add-post').addEventListener('click', function () {
    var url = this.getAttribute('data-url');

    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.querySelector('#new-post .modal-body').innerHTML = this.responseText;
            var modal = new bootstrap.Modal(document.getElementById('new-post'));
            modal.show();
        }
    };
    xhr.send();
});