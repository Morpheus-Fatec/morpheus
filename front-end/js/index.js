const app = Vue.createApp({
    data() {
        return {
            root: {
                formData: {
                    sourceSelected: {
                        name: '',
                        address: ''
                    },
                    isSubmitted: false,
                    alert: {
                        show: false,
                        type: 'warning', // Tipo do alerta ('warning', 'danger', 'success', etc.)
                        titleError: 'Erro!',
                        desc: 'Por favor, preencha todos os campos obrigatórios.'
                    }
                }
            },
            isVisible: true,
            sourceNews: {
                formData: {
                    action: null,
                    model: null,
                    sourceSelected: {
                        code: null,
                        name: null,
                        address: null
                    },
                    alert: {
                        show: false,
                        type: 'warning',
                        titleError: 'Erro!',
                        desc: 'Por favor, preencha todos os campos obrigatórios.'
                    }
                },
                tags:{
                    modal:null,
                    selected:[],
                    filtered:[],
                    movedRemove:[],
                    movedAdd:[],
                    search: {
                        query: '',
                        selectedQuery:'',
                        sort: {
                            order: 'asc'
                        },
                    }
                },
                all: [],
                filtered: [],
                newsSelected: null,
                delete: {
                    sourceSelected: {
                        name: null,
                        address: null
                    },
                    modal: null
                },
                search: {
                    query: '',
                    field: 'name',
                    sort: {
                        field: 'name',
                        order: 'asc'
                    },
                }
            },
            tags: {
                all:[],
                filtered: [],
                modal:null,
                search: {
                    query: '',
                    field: 'name',
                    sort: {
                        field: 'name',
                        order: 'asc'
                    },
                },
                insert:{
                    active:false,
                    content:'',
                    isSubmitted:false
                },
                delete: {
                    tagSelected: { tagCode: null, tagName: null },
                    modal: null
                },
                alert: {
                    show: false,
                    type: 'warning',
                    titleError: 'Erro!',
                    desc: 'Por favor, preencha todos os campos obrigatórios.'
                }
            }
        }
    },
    methods: {
        toggleVisibility() {
            this.isVisible = !this.isVisible; // Alterna a visibilidade
        },
        rootMontedAlert(type, message, title) {
            this.root.formData.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            }

            setTimeout(() => {
                this.root.formData.alert.show = false;
            }, 20000);
        },
        newsLoad() {
            axios.get('http://localhost:8080/morpheus/source')
            .then(response => {
                this.sourceNews.all = [];
                response.data.forEach(portalNoticia => {
                    if (portalNoticia.type == 1) {
                    const itemAdd = new Object();
                    itemAdd.code = portalNoticia.code;
                    itemAdd.name = portalNoticia.srcName;
                    itemAdd.address = portalNoticia.address;
                    itemAdd.tags = portalNoticia.tags;
                    this.sourceNews.all.push(itemAdd);
                    }
                });

                })
                .catch(error => {
                    this.newsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Não foi possível carregar os dados do portal');
                });
            this.newsFilter();
        },
        newsUpdateSearch() {
            this.newsFilter();
        },
        newsFilter() {
            const query = this.sourceNews.search.query.toLowerCase();
            this.sourceNews.filtered = this.sourceNews.all
                .filter(news =>
                    news[this.sourceNews.search.field].toLowerCase().includes(query)
                )
                .sort((a, b) => {
                    const result = a[this.sourceNews.search.sort.field].toLowerCase().localeCompare(b[this.sourceNews.search.sort.field].toLowerCase());
                    return this.sourceNews.search.sort.order === 'asc' ? result : -result;
                });
        },
        newsToggleSort(field) {
            this.sourceNews.search.sort.field = field;
            this.sourceNews.search.sort.order = this.sourceNews.search.sort.order === 'asc' ? 'desc' : 'asc';
            this.newsFilter();
        },
        newsStartCreated() {
            this.root.formData.alert.show = false;
            this.sourceNews.formData.action = "create";
            const modalElement = this.$refs.sourceNewsFormModal;
            this.sourceNews.formData.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.formData.modal.show();
        },
        newsStartEdit(news) {
            this.root.formData.alert.show = false;
            this.sourceNews.formData.action = "edit";
            this.sourceNews.formData.sourceSelected = news;
            const modalElement = this.$refs.sourceNewsFormModal;
            this.sourceNews.formData.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.formData.modal.show();
        },
        newsMontedAlert(type, message, title) {
            this.sourceNews.formData.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            }
        },
        newsSave() {
            this.sourceNews.formData.isSubmitted = true;
            if (this.sourceNews.formData.sourceSelected.name && this.sourceNews.formData.sourceSelected.address) {
                this.sourceNews.formData.modal.hide();

                let endpoint = this.sourceNews.formData.action === 'create'
                    ? 'http://localhost:8080/morpheus/source'
                    : `http://localhost:8080/morpheus/source/${this.sourceNews.formData.sourceSelected.code}`;

                const payload = {
                    srcName: this.sourceNews.formData.sourceSelected.name,
                    address: this.sourceNews.formData.sourceSelected.address,
                    tags: this.sourceNews.formData.tags,
                    type: 1
                };

                const request = this.sourceNews.formData.action === 'create'
                    ? axios.post(endpoint, payload)
                    : axios.patch(endpoint, payload);

                request
                    .then(response => {
                        this.rootMontedAlert('success', 'Foi salvo com sucesso o portal: ' + this.sourceNews.formData.sourceSelected.name, 'Portal salvo com sucesso');
                    })
                    .catch(error => {
                        this.newsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao tentar salvar!');
                    });
            } else {
                this.newsMontedAlert('danger', 'Preencha todos os campos', 'Erro ao tentar salvar! ');
            }
        },
        newsDelete(news) {
            this.sourceNews.delete.sourceSelected = news;
            const modalElement = this.$refs.sourceNewsDeleteModal;
            this.sourceNews.delete.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.delete.modal.show();
        },
        newsConfirmDelete() {
            this.sourceNews.delete.modal.hide();
            const code = this.sourceNews.delete.sourceSelected.code;
            const endpoint = `http://localhost:8080/morpheus/source/${code}`;

            axios.delete(endpoint)
                .then(response => {
                    this.rootMontedAlert('success', 'Foi excluido com sucesso o portal: ' + this.sourceNews.delete.sourceSelected.name, 'Portal excluido com sucesso');
                })
                .catch(error => {
                    this.rootMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao tentar excluir!');
                });
        },

        tagsForSourceNewsOpen(news){
            const modalElement = this.$refs.tagsForSourceNewsModal;
            this.sourceNews.tags.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.tags.modal.show();
            this.sourceNews.tags.newsSelected = news;
            this.sourceNews.tags.selected = news.tags;

        },
        tagsForSourceNewsAdd(){
            this.sourceNews.tags.selected.push(...this.sourceNews.tags.movedAdd);
            this.sourceNews.tags.movedAdd = [];
        },
        tagsForSourceNewsRemove(){
            this.sourceNews.tags.selected = this.sourceNews.tags.selected.filter(tagCode => !this.sourceNews.tags.movedRemove.includes(tagCode));
            this.sourceNews.tags.movedRemove = [];
        },
        tagsForSourceNewsCreateTag(){
            this.tags.insert.content = this.tags.search.query;
            this.tagCreate();
            this.tags.search.query = "";
        },
        tagsForSourceNewsSave(){
            this.sourceNews.formData.isSubmitted = true;
                const endpoint = `http://localhost:8080/morpheus/source/${this.sourceNews.tags.newsSelected.code}`;
            
                const payload = {
                    srcName: this.sourceNews.tags.newsSelected.name,
                    address: this.sourceNews.tags.newsSelected.address,
                    tags: this.sourceNews.tags.selected,
                    type: 1
                };

                axios.patch(endpoint, payload)
                    .then(response => {
                        this.rootMontedAlert('success', 'Foi salvo com sucesso as tags do portal: ' + this.sourceNews.tags.newsSelected.name, 'Tags salvas com sucesso');
                    })
                    .catch(error => {
                        this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                    })
                    .finally(final => {
                        
                    });
        },

        tagsMontedAlert(type, message, title) {
            this.tags.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            }
        },
        tagsOpenMananger(){
            const modalElement = this.$refs.tagsModal;
            this.tags.modal = new bootstrap.Modal(modalElement);
            this.tags.modal.show();
        },
        tagsFilter(){
            const query = this.tags.search.query.toLowerCase();
            this.tags.filtered = this.tags.all
                .filter(tag => tag.tagName.toLowerCase().includes(query))
                .sort((a, b) => {
                    const result = a.tagName.toLowerCase().localeCompare(b.tagName.toLowerCase());
                    return this.tags.search.sort.order === 'asc' ? result : -result;
                });
        },
        tagsLoad() {
            this.tags.all = [];
            axios.get('http://localhost:8080/morpheus/tag')
                .then(response => {
                this.tags.all = response.data.map(tag => ({
                    tagCod: tag.tagCod,
                    tagName: tag.tagName
                }));
                })
                .catch(error => {
                })
                .finally(final => {
                    this.tags.insert.content = "";
                    this.tags.insert.isSubmitted = false;
                    this.tags.insert.active = false;
                    this.tagsFilter();
                });
        },
        tagCreate(){
            this.tags.insert.isSubmitted = true;

            if (this.tags.insert.content) {
                axios
                    .post('http://localhost:8080/morpheus/tag', {
                        tagName: this.tags.insert.content
                    })
                    .then(response => {
                        this.tagsLoad();
                        this.tagsMontedAlert('success', 'Foi salvo com sucesso a tag: ' + this.sourceNews.formData.sourceSelected.name, 'Tag salva com sucesso');
                    })
                    .catch(error => {
                        this.tagsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao tentar salvar!');
                    });
            }
        },
        tagEdit(tag) {
            tag.isEditing = !tag.isEditing;
        },
        tagSave(tag) {
            tag.isSubmitted = true;
            if(tag.tagName){
                axios
                    .put(`http://localhost:8080/morpheus/tag/${Number(tag.tagCod)}`, {
                        tagName: tag.tagName
                    })
                    .then(response => {
                        this.tagsMontedAlert('success', 'Foi salvo com sucesso a tag: ' + tag.tagName, 'Tag salva com sucesso');
                        this.tagsLoad();
                    })
                    .catch(error => {
                        this.tagsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao tentar salvar!');
                        this.tagsLoad();
                    });
            }
        },
        tagDelete(tag) {
            this.tags.delete.tagSelected = tag;
            const modalElement = this.$refs.tagsDeleteModal;
            this.tags.delete.modal = new bootstrap.Modal(modalElement);
            this.tags.modal.hide();
            this.tags.delete.modal.show();
        },
        tagConfirmDelete() {
            this.tags.delete.modal.hide();
            const code = this.tags.delete.tagSelected.tagCode;
            const endpoint = `http://localhost:8080/morpheus/source/${code}`;

            axios.delete(endpoint)
                .then(response => {
                    this.tagsMontedAlert('success', 'Foi excluido com sucesso a tag: ' + this.tags.delete.tagSelected.tagName, 'Tag excluido com sucesso');
                })
                .catch(error => {
                    this.tagsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao tentar excluir!');
                })
                .finally( final => {
                    this.tags.modal.show();
                });
        },
    },
    computed: {
        selectedTags() {
            return this.tags.all
                .filter(tag => this.sourceNews.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.selectedQuery ||
                        tag.tagName.toLowerCase().includes(this.tags.search.selectedQuery.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        },
        unselectedTags() {
            return this.tags.all
                .filter(tag => !this.sourceNews.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.query ||
                        tag.tagName.toLowerCase().includes(this.tags.search.query.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        }
    },
    mounted() {
        this.newsLoad();
        this.tagsLoad();
    }
});

app.mount('.container');
