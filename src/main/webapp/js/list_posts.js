window.onload = function() {
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
                card.className = 'card text-bg-light mb-3';
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
                let options = {  month: '2-digit', day: '2-digit' };
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
                cardFooter.innerText = ``;
                cardFooter.style.display = 'flex';
                cardFooter.style.justifyContent = 'flex-end';

                // Create the button
                let like = document.createElement('button');
                like.type = 'button';
                like.className = 'btn btn-light';
                like.innerText = 'like';

                let comment = document.createElement('button');
                comment.type = 'button';
                comment.className = 'btn btn-light';
                comment.innerText = 'comment';
                comment.setAttribute('data-bs-toggle', 'modal');
                comment.setAttribute('data-bs-target', '#myModal');

                comment.addEventListener('click', function() {
                    // Get the modal
                    console.log('comment button clicked');
                    let modal = document.getElementById('myModal');

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
                });



                // Add the title and text to the card body

                cardBody.appendChild(cardText);

                // Add the header and body to the card
                card.appendChild(cardHeader);
                card.appendChild(cardBody);
                card.appendChild(cardDate);
                card.appendChild(cardFooter);
                cardFooter.appendChild(like);
                cardFooter.appendChild(comment);

                // Add the card to the container
                postsContainer.appendChild(card);
            });
        }
    };

    xhr.open("GET", url, true);
    xhr.send();

}