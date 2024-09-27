const bootstrap = window.bootstrap;

new Vue({
  el: '#app',
  template: '#tag-screen-template',
  data: {
    tags: [],
    newPortalNews:{
        name:"",
        addres:"",
        newTag:"",
        tag:[]
    },
    modalData:{
        title:"Editar Portal de Noticias",
        action:"Cadastrar"
    },
    newsSource:[],
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
    addTag() {
        newTagAdd = this.tags.find(item => item.tagCod === this.newPortalNews.newTag);
        this.newTag.push(newTagAdd);

        const index = this.tags.findIndex(element => element.tagCod === this.newPortalNews.newTag);
        if (index !== -1) {
            this.tags.splice(index, 1);
        }

        this.newPortalNews = [];
    },

    removeTag(tag) {
        const index = this.newTag.findIndex(element => element.tagCod === tag.tagCod);
        if (index !== -1) {
            this.newTag.splice(index, 1);
        }

        this.tags.push(tag);
    },
    getTags() {
        axios
            .get('https://apimorpheus1.free.beeceptor.com/tags')
            .then(response => {
                this.tags = response.data.data;
            })
            .catch(error => {
                console.error(error);
            });
    },
    getNews(){
        axios.get('https://m5paloma.free.beeceptor.com/todos')
            .then(response => {
                this.newsSource = [];
                response.data.forEach(portalNoticia => {
                    if (portalNoticia.type == 1) {
                        const itemAdd = new Object();
                        itemAdd.id = portalNoticia.id;
                        itemAdd.name = portalNoticia.srcName;
                        itemAdd.address = portalNoticia.address;
                        itemAdd.tags = portalNoticia.tags;
                        this.newsSource.push(itemAdd);
                    }
                });

            })
            .catch(error => {
                console.error("Erro ao carregar dados:", error);
                alert("Erro ao carregar os dados. Verifique o console para mais detalhes.");
                throw error;

            });
    },

    editRegister(news) {
        console.log(news);
        this.newPortalNews.name = news.name;
        this.newPortalNews.address = news.address;
        this.newPortalNews.tags = news.tags;
        this.openModal();
        this.modalData.title = "Editar Portal de Noticias";
        this.modalData.action = "Editar";
    },


    handlePostTag() {
        erro = this.newPortalNews.addres == "" || this.newPortalNews.name == "";
        if (erro) {
            axios
            .post('https://morpheus-api.free.beeceptor.com/', {
                name: this.newPortalNews.name,
                addres: this.newPortalNews.addres,
                tags: this.newPortalNews.tags,
            })
            .then(response => {
                this.newPortalNews.name = "";
                this.newPortalNews.addres = "";
                this.newPortalNews.tags = [];
                const myModal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));
                if (myModal) myModal.hide();
            })
            .catch(error => {
                console.error(error);
            });
        } else {
            alert('Preencha todos os campos.');
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
            this.getNews();
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
            this.getNews();
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
    this.getNews();
  }
});
