document.addEventListener("DOMContentLoaded", () => {
    loadTasks();

    const input = document.getElementById("taskInput");
    input.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            addTask();
        }
    });
});

function loadTasks() {
    fetch("tasks")
        .then(response => response.json())
        .then(tasks => {
            const list = document.getElementById("list");
            list.innerHTML = "";

            tasks.forEach((task, index) => {
                const li = document.createElement("li");

                const span = document.createElement("span");
                span.className = "task-text";
                span.textContent = task;

                const button = document.createElement("button");
                button.className = "delete-btn";
                button.textContent = "Delete";
                button.onclick = () => deleteTask(index);

                li.appendChild(span);
                li.appendChild(button);
                list.appendChild(li);
            });
        })
        .catch(error => console.error("Error loading tasks:", error));
}

function addTask() {
    const input = document.getElementById("taskInput");
    const value = input.value.trim();

    if (!value) {
        return;
    }

    fetch("tasks", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "task=" + encodeURIComponent(value)
    })
        .then(() => {
            input.value = "";
            loadTasks();
        })
        .catch(error => console.error("Error adding task:", error));
}

function deleteTask(index) {
    fetch("tasks?index=" + index, {
        method: "DELETE"
    })
        .then(() => loadTasks())
        .catch(error => console.error("Error deleting task:", error));
}
