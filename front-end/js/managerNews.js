const bootstrap = window.bootstrap;

new Vue({
  el: '#news',
  template: '#managerNews',
  data: {
    tags: [],
    newPortalNews: {
      code: "",
      name: "",
      address: "",
      newTag: "",
      tags: [],
      enabled: false
    },
    formData: {
      title: "Editar Portal de Noticias",
      action: "Cadastrar"
    },
    newsSource: [],
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

  mounted() {
    this.getNews();
    this.getTags();

  },

  methods: {
    openModal() {
      this.newPortalNews.enabled = true;
      this.formData.title = "Cadastrar Portal de Notícias";
      this.formData.action = "Cadastrar";

    },


    getNews() {
      axios.get('https://morpheus-api10.free.beeceptor.com/todos')
        .then(response => {
          this.newsSource = [];
          response.data.forEach(portalNoticia => {
            if (portalNoticia.type == 1) {
              const itemAdd = new Object();
              itemAdd.code = portalNoticia.code;
              itemAdd.name = portalNoticia.srcName;
              itemAdd.ss = portalNoticia.ss;
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
      this.newPortalNews.name = news.name;
      this.newPortalNews.ss = news.ss;
      this.newPortalNews.tags = news.tags;
      this.newPortalNews.enabled = true;
      this.formData.title = "Editar Portal de Noticias";
      this.formData.action = "Editar";

    },

    postNews() {

      if (this.formData.action === "Cadastrar") {
        axios.post('https://morpheus-api10.free.beeceptor.com/todos', {
          srcName: this.newPortalNews.name,
          address: this.newPortalNews.address,
          type: 1,
          tags: this.newPortalNews.tags

        })
          .then(response => {
            this.getNews();
            this.resetForm();
            this.newPortalNews.enabled = false;
          })
          .catch(error => {
            console.error("Erro ao cadastrar notícia:", error);
          });
        console.log("caralho")
      } else {
        axios.put(`https://morpheus-api10.free.beeceptor.com/todos/${this.newPortalNews.code}`, {
          srcName: this.newPortalNews.name,
          address: this.newPortalNews.address,
          type: 1,
          tags: this.newPortalNews.tags
        })
          .then(response => {
          this.getNews(); // Atualiza a lista de notícias
          this.resetForm(); 
            this.newPortalNews.enabled = false;
          })
          .catch(error => {
            console.error("Erro ao editar notícia:", error);
          });
      }
    },



  confirmDelete(news) {
    this.newsToDelete = news;
    const myModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
    myModal.show();
  },


  deleteRegister() {
    if (this.newsToDelete) {
      axios.delete(`https://morpheus-api10.free.beeceptor.com/todos/${this.newPortal.id}`)
        .then(response => {
          this.getNews(); // Atualiza a lista de notícias
          this.newsToDelete = null; // Reseta a variável
          const myModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
          myModal.hide(); // Fecha o modal
        })
        .catch(error => {
          console.error(error);
          alert("Erro ao excluir a notícia. Tente novamente."); // Feedback para o usuário
        });
    }
  },


  filterNews() {
    const searchLower = this.searchNews.toLowerCase();
    this.filteredNewsList = this.newsList.filter(news =>
      news.name.toLowerCase().includes(searchLower) ||
      news.id.toString().includes(searchLower) // Busca pelo ID
    );
  },



  addTag() {
    let newTagAdd = this.tags.find(item => item.id === this.newPortalNews.newTag);
    console.log(this.newPortalNews.tags)
    let tagObj = new Object();
    tagObj.name = newTagAdd.name;
    tagObj.id = newTagAdd.id;

    this.newPortalNews.tags.push(tagObj);

    const index = this.tags.findIndex(element => element.id === this.newPortalNews.newTag);
    if (index !== -1) {
      this.tags.splice(index, 1);
    }

    this.newPortalNews.newTag = "";
  },



  removeTag(tag) {
    const index = this.newPortalNews.tags.findIndex(t => t.id === tag.id);
    if (index !== -1) {
      this.newPortalNews.tags.splice(index, 1);
      this.tags.push(tag);
    }
  },

  getTags() {
    axios
      .get('https://morpheus-api10.free.beeceptor.com/tags')
      .then(response => {
        this.tags = response.data.map(tag => ({
          id: tag.tagCod,
          name: tag.tagName
        }));
      })
      .catch(error => {
        console.error(error);
      });
  },


  resetForm() {
    
  },
},
});
