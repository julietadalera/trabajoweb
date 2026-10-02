const API = "/items";

let itens = [];
let selecionado = null;

const $ = (id) => document.getElementById(id);

function aviso(texto, erro = false) {
  const p = $("aviso");
  p.textContent = texto;
  p.className = erro ? "erro" : "ok";
}

function urlImagem(foto) {
  return foto ? "/img/" + encodeURIComponent(foto) : null;
}

function mostrarPreview(src) {
  const box = $("preview");
  box.textContent = "";

  if (!src) {
    box.textContent = "Sem imagem";
    return;
  }

  const img = document.createElement("img");
  img.alt = "Imagem do item";
  img.onerror = () => {
    box.textContent = "Imagem não encontrada";
  };
  img.src = src;
  box.appendChild(img);
}

// GET /items: listar todos os itens
async function carregar() {
  try {
    const resposta = await fetch(API);
    if (!resposta.ok) {
      throw new Error("Código " + resposta.status);
    }
    itens = await resposta.json();
    desenhar();
  } catch (e) {
    aviso("Erro ao buscar os itens: " + e.message, true);
  }
}

function desenhar() {
  const corpo = $("corpo");
  corpo.textContent = "";

  if (itens.length === 0) {
    const tr = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 4;
    td.className = "vazio";
    td.textContent = "Nenhum item cadastrado.";
    tr.appendChild(td);
    corpo.appendChild(tr);
    return;
  }

  for (const item of itens) {
    const tr = document.createElement("tr");
    if (selecionado && selecionado.id === item.id) {
      tr.className = "selecionada";
    }
    tr.addEventListener("click", () => selecionar(item));

    const tdFoto = document.createElement("td");
    if (item.foto) {
      const img = document.createElement("img");
      img.className = "miniatura";
      img.alt = "";
      img.onerror = () => img.remove();
      img.src = urlImagem(item.foto);
      tdFoto.appendChild(img);
    }
    tr.appendChild(tdFoto);

    for (const valor of [item.id, item.nome, item.tipo]) {
      const td = document.createElement("td");
      td.textContent = valor;
      tr.appendChild(td);
    }

    corpo.appendChild(tr);
  }
}

function selecionar(item) {
  selecionado = item;
  $("nome").value = item.nome;
  $("tipo").value = item.tipo;
  $("arquivo").value = "";
  mostrarPreview(urlImagem(item.foto));
  desenhar();
}

function limparFormulario() {
  selecionado = null;
  $("nome").value = "";
  $("tipo").selectedIndex = 0;
  $("arquivo").value = "";
  mostrarPreview(null);
  desenhar();
}

// POST /upload: enviar a imagem e receber o nome salvo
async function enviarImagem(arquivo) {
  const dados = new FormData();
  dados.append("file", arquivo);

  const resposta = await fetch("/upload", { method: "POST", body: dados });
  if (!resposta.ok) {
    throw new Error("Erro ao enviar a imagem. Código: " + resposta.status);
  }
  return (await resposta.text()).trim();
}

async function lerFormulario() {
  const nome = $("nome").value.trim();
  if (!nome) {
    throw new Error("Digite um nome.");
  }

  const dados = { nome, tipo: $("tipo").value };

  const arquivo = $("arquivo").files[0];
  if (arquivo) {
    dados.foto = await enviarImagem(arquivo);
  }
  return dados;
}

// POST /items: cadastrar novo item
async function cadastrar() {
  try {
    const dados = await lerFormulario();

    const resposta = await fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados),
    });
    if (!resposta.ok) {
      throw new Error("Erro ao cadastrar. Código: " + resposta.status);
    }

    limparFormulario();
    await carregar();
    aviso("Item cadastrado com sucesso!");
  } catch (e) {
    aviso(e.message, true);
  }
}

// PUT /items/{id}: atualizar o item selecionado
async function editar() {
  if (!selecionado) {
    aviso("Selecione um item na tabela para editar.", true);
    return;
  }

  try {
    const dados = await lerFormulario();

    const resposta = await fetch(API + "/" + selecionado.id, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados),
    });
    if (!resposta.ok) {
      throw new Error("Erro ao editar. Código: " + resposta.status);
    }

    limparFormulario();
    await carregar();
    aviso("Item atualizado com sucesso!");
  } catch (e) {
    aviso(e.message, true);
  }
}

// DELETE /items/{id}: excluir o item selecionado
async function excluir() {
  if (!selecionado) {
    aviso("Selecione um item na tabela para excluir.", true);
    return;
  }

  if (!confirm("Deseja realmente excluir este item?")) {
    return;
  }

  try {
    const resposta = await fetch(API + "/" + selecionado.id, { method: "DELETE" });
    if (!resposta.ok) {
      throw new Error("Erro ao excluir. Código: " + resposta.status);
    }

    limparFormulario();
    await carregar();
    aviso("Item excluído com sucesso!");
  } catch (e) {
    aviso(e.message, true);
  }
}

$("btnCadastrar").addEventListener("click", cadastrar);
$("btnEditar").addEventListener("click", editar);
$("btnExcluir").addEventListener("click", excluir);
$("btnLimpar").addEventListener("click", limparFormulario);

$("arquivo").addEventListener("change", () => {
  const arquivo = $("arquivo").files[0];
  if (arquivo) {
    mostrarPreview(URL.createObjectURL(arquivo));
  }
});

carregar();
