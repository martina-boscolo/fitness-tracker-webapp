document.addEventListener('DOMContentLoaded', function () {
    if (!checkAuth()) {
        logOut()
    }
});

async function fetchAllPlans() {
    const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/idUser');
    const exercise_plan = await response.json();
    let data = {}
    exercise_plan['resource-list'].forEach(ep => {
        ep = ep.exercise_plan
        let plans = {}
        let ep_plan = ep?.plan?.plan;
        let plan_keys = Object.keys(ep_plan)
        plan_keys.forEach(pk => {
            plans[pk] = ep_plan[pk]
        })
        data[ep.planName] = {
            "id": ep.id,
            "days": plans
        }
    });
    return data;
}

async function fetchExercises() {
    const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercises')
    const exercises = await response.json()
    console.log(exercises['resource-list'].map(rl => rl.exercise));
    return exercises['resource-list'].map(rl => rl.exercise);

}

async function deletePlan(plan_id) {
    await fetch(`http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/${plan_id}`, {
        method: 'DELETE',
    });
}

async function updatePlan(plan_name, plan) {
    const updatedExercisePlan = {
        id: plan.id,
        idUser: -1,
        planName: plan_name,
        plan: {"plan": plan.days}
    };

    try {
        const response = await fetch(`http://localhost:8080/cycleK-1.0.0/rest/exercise_plan/`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedExercisePlan)
        });

        if (!response.ok) {
            console.log("error occurred")
        }
    } catch (error) {
        console.error('Error updating exercise plan:', error);
    }
}

async function addPlan(plan_name) {
    const newExercisePlan = {
        planName: plan_name,
        plan: {"plan": {}}
    };

    try {
        const response = await fetch('http://localhost:8080/cycleK-1.0.0/rest/exercise_plan', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newExercisePlan)
        });

        if (!response.ok) {
            console.log('the error occurred now...')
        }

        const responseData = await response.json();
        console.log('New exercise plan added:', responseData);
        return responseData.exercise_plan
    } catch (error) {
        console.error('Error updating exercise plan:', error);
    }
}

const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

let currentPlanIndex = 0;

