const app = Vue.createApp({
    data() {
        return {
            isLoading: false,
            cron: {
                active: false,
                periodice: "",
                hour: "",
                timeZone: "",
                timeout: "",
                isSubmited: false,
                alert: {
                    show: false,
                    type: 'warning', // Tipo do alerta ('warning', 'danger', 'success', etc.)
                    titleError: 'Erro!',
                    desc: 'Por favor, preencha todos os campos obrigatórios.'
                }
            },
            root: {
                formData: {
                    sourceSelected: {
                        address: '',
                        get:0,
                        post:0
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
            sourceApi: {
                formData: {
                    action: null,
                    model: null,
                    sourceSelected: {
                        address: null,
                        get:0,
                        post:0
                    },
                    alert: {
                        show: false,
                        type: 'warning',
                        titleError: 'Erro!',
                        desc: 'Por favor, preencha todos os campos obrigatórios.'
                    }
                },
                tags: {
                    modal: null,
                    selected: [],
                    filtered: [],
                    movedRemove: [],
                    movedAdd: [],
                    search: {
                        query: '',
                        selectedQuery: '',
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
                        address: null,
                        post:0,
                        get:0
                    },
                    modal: null
                },
                search: {
                    query: '',
                    field: 'address',
                    sort: {
                        field: 'address',
                        order: 'asc'
                    },
                }
            },
            tags: {
                all: [],
                filtered: [],
                modal: null,
                search: {
                    query: '',
                    field: 'name',
                    sort: {
                        field: 'name',
                        order: 'asc'
                    },
                },
                insert: {
                    active: false,
                    content: '',
                    isSubmitted: false
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
            },
            regionalism: {
                insertWord: '',
                alert: {
                    active: true,
                    class: "primary",
                    message: ""
                },
                delete: {
                    modal: null,
                    wordSelected: {
                        content: "",
                        id: null
                    }
                },
                insert: '',
                modal: null,
                words: [],
                search: "",
                filtered: [],
                wordSelected: {
                    search: "",
                    filtered: [],
                    available: false,
                    word: "",
                    synonyms: []
                },
                sort: {
                    order: 'asc'
                }
            },

        }
    },
    methods: {
        toggleVisibility() {
            this.isVisible = !this.isVisible;
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
        apiLoad() {
            axios.get('http://localhost:8080/morpheus/api')
                .then(response => {
                    this.sourceApi.all = [];
         
                    response.data.forEach(portalNoticia => {

                        const itemAdd = new Object();
                        itemAdd.code = portalNoticia.code;
                        itemAdd.address = portalNoticia.address;
                        itemAdd.get = portalNoticia.get;
                        itemAdd.post = portalNoticia.post;
                        itemAdd.tags = portalNoticia.tagCodes;
                        this.sourceApi.all.push(itemAdd);

                    });
                    console.log(this.sourceApi.all);
                    this.apiFilter();
                })
                .catch(error => {
                    throw error;
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal');
                });
        },
        apiUpdateSearch() {
            this.apiFilter();
        },
        apiFilter() {
            const query = this.sourceApi.search.query.toLowerCase();
            this.sourceApi.filtered = this.sourceApi.all
                .filter(news =>
                    news['address'].toLowerCase().includes(query)
                );
        },
        apiToggleSort(field) {
            this.sourceApi.search.sort.field = field;
            this.sourceApi.search.sort.order = this.sourceApi.search.sort.order === 'asc' ? 'desc' : 'asc';
            this.apiFilter();
        },
        apiStartCreated() {
            this.sourceApi.formData.sourceSelected = {
                code: "",
                address: "",
                get:0,
                post:0,
                tags: []
            };
            this.sourceApi.formData.isSubmitted = false;
            this.root.formData.alert.show = false;
            this.sourceApi.formData.action = "create";
            const modalElement = this.$refs.sourceNewsFormModal;
            this.sourceApi.formData.modal = new bootstrap.Modal(modalElement);
            this.sourceApi.formData.modal.show();
        },

        apiStartEdit(news) {
            this.root.formData.alert.show = false;
            this.sourceApi.formData.action = "edit";
            this.sourceApi.formData.sourceSelected = {
                code: news.code,
                address: news.address,
                get:news.get,
                post:news.post,
                tags: news.tags
            };
            const modalElement = this.$refs.sourceNewsFormModal;
            this.sourceApi.formData.modal = new bootstrap.Modal(modalElement);
            this.sourceApi.formData.modal.show();
        },
        apiMontedAlert(type, message, title) {
            this.sourceApi.formData.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            }
        },
        apiSave() {
            this.sourceApi.formData.isSubmitted = true;
            if (this.sourceApi.formData.sourceSelected.address) {
                this.sourceApi.formData.modal.hide();

                let endpoint = this.sourceApi.formData.action === 'create'
                    ? 'http://localhost:8080/morpheus/api'
                    : `http://localhost:8080/morpheus/api/${this.sourceApi.formData.sourceSelected.code}`;

                const payload = {
                    code: this.sourceApi.formData.sourceSelected.code,
                    address: this.sourceApi.formData.sourceSelected.address,
                    tagCodes: this.sourceApi.formData.sourceSelected.tags,
                    get: this.sourceApi.formData.sourceSelected.get ? 1 : 0,
                    post: this.sourceApi.formData.sourceSelected.post ? 1 : 0,
                };

                if (this.sourceApi.formData.action != 'create') {

                }

                const request = this.sourceApi.formData.action === 'create'
                    ? axios.post(endpoint, payload)
                    : axios.put(endpoint, payload);

                request
                    .then(response => {
                        this.rootMontedAlert('success', 'Foi salvo com sucesso a Api: ' + this.sourceApi.formData.sourceSelected.address, 'API salvo com sucesso');
                        this.apiLoad();
                        this.sourceApi.formData.isSubmitted = false;
                    })
                    .catch(error => {
                        this.apiMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                        this.sourceApi.formData.isSubmitted = false;
                    });
            } else {
                this.apiMontedAlert('danger', 'Preencha todos os campos', 'Erro ao tentar salvar! ');
            }
        },
        apiDelete(api) {
            this.sourceApi.delete.sourceSelected = api;
            const modalElement = this.$refs.sourceNewsDeleteModal;
            this.sourceApi.delete.modal = new bootstrap.Modal(modalElement);
            this.sourceApi.delete.modal.show();
        },
        apiConfirmDelete() {
            this.sourceApi.delete.modal.hide();
            const code = this.sourceApi.delete.sourceSelected.code;
            const endpoint = `http://localhost:8080/morpheus/api/${code}`;

            axios.delete(endpoint)
                .then(response => {
                    this.apiLoad();
                    this.rootMontedAlert('success', 'Foi excluida com sucesso a API: ' + this.sourceApi.delete.sourceSelected.address, 'API excluida com sucesso');
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar excluir!');
                });
        },

        tagsForSourceNewsOpen(news) {
            const modalElement = this.$refs.tagsForSourceNewsModal;
            this.sourceApi.tags.modal = new bootstrap.Modal(modalElement);
            this.sourceApi.tags.modal.show();
            this.sourceApi.tags.newsSelected = news;
            this.sourceApi.tags.selected = news.tags;
        },
        tagsForSourceNewsAdd() {
            this.sourceApi.tags.selected.push(...this.sourceApi.tags.movedAdd);
            this.sourceApi.tags.movedAdd = [];
        },
        tagsForSourceNewsRemove() {
            this.sourceApi.tags.selected = this.sourceApi.tags.selected.filter(tagCode => !this.sourceApi.tags.movedRemove.includes(tagCode));
            this.sourceApi.tags.movedRemove = [];
        },
        tagsForSourceNewsCreateTag() {
            this.tags.insert.content = this.tags.search.query;
            this.tagCreate();
            this.tags.search.query = "";
        },
        tagsForSourceNewsSave() {
            this.sourceApi.formData.isSubmitted = true;
            const endpoint = `http://localhost:8080/morpheus/api/${this.sourceApi.tags.newsSelected.code}`;

            const payload = {
                code: this.sourceApi.tags.newsSelected.code,
                address: this.sourceApi.tags.newsSelected.address,
                get: this.sourceApi.tags.newsSelected.get,
                post: this.sourceApi.tags.newsSelected.post,
                tagCodes: this.sourceApi.tags.selected
            };

            axios.put(endpoint, payload)
                .then(response => {
                    this.rootMontedAlert('success', 'Foi salvo com sucesso as tags do portal: ' + this.sourceApi.tags.newsSelected.name, 'Tags salvas com sucesso');
                })
                .catch(error => {
                    this.apiMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                })
                .finally(final => {
                    this.sourceApi.tags.modal.hide();
                    this.apiLoad();
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
        tagsOpenMananger() {
            const modalElement = this.$refs.tagsModal;
            this.tags.modal = new bootstrap.Modal(modalElement);
            this.tags.modal.show();
        },
        tagsFilter() {
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
                        tagCode: tag.tagCode,
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
        tagCreate() {
            this.tags.insert.isSubmitted = true;

            if (this.tags.insert.content) {
                axios
                    .post('http://localhost:8080/morpheus/tag', {
                        tagName: this.tags.insert.content
                    })
                    .then(response => {
                        this.tagsLoad();
                        this.tagsMontedAlert('success', 'Foi salvo com sucesso a tag: ' + this.tags.insert.content, 'Tag salva com sucesso');
                        this.tags.insert.content = "";
                    })
                    .catch(error => {
                        this.tagsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                    });
            }
        },
        tagEdit(tag) {
            tag.isEditing = !tag.isEditing;
        },
        tagSave(tag) {
            tag.isSubmitted = true;
            if (tag.tagName) {
                axios
                    .put(`http://localhost:8080/morpheus/tag/${Number(tag.tagCode)}`, {
                        tagName: tag.tagName
                    })
                    .then(response => {
                        this.tagsMontedAlert('success', 'Foi salvo com sucesso a tag: ' + tag.tagName, 'Tag salva com sucesso');
                        this.tagsLoad();
                    })
                    .catch(error => {
                        this.tagsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
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
            const endpoint = `http://localhost:8080/morpheus/tag/${code}`;

            axios.delete(endpoint)
                .then(response => {
                    this.tagsMontedAlert('success', 'Foi excluida com sucesso a tag: ' + this.tags.delete.tagSelected.tagName, 'Tag excluida com sucesso');
                })
                .catch(error => {
                    this.tagsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar excluir!');
                })
                .finally(final => {
                    this.tags.modal.show();
                    this.tagsLoad();
                });
        },
        cronLoad() {
            this.isLoading = true;
            axios.get('http://localhost:8080/morpheus/config/properties')
                .then(response => {
                    const config = response.data;
                    this.cron.active = config.active;
                    this.cron.periodice = config.frequency;
                    let parts = config.time.split(':');
                    this.cron.hour = parts[0];
                    this.cron.minute = parts[1];
                    this.cron.timeZone = config.timeZone;
                    this.cron.timeout = config.timeout / 1000;
                })
                .catch(error => {
                    this.cronMontedAlert('danger', 'Houve uma indisponibilidade no sistema tente novamente mais tarde.', 'Erro ao carregar dados do cron.');
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        cronValidateHour() {
            if (this.cron.hour > 23) {
                this.cron.hour = 23;
            } else if (this.cron.hour < 0) {
                this.cron.hour = 0;
            }
        },
        cronValidateMinute() {
            if (this.cron.minute > 59) {
                this.cron.minute = 59;
            } else if (this.cron.minute < 0) {
                this.cron.minute = 0;
            }
        },
        cronValidateTimeout(){
            if (this.cron.timeout > 15) {
                this.cron.timeout = 15;
                this.cronMontedAlert('danger', 'O valor máximo permitido para o timeout é 15 segundos.', 'Erro de validação');
            } else if (this.cron.timeout < 1) {
                this.cron.timeout = 1;
                this.cronMontedAlert('danger', 'O valor mínimo permitido para o timeout é 1 segundo.', 'Erro de validação');
            }
        },
        validateTimeoutInput(event) {
            const value = event.target.value;
            if (value < 1) {
                this.cron.timeout = 1;
            } else if (value > 15) {
                this.cron.timeout = 15;
            } else {
                this.cron.timeout = value;
            }
        },
        cronSalvarConfiguracao() {
            this.cronValidateTimeout();
        
            if (this.cron.timeout > 15 || this.cron.timeout < 1) {
                this.cronMontedAlert('danger', 'O valor do timeout deve estar entre 1 e 15 segundos.', 'Erro de validação');
                return;
            }
        
            this.cron.isSubmited = true;
        
            if (this.cron.active && (!this.cron.periodice || this.cron.hour === '' || this.cron.minute === '' || !this.cron.timeZone || this.cron.timeZone === '' || this.cron.timeout === '' || this.cron.timeout > 15 || this.cron.timeout < 1)) { 
                this.cronMontedAlert('danger', 'Por favor, preencha todos os campos obrigatórios.', 'Erro ao salvar o cron.');
                return;
            }
        
            this.isLoading = true;
            const payload = {
                frequency: this.cron.periodice,
                time: this.cron.hour + ':' + this.cron.minute,
                timeZone: this.cron.timeZone,
                timeout: this.cron.timeout * 1000,
                active: this.cron.active.toString()
            };
        
            axios.post('http://localhost:8080/morpheus/config/update', payload)
                .then(response => {
                    const offcanvasElement = this.$refs.offcanvas;
                    const bsOffcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
                    if (bsOffcanvas) {
                        bsOffcanvas.hide(); // Esta linha fecha a janela
                    }
                    this.rootMontedAlert('success', 'Configuração do Cron salva com sucesso.', 'Configuração salva com sucesso.');
                })
                .catch(error => {
                    this.cronMontedAlert('danger', 'Houve uma indisponibilidade no sistema tente novamente mais tarde.', 'Erro ao salvar os dados do cron.');
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        cronMontedAlert(type, message, title) {
            this.cron.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            }
        },

        regionalismOpen() {
            const modalElement = this.$refs.regionalismModal;
            this.regionalism.modal = new bootstrap.Modal(modalElement);
            this.regionalism.modal.show();
            this.getWordsRegionalism();
        },
        getWordsRegionalism() {
            this.isLoading = true;
            axios.get('http://localhost:8080/texts')
                .then(response => {
                    const words = response.data;
        

                    this.regionalism.words = [];

                    words.forEach(word => {
                        let item = new Object();
                        item.content = word.content;
                        item.code = word.code;
                        item.synonyms = word.synonyms;
                        this.regionalism.words.push(item);
                    });

                })
                .catch(error => {
                    this.alertRegionalism("Erro ao carregar os sinônimos", "danger");
                })
                .finally(final => {
                    this.filterWords();
                    this.isLoading = false;
                });
            this.regionalism.wordSelected.available = false;
            this.regionalism.alert = {
                active: false,
                class: "primary",
                message: ""
            };
        },
        alertRegionalism(messageInsert, classInsert) {
            this.regionalism.alert = {
                active: true,
                class: classInsert,
                message: messageInsert
            }
        },
        filterWords() {
            const query = this.regionalism.search.toLowerCase();
            this.regionalism.filtered = this.regionalism.words
                .filter(word => word.content.toLowerCase().includes(query))
                .sort((a, b) => {
                    const result = a.content.toLowerCase().localeCompare(b.content.toLowerCase());
                    return this.regionalism.sort.order === 'asc' ? result : -result;
                });
        },
        filterWordsSynonyms() {
            const query = this.regionalism.wordSelected.search.toLowerCase();
            const selectedWordId = this.regionalism.wordSelected.word.code;

            this.regionalism.wordSelected.filtered = this.regionalism.words
                .filter(word =>
                    word.content.toLowerCase().includes(query) &&
                    word.code !== selectedWordId
                )
                .sort((a, b) => {
                    const result = a.content.toLowerCase().localeCompare(b.content.toLowerCase());
                    return this.regionalism.sort.order === 'asc' ? result : -result;
                });
        },
        editWord(word) {
            this.regionalism.wordSelected.word = word;
            this.regionalism.wordSelected.available = true;
            this.regionalism.search = "";
            this.filterWordsSynonyms();
            this.regionalism.wordSelected.synonyms = word.synonyms;
        },
        saveWord() {
            this.isLoading = true;
            const idWord = this.regionalism.wordSelected.word.code;
            axios.put(`http://localhost:8080/texts/${idWord}`, {
                synonymIds: this.regionalism.wordSelected.synonyms,
                textoDescription: this.regionalism.wordSelected.word.content
            })
                .then(response => {
                    this.getWordsRegionalism();
                    this.alertRegionalism("Editado com sucesso", "success");

                })
                .catch(error => {
                    this.alertRegionalism("Erro ao atualizar a palavra", "danger");
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        addWord() {
            if (this.regionalism.insertWord === "") {
                this.alertRegionalism("Preencha o conteúdo", "danger");
                return false;
            }
            this.isLoading = true;
            axios.post('http://localhost:8080/texts', {
                textDescription: this.regionalism.insertWord
            })
                .then(response => {
                    this.regionalism.insertWord = "";

                    this.getWordsRegionalism();
                    this.alertRegionalism("Cadastro realizado com sucesso", "success");
                })
                .catch(error => {
                    this.alertRegionalism("Erro ao cadastrar a palavra", "danger");
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        isSynonymSelected(word) {
            return this.regionalism.wordSelected.synonyms.includes(word.code);
        },
        toggleSynonym(id) {
            const index = this.regionalism.wordSelected.synonyms.indexOf(id);
            if (index > -1) {
                this.regionalism.wordSelected.synonyms.splice(index, 1);
            } else {
                this.regionalism.wordSelected.synonyms.push(id);
            }
        },
        deleteWord(word) {
            this.regionalism.delete.wordSelected = word;
            const modalElement = this.$refs.regionalismDeleteModal;
            this.regionalism.delete.modal = new bootstrap.Modal(modalElement);
            this.regionalism.modal.hide();
            this.regionalism.delete.modal.show();
        },
        confirmDeleteWord() {
            this.isLoading = true;
            axios.delete(`http://localhost:8080/texts/${this.regionalism.delete.wordSelected.code}`)
                .then(response => {
                    this.alertRegionalism("Palavra deletada com sucesso", "success");
                })
                .catch(error => {
                    this.alertRegionalism("Erro ao deletar palavra", "danger");
                })
                .finally(final => {
                    this.regionalism.modal.show();
                    this.regionalism.delete.modal.hide();
                    this.isLoading = false;
                    this.getWordsRegionalism();
                });
        },
        cancelDeleteWord() {
            this.regionalism.modal.show();
            this.regionalism.delete.modal.hide();
        },
        startUserGuide() {
            let stepsGuide = [];
            stepsGuide = [
                {
                    element: '#titulo-pagina',
                    intro: 'Aqui você pode gerenciar todas as fontes de dados baseadas em APIs.',
                    position: 'bottom'
                },
                {
                    element: '#botao-cadastrar',
                    intro: 'Aqui você pode cadastrar uma nova fonte de dados baseada em API',
                    position: 'bottom'
                },
                {
                    element: '#botao-tags',
                    intro: 'Aqui você pode gerenciar as tags do sistema',
                    position: 'bottom'
                },
                {
                    element: '#botao-regionalismo',
                    intro: 'Aqui você pode gerenciar os regionalismos linguisticos',
                    position: 'bottom'
                },
                {
                    element: '#botao-configurar',
                    intro: 'Aqui você pode gerenciar as configurações do sistema',
                    position: 'bottom'
                },
                {
                    element: '#busca-viva',
                    intro: 'Aqui você pode realizar uma busca entre todas as fontes já cadastradas',
                    position: 'bottom'
                },
                {
                    element: '#tabela-ver-dados',
                    intro: 'Aqui você pode ver e editar todas as fontes de dados baseadas em APIs',
                    position: 'bottom'
                }
            ]
    
            introJs().setOptions({
                steps: stepsGuide,
                nextLabel: 'Próximo',
                prevLabel: 'Anterior',
                skipLabel: '<i class="fa fa-times"></i>',
                doneLabel: 'Concluir'
              })
              .onchange(async function(element) {
              })
              .start();
        }
    },
    
    computed: {
        selectedTags() {
            return this.tags.all
                .filter(tag => this.sourceApi.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.selectedQuery ||
                        tag.tagName.toLowerCase().includes(this.tags.search.selectedQuery.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        },
        unselectedTags() {
  
            return this.tags.all
                .filter(tag => !this.sourceApi.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.query ||
                        tag.tagName.toLowerCase().includes(this.tags.search.query.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        }
    },
    mounted() {
        this.apiLoad();
        this.tagsLoad();
        this.cronLoad();

        this.isLoading = true;
        setTimeout(() => {
            this.isLoading = false; // Para o loading após 3 segundos
        }, 3000);
    }
});

app.mount('.container');
