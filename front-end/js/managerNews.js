const bootstrap = window.bootstrap;

const url = "https://m2paloma.free.beeceptor.com/";

function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth' // para uma rolagem suave
  });
}

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
    newsToDelete: null,
    tagBeingEdited: null,
  },

  computed: {
    filteredSources() {
      return this.newsSource.filter(source =>
        source.name.toLowerCase().includes(this.searchQuery.toLowerCase())
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
      axios.get(url+'todos')
        .then(response => {
          this.newsSource = [];
          response.data.forEach(portalNoticia => {
            if (portalNoticia.type == 1) {
              const itemAdd = new Object();
              itemAdd.code = portalNoticia.code;
              itemAdd.name = portalNoticia.srcName;
              itemAdd.address = portalNoticia.address;
              itemAdd.tags = portalNoticia.tags;
              this.newsSource.push(itemAdd);
            }
          });

        })
        .catch(error => {
          alert("Erro ao carregar os dados. Verifique o console para mais detalhes.");
        });

    },

    editRegister(news) {
      this.newPortalNews.name = news.name;
      this.newPortalNews.address = news.address;
      this.newPortalNews.tags = news.tags;
      this.tags = this.tags.filter(itemB => 
        !this.newPortalNews.tags.some(itemA => itemB.tagCod === itemA.tagCod)
      );
      this.newPortalNews.enabled = true;
      this.formData.title = "Editar Portal de Noticias";
      this.formData.action = "Editar";

      scrollToTop();
    },

    postNews() {
      if(this.newPortalNews.name == "" || this.newPortalNews.address == ""){
        alert("Preencha todos os campos");
        return false;
      }

      if (this.formData.action === "Cadastrar") {
        axios.post(url+'todos', {
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
            alert("Erro ao cadastrar notícia:", error);
          });
      } else {
        axios.put(`${url}todos/${this.newPortalNews.code}`, {
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
            alert("Erro ao editar notícia:", error);
          });
      }
    },

  confirmDelete(news) {
    this.newsToDelete = news;
    const myModal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
    myModal.show();
  },


  deleteRegister(news) {
    let result = confirm("Você realmente deseja excluir o portal de noticias " + news.name + "?");
    if (result) {
      axios.delete(`${url}todos/${news.code}`)
        .then(response => {
          this.getNews(); // Atualiza a lista de notícias
          //this.newsToDelete = null; // Reseta a variável
           
          //this.newsToDelete = true;
        })
        .catch(error => {
          //this.newsToDelete = false;
        });
    }
  },

  addTag() {
    let newTagAdd = this.tags.find(item => item.tagCod === this.newPortalNews.newTag);
    let tagObj = new Object();
    tagObj.tagName = newTagAdd.tagName;
    tagObj.tagCod = newTagAdd.tagCod;

    this.newPortalNews.tags.push(tagObj);

    const index = this.tags.findIndex(element => element.tagCod === this.newPortalNews.newTag);
    if (index !== -1) {
      this.tags.splice(index, 1);
    }

    this.newPortalNews.newTag = "";
  },



  removeTag(tag) {
    const index = this.newPortalNews.tags.findIndex(t => t.tagCod === tag.tagCod);
    if (index !== -1) {
      this.newPortalNews.tags.splice(index, 1);
      this.tags.push(tag);
    }
  },

  getTags() {
    this.tags = [];
    axios
      .get(url+'tags')
      .then(response => {
        this.tags = response.data.map(tag => ({
          tagCod: tag.tagCod,
          tagName: tag.tagName
        }));
      })
      .catch(error => {
      });
  },


  resetForm() {
    this.newPortalNews.name="";
    this.newPortalNews.address="";
    this.newPortalNews.tags=[];
    this.getTags();
  },
},
});