document.addEventListener("DOMContentLoaded", async function () {
    let plans = await fetchAllPlans();
    let planKeys = Object.keys(plans);
    let exercises = await fetchExercises();

    const planContainer = document.getElementById("planContainer");
    const modal = $('#modal');
    const planModal = $('#planModal');
    const exerciseForm = document.getElementById("exerciseForm");
    const planForm = document.getElementById("planForm");
    const daySelect = document.getElementById("daySelect");
    const exerciseSelect = document.getElementById("exerciseSelect");
    const prevPlanBtn = document.getElementById("prevPlanBtn");
    const nextPlanBtn = document.getElementById("nextPlanBtn");
    const addExerciseBtn = document.getElementById("addExerciseBtn");
    const addPlanBtn = document.getElementById("addPlanBtn");
    const deletePlanBtn = document.getElementById("deletePlanBtn");
    const planNameElement = document.getElementById('planName');
    // fill the exercise select dropdown
    exerciseSelect.innerHTML = exercises.map(ex => `<option value="${ex.id}">${ex.exercise_name}</option>`).join('');





    // function showPlan(index) {
    //     planContainer.innerHTML = "";
    //     const planKey = planKeys[index];
    //     const plan = plans[planKey];
    //     const planElement = document.createElement("div");
    //     planElement.className = "plan active";
    //     planElement.innerHTML = `<h2>${planKey}</h2>`;
    //     Object.keys(plan.days).forEach(day => {
    //         const dayElement = document.createElement("div");
    //         dayElement.className = "mt-4";
    //         dayElement.innerHTML = `<h3>${day}</h3>`;
    //         plan.days[day].forEach(ex => {
    //             const exerciseElement = document.createElement("div");
    //             exerciseElement.className = "mb-2";
    //             exerciseElement.innerHTML = `
    //                 <p>Exercise ID: ${ex.idExercise}, Reps: ${ex.reps}, Sets: ${ex.sets}, Weight: ${ex.weight}</p>
    //                 <button class="btn btn-warning btn-sm editBtn" data-plan="${planKey}" data-day="${day}" data-id="${ex.idExercise}">Edit</button>
    //             `;
    //             dayElement.appendChild(exerciseElement);
    //         });
    //         planElement.appendChild(dayElement);
    //     });
    //     planContainer.appendChild(planElement);
    // }



    function showPlan(index) {
        planContainer.innerHTML = "";
        const planKey = planKeys[index];
        const plan = plans[planKey];

        // Display the plan name in the center
        planNameElement.textContent = planKey;

        Object.keys(plan.days).forEach(day => {
            const dayElement = document.createElement("div");
            dayElement.className = "card";
            dayElement.innerHTML = `<h2>${day}</h2>`;

            plan.days[day].forEach(ex => {
                const exerciseElement = document.createElement("div");
                const exerciseName = exercises.filter((ex1) => ex1.id == ex.idExercise)[0].exercise_name
                exerciseElement.className = "exercise";
                exerciseElement.innerHTML = `
                <div>
                    <p><strong>Exercise:</strong> ${exerciseName}</p>
                    <p><strong>Repetition:</strong> ${ex.reps}</p>
                    <p><strong>Sets:</strong> ${ex.sets}</p>
                    <p><strong>Weight:</strong> ${ex.weight}</p>
                </div>
                <button class="btn btn-warning btn-sm editBtn" data-plan="${planKey}" data-day="${day}" data-id="${ex.idExercise}">
                    <i class="fas fa-edit" style="pointer-events:none"></i>
                </button>
            `;
                dayElement.appendChild(exerciseElement);
            });

            planContainer.appendChild(dayElement);
        });
    }





    function populateForm(planKey, day, idExercise, reps, sets, weight) {
        daySelect.value = day;
        exerciseSelect.value = idExercise;
        document.getElementById("reps").value = reps;
        document.getElementById("sets").value = sets;
        document.getElementById("weight").value = weight;
    }

    document.body.addEventListener('click', function (event) {
        if (event.target.classList.contains('editBtn')) {
            const planKey = event.target.getAttribute('data-plan');
            const day = event.target.getAttribute('data-day');
            const idExercise = event.target.getAttribute('data-id');
            const exercise = plans[planKey].days[day].find(ex => ex.idExercise == idExercise);
            populateForm(planKey, day, idExercise, exercise.reps, exercise.sets, exercise.weight);
            modal.modal('show');
        }
    });

    prevPlanBtn.addEventListener('click', function () {
        currentPlanIndex = (currentPlanIndex - 1 + planKeys.length) % planKeys.length;
        showPlan(currentPlanIndex);
    });

    nextPlanBtn.addEventListener('click', function () {
        currentPlanIndex = (currentPlanIndex + 1) % planKeys.length;
        showPlan(currentPlanIndex);
    });

    addExerciseBtn.addEventListener('click', function () {
        const currentPlan = planKeys[currentPlanIndex];
        daySelect.value = '';
        exerciseSelect.value = '';
        document.getElementById("reps").value = '';
        document.getElementById("sets").value = '';
        document.getElementById("weight").value = '';
        modal.modal('show');
    });

    addPlanBtn.addEventListener('click', function () {
        document.getElementById("newPlanName").value = '';
        planModal.modal('show');
    });

    deletePlanBtn.addEventListener('click', async function () {
        if (confirm("Are you sure you want to delete this plan?")) {
            await deletePlan(plans[planKeys[currentPlanIndex]].id);
            plans = await fetchAllPlans();
            planKeys = Object.keys(plans);
            currentPlanIndex = 0;
            showPlan(currentPlanIndex);
        }
    });

    exerciseForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const day = daySelect.value;
        const idExercise = exerciseSelect.value;
        const reps = document.getElementById("reps").value;
        const sets = document.getElementById("sets").value;
        const weight = document.getElementById("weight").value;

        const currentPlanKey = planKeys[currentPlanIndex];
        if (!plans[currentPlanKey].days[day]) {
            plans[currentPlanKey].days[day] = [];
        }
        const existingExercise = plans[currentPlanKey].days[day].find(ex => ex.idExercise == idExercise);
        if (existingExercise) {
            existingExercise.reps = reps;
            existingExercise.sets = sets;
            existingExercise.weight = weight;
        } else {
            plans[currentPlanKey].days[day].push({idExercise, reps, sets, weight});
        }

        await updatePlan(currentPlanKey, plans[currentPlanKey])
        showPlan(currentPlanIndex);
        modal.modal('hide');
    });

    planForm.addEventListener('submit', async function (event) {
        event.preventDefault();
        const newPlanName = document.getElementById("newPlanName").value;
        const plan = await addPlan(newPlanName);
        plans[newPlanName] = {"id": plan.id, "days": {}};
        planKeys.push(newPlanName);
        currentPlanIndex = planKeys.length - 1;
        showPlan(currentPlanIndex);
        planModal.modal('hide');
    });

    days.forEach(day => {
        const option = document.createElement("option");
        option.value = day;
        option.textContent = day;
        daySelect.appendChild(option);
    });

    showPlan(currentPlanIndex);
});




