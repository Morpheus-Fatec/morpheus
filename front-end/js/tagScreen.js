const bootstrap = window.bootstrap;

new Vue({
  el: '#app',
  data: {
    tags: [
      { id: 1, name: 'HTML', actions: ['Editar', 'Deletar'] },
      { id: 2, name: 'CSS', actions: ['Editar', 'Deletar'] },
      { id: 3, name: 'JavaScript', actions: ['Editar', 'Deletar'] },
      { id: 4, name: 'Vue.js', actions: ['Editar', 'Deletar'] }
    ],
    searchQuery: '',
    newTagName: '',
    tagToDelete: null,
    tagBeingEdited: null,
  },
  
  computed: {
    filteredTags() {
      return this.tags.filter(tag => 
        tag.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
    modalTitle() {
      return this.tagBeingEdited ? 'Editar tag' : 'Nova tag';
    },
    modalTitleId() {
      return this.tagBeingEdited ? 'editTagLabel' : 'exampleModalLabel';
    }
  },
  methods: {
    openModal() {
      const myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
      myModal.show();
    },
    saveTag() {
      if (this.newTagName.trim()) {
        if (this.tagBeingEdited) {
          this.tagBeingEdited.name = this.newTagName;
          this.tagBeingEdited = null;
        } else {
          const newId = this.tags.length ? Math.max(...this.tags.map(tag => tag.id)) + 1 : 1;
          this.tags.push({ id: newId, name: this.newTagName, actions: ['Editar', 'Deletar'] });
        }
        this.newTagName = '';
        const myModal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
        if (myModal) myModal.hide();
      } else {
        alert('O nome da tag não pode estar vazio.');
      }
    },
    editTag(tag) {
      this.newTagName = tag.name;
      this.tagBeingEdited = tag;
      const myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
      myModal.show();
    },
    confirmDelete(tag) {
      this.tagToDelete = tag;
      const myModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
      myModal.show();
    },
    deleteTag() {
      if (this.tagToDelete) {
        this.tags = this.tags.filter(tag => tag.id !== this.tagToDelete.id);
        this.tagToDelete = null;
        const myModal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
        if (myModal) myModal.hide();
      }
    },
  },
  template: `
  <div>
    <div class="container py-5">
      <div class="d-flex align-items-center gap-4">
        <img src="../assets/tag.png" width="80"/>
        <h1>Tags</h1>
      </div>
      <div class="mt-4 mb-3">
        <button class="btn btn-dark bg-gradient w-25" @click="openModal">Cadastrar tags</button>
      </div>
      <div class="input-group mb-3">
        <input type="text" class="form-control" v-model="searchQuery" aria-label="Text input with dropdown button" placeholder="Pesquisar tags...">
      </div>
      <table class="table" v-if="filteredTags.length">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Tag</th>
            <th scope="col">Ações</th>
          </tr>
        </thead>
        <tbody>
          <!-- Iterando sobre as tags filtradas -->
          <tr v-for="tag in filteredTags" :key="tag.id">
            <th scope="row">{{ tag.id }}</th>
            <td>{{ tag.name }}</td>
            <td>
              <button class="btn btn-sm btn-outline-dark mx-1" @click="editTag(tag)">
                Editar
              </button>
              <button class="btn btn-sm btn-outline-danger mx-1" @click="confirmDelete(tag)">
                Deletar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="alert alert-danger" role="alert">
        <p>Nenhuma tag encontrada.</p>
      </div>
      
      <!-- Modal de cadastro de tags -->
      <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" :id="modalTitleId">{{ modalTitle }}</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <form>
                <div class="mb-3">
                  <label for="recipient-name" class="col-form-label">Nome da Tag:</label>
                  <input type="text" class="form-control" v-model="newTagName" id="recipient-name">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
              <button type="button" class="btn btn-primary" @click="saveTag">Salvar</button>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal de confirmação de exclusão -->
      <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="confirmDeleteLabel">Confirmar Exclusão</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <div v-if="tagToDelete">
                Deseja realmente excluir a tag "<strong>{{ tagToDelete.name }}</strong>"?
              </div>
              <div v-else>
                <p>Tag não encontrada.</p>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <button type="button" class="btn btn-danger" @click="deleteTag">Excluir</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  `
})