const bootstrap = window.bootstrap;

new Vue({
  el: '#app',
  template: '#tag-screen-template',
  data: {
    tags: [],
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

    handleGetTags() {
      axios
        .get('https://morpheus-api.free.beeceptor.com/')
        .then(response => {
          this.tags = response.data.data;
        })
        .catch(error => {
          console.error(error);
        });
    },

    handlePostTag() {
      if (this.newTagName.trim()) {
        axios
          .post('https://morpheus-api.free.beeceptor.com/', {
            name: this.newTagName
          })
          .then(response => {
            console.log(response.data);
            this.newTagName = '';
            this.handleGetTags();
            const myModal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
            if (myModal) myModal.hide();
          })
          .catch(error => {
            console.error(error);
          });
      } else {
        alert('O nome da tag não pode estar vazio.');
      }
    },

    handlePutTag() {
      if (this.tagBeingEdited && this.newTagName.trim()) {
        axios
          .put(`https://morpheus-api.free.beeceptor.com/`, {
            name: this.newTagName
          })
          .then(response => {
            console.log(response.data);
            this.tagBeingEdited = null;
            this.newTagName = '';
            this.handleGetTags();
            const myModal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
            if (myModal) myModal.hide();
          })
          .catch(error => {
            console.error(error);
          });
      } else {
        alert('O nome da tag não pode estar vazio.');
      }
    },

    confirmDelete(tag) {
      this.tagToDelete = tag;
      const myModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
      myModal.show();
    },

    handleDeleteTag() {
      if (this.tagToDelete) {
        axios
          .delete(`https://morpheus-api.free.beeceptor.com/`)
          .then(response => {
            console.log(response.data);
            this.handleGetTags();
            this.tagToDelete = null;
            const myModal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
            if (myModal) myModal.hide();
          })
          .catch(error => {
            console.error(error);
          });
      }
    }
  },
  
  mounted() {
    this.handleGetTags();
  }
});
