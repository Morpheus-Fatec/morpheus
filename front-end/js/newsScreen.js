const bootstrap = window.bootstrap;

new Vue({
    el: '#news',
    template: '#template-news',
    data: {
        searchQuery: '',
        filteredNewsList: [],
        newsName: '',
        newsAddress: '',
        newsTags: '',
        newName: '', 
        newAddress: '', 
        newTag: [], 
        listTags:[],
        newTagAdd: {},
        newPortal:{
            id:null,
            tituloModal:"",
            action:"",
            erro:{
                status:false,
                texto:""
            },
            tags:[]
        }
    },



    mounted() {
        this.getNews();
        this.getTags();
    },

    methods: {

    openRegister() {
        this.newPortal.tituloModal = "Cadastrar Portal de Noticias";
        this.newPortal.action = "Cadastrar";
        const myModal = new bootstrap.Modal(document.getElementById('modalRegister'));
        myModal.show();
      },

      editRegister(news){
        this.newTag = news.tags;
        this.newName = news.name;
        this.newAddress = news.url;
        this.openRegister();
        this.newPortal.tituloModal = "Editar Portal de Noticias";
        this.newPortal.action = "Editar";
      },

      addTag(){
        newTagAdd = this.listTags.find(item => item.id === this.newsTags);
        this.newTag.push(newTagAdd);

        const index = this.listTags.findIndex(element => element.id === this.newsTags);
        if (index !== -1) {
            this.listTags.splice(index, 1);
        }

        this.newsTags = {};
      },

      removeTag(tag){
        const index = this.newTag.findIndex(element => element.id === tag.id);
        if (index !== -1) {
            this.newTag.splice(index, 1);
        }

        this.listTags.push(tag);
      },
      getTags(){
        axios
        .get('https://m3paloma.free.beeceptor.com/tags')
        .then(response => {
          this.listTags = response.data.data;
        })
        .catch(error => {
          console.error(error);
        });
      },
      getNews() {
        axios.get('https://m3paloma.free.beeceptor.com/todos')
            .then(response => {
                response.data.forEach(portalNoticia => {
                    const itemAdd = new Object();
                    itemAdd.id = portalNoticia.id; 
                    itemAdd.name = portalNoticia.name; 
                    itemAdd.url = portalNoticia.url; 
                    itemAdd.tags = portalNoticia.tags;
                    this.filteredNewsList.push(itemAdd);
                });

            })
            .catch(error => {
                console.error("Erro ao carregar dados:", error);
                alert("Erro ao carregar os dados. Verifique o console para mais detalhes.");
                throw  error;

            });
    },    

    postNews(action) {
        try
        {
            this.newPortal.erro.status = false;
            this.newPortal.erro.texto = '';
            if (!this.newName.trim() || !this.newAddress.trim()) {
                    throw "Preencha todos os campos";
                };

                const newsSource = {
                    name: this.newName,
                    url: this.newUrl,
                    tags: this.newTag};
                
                if(action == "Cadastrar"){
                    axios.post('https://morpheus-api.free.beeceptor.com/todos', newsSource)
                        .then(response => {
                            this.getNews();
                            this.resetForm();

                            const myModal = new bootstrap.Modal(document.getElementById('modalRegister'));
                            myModal.hide();
                        })
                        .catch(error => {
                            console.error("Erro ao cadastrar notícia:", error);
                            this.newPortal.erro.status = true;
                            this.newPortal.erro.texto = "Erro ao cadastrar a notícia. Tente novamente.";
                        });
                }else{
                    axios.put(`https://morpheus-api.free.beeceptor.com/todos/${this.newPortal.id}`, newsSource)
                        .then(response => {
                            this.getNews();
                            this.resetForm();

                            const myModal = new bootstrap.Modal(document.getElementById('modalRegister'));
                            myModal.hide();
                        })
                        .catch(error => {
                            console.error("Erro ao editar notícia:", error);
                            this.newPortal.erro.status = true;
                            this.newPortal.erro.texto = "Erro ao editar a notícia. Tente novamente.";
                        });
                                    }
                }
        catch (error) {
            this.newPortal.erro.status = true;
            this.newPortal.erro.texto = error;
        }
    },

    openDelete(){
        const myModal = new bootstrap.Modal(document.getElementById('modalDelete'));
        myModal.show();
    },

    save(){
        this.filteredNewsList.push({
            id: this.nextId++,             
            name: this.newName,
            url: this.newAddress,
            tag: this.newTag,
        });

        this.resetForm()

        const myModal = bootstrap.Modal.getInstance(document.getElementById('modalRegister'));
        myModal.hide();
    },


    resetForm() {
        this.newName = '';
        this.newAddress = '';
        this.newTag = '';

    },

    filterNews() {
        const searchLower = this.searchNews.toLowerCase();
        this.filteredNewsList = this.newsList.filter(news => 
            news.name.toLowerCase().includes(searchLower) || 
            news.id.toString().includes(searchLower) // Busca pelo ID
        );
      },
    },
})