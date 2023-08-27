var inputs = document.querySelectorAll(".bg-input input")
var labels = document.querySelectorAll(".bg-input label")
var buttons = document.querySelectorAll("#alerta button")

for (cont=0;cont<buttons.length;cont++) {
    buttons[cont].addEventListener("click", () => {
        document.getElementById("alerta").style.display = "none"
    });
}

for (cont=0;cont<inputs.length;cont++) {
    let input = inputs[cont];
    let label = labels[cont];

    input.addEventListener("focus", () => {
        label.style.transform = "translateY(-25px)"
    });

    input.addEventListener('blur', (event) => {
        if (input.value.trim() == "") {
            label.style.transform = "translateY(0px)"
        }
    });
}