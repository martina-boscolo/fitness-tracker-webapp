// Creazione di un nuovo oggetto XMLHttpRequest
const xhr = new XMLHttpRequest();

// Impostazione del metodo HTTP e dell'URL di destinazione per la richiesta
xhr.open('GET', 'http://localhost:8080/cycleK-1.0.0/rest/diet', true);

// Impostazione dell'intestazione Content-Type della richiesta
xhr.setRequestHeader('Content-Type', 'application/json');

// Impostazione della funzione di callback per la gestione della risposta
xhr.onload = function () {
    // Verifica lo stato della richiesta
    if (xhr.status >= 200 && xhr.status < 300) {
        // Parsing della risposta JSON e accesso alla "resource-list"
        const data = JSON.parse(xhr.responseText)["resource-list"];

        // Verifica se data non è undefined o null
        console.log(data);
        if (data) {
            // Una volta ricevuti i dati JSON, costruisci la rappresentazione della dieta
            const dataContainer = document.getElementById('dataContainer');
            dataContainer.innerHTML = ''; // Pulisci il contenuto precedente

            // Itera su ciascun oggetto dieta nella resource list
            data.forEach(resource => {
                const diet = resource.diet; // Accesso al piano dietetico all'interno di ciascun elemento
                const dietDate = resource.dietDate; // Accesso alla data della dieta

                // Creare un oggetto per contenere i pasti di ogni giorno
                const mealsByDay = {};

                // Itera sui giorni della settimana nel piano dietetico
                Object.keys(diet).forEach(day => {
                    // Inizializza l'array per i pasti di questo giorno
                    mealsByDay[day] = [];

                    // Itera sui pasti del giorno e aggiungi ogni pasto all'array dei pasti
                    Object.keys(diet[day]).forEach(meal => {
                        const foodItems = diet[day][meal];
                        mealsByDay[day].push({ meal: meal, foods: foodItems });
                    });
                });

                // Ora che hai separato i pasti per giorno, puoi crearne l'HTML
                Object.keys(mealsByDay).forEach(day => {
                    const dayElement = document.createElement('div');
                    dayElement.classList.add('day'); // Aggiungi una classe CSS per la formattazione
                    dayElement.innerHTML = `<h2>${day}</h2>`;

                    // Itera sui pasti di questo giorno e crea la lista degli alimenti
                    mealsByDay[day].forEach(mealObj => {
                        const mealElement = document.createElement('ul');
                        mealElement.innerHTML = `<h3>${mealObj.meal}</h3>`;

                        // Itera sugli alimenti del pasto
                        Object.entries(mealObj.foods).forEach((food, quantity) => {
                            const foodItemElement = document.createElement('li');
                            foodItemElement.textContent = `${food}: ${quantity}`;
                            mealElement.appendChild(foodItemElement);
                        });

                        dayElement.appendChild(mealElement);
                    });

                    dataContainer.appendChild(dayElement);
                });
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
