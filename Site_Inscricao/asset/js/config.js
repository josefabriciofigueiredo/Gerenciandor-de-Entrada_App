import { initializeApp } from "https://www.gstatic.com/firebasejs/9.20.0/firebase-app.js";
import { getFirestore, addDoc, collection, onSnapshot, query, serverTimestamp } from "https://www.gstatic.com/firebasejs/9.20.0/firebase-firestore.js";

// Dados do Firebase Deletado
const firebaseConfig = {
  dados: null
};

// Initialize Firebase  
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

// ----- Adicionar -----
function inscrever() {
  const nome = document.getElementById("nome")
  const idade = document.getElementById("idade")
  const escola = document.getElementById("escola")
  const telefone = document.getElementById("telefone")
  const email = document.getElementById("email")
  const divAlerta = document.getElementById("msgAlerta")

  const valNome = nome.value
  const valIdade = idade.value
  const valEscola = escola.value.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase().trim();
  const valTelefone = telefone.value
  const valEmail = email.value

  var formErro = false;
  if (valNome.length == 0) {
    divAlerta.innerText = "Nome Obrigatório!"
    formErro = true
  } else if (valIdade.length == 0) {
    divAlerta.innerText = "Idade Obrigatório!"
    formErro = true
  } else if (isNaN(valIdade)) {
    divAlerta.innerText = "Idade Inválida!"
    formErro = true
  } else if (valIdade > 100) {
    divAlerta.innerText = "Idade Inválida!"
    formErro = true
  }
  
  if (formErro) {
    document.getElementById("alerta").style.display = "flex"
  } else {
    var inscrever = document.getElementById("inscrever")
    var inscrevendo = document.getElementById("inscrevendo")
    
    inscrever.style.display = "none"
    inscrevendo.style.display = "block"

    addDoc(collection(db, "Inscricao"), {
      nome: valNome,
      idade: valIdade,
      escola: valEscola,
      telefone: valTelefone,
      email: valEmail,
      timestamp: serverTimestamp()
    }).then(() => {
      console.log("Adicionado com sucesso");

      nome.value = ""
      idade.value = ""
      escola.value = ""
      telefone.value = ""
      email.value = ""
    }).catch((error) => { 
      console.error("Erro ao adicionar: ", error);
    }).finally(() => {
      inscrever.style.display = "block"
      inscrevendo.style.display = "none"

      window.location.href = "asset/template/fim.html"
    });
  }
}

// Adicionar evento no <button>
const button = document.querySelector("button");
button.addEventListener("click", () => {
  inscrever();
});