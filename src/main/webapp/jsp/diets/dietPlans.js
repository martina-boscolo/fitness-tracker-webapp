function fetchDataFromBackend() {
    // Crea una nuova istanza dell'oggetto XMLHttpRequest
    const xhr = new XMLHttpRequest();

    // Imposta l'URL della richiesta
    const url = 'http://localhost:8080/cycleK-1.0.0/rest/diet';

    // Configura la richiesta
    xhr.open('GET', url, true);

    // Imposta il gestore dell'evento per la risposta
    xhr.onload = function () {
        // Verifica lo stato della richiesta
        if (xhr.status >= 200 && xhr.status < 300) {
            // Parsing della risposta JSON e accesso alla "resource-list"
            const data = JSON.parse(xhr.responseText)["resource-list"];

            // Verifica se data non è undefined o null
            if (data) {
                // Una volta ricevuti i dati JSON, costruisci la rappresentazione della dieta
                const dataContainer = document.getElementById('dataContainer');
                dataContainer.innerHTML = ''; // Pulisci il contenuto precedente

                // Itera su ciascun oggetto dieta nella resource list
                data.forEach(diet => {
                    const day = Object.keys(diet)[0]; // Ottieni il nome del giorno
                    const meals = diet[day]; // Ottieni i pasti per il giorno

                    // Crea un elemento div per il giorno
                    const dayElement = document.createElement('div');
                    dayElement.classList.add('day'); // Aggiungi una classe CSS per la formattazione
                    dayElement.innerHTML = `<h2>${day}</h2>`;

                    // Itera su ciascun pasto del giorno
                    Object.keys(meals).forEach(meal => {
                        const mealElement = document.createElement('ul');
                        mealElement.innerHTML = `<h3>${meal}</h3>`;

                        // Itera su ciascun alimento nel pasto
                        Object.keys(meals[meal]).forEach(food => {
                            const foodItemElement = document.createElement('li');
                            foodItemElement.textContent = `${food}: ${meals[meal][food]}`;
                            mealElement.appendChild(foodItemElement);
                        });

                        dayElement.appendChild(mealElement);
                    });

                    dataContainer.appendChild(dayElement);
                });
            } else {
                console.error('I dati non sono nel formato atteso: "resource-list" non è definito.');
            }
        } else {
            console.error('Errore nella richiesta:', xhr.statusText);
        }
    };

    // Gestione degli errori di rete
    xhr.onerror = function () {
        console.error('Errore di rete durante la richiesta.');
    };

    // Invia la richiesta
    xhr.send();
}