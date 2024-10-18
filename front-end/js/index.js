const app = Vue.createApp({
    data() {
        return {
            isLoading:false,
            cron: {
                active: false,
                periodice: "",
                hour: "",
                timeZone: "",
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
                insertWord:  '',
                alert:{
                    active:true,
                    class:"primary",
                    message:""
                },
                delete:{
                    modal:null,
                    wordSelected:{
                        content:"",
                        id:null
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
                    this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal');
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
                        this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
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
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar excluir!');
                });
        },

        tagsForSourceNewsOpen(news) {
            const modalElement = this.$refs.tagsForSourceNewsModal;
            this.sourceNews.tags.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.tags.modal.show();
            this.sourceNews.tags.newsSelected = news;
            this.sourceNews.tags.selected = news.tags;

        },
        tagsForSourceNewsAdd() {
            this.sourceNews.tags.selected.push(...this.sourceNews.tags.movedAdd);
            this.sourceNews.tags.movedAdd = [];
        },
        tagsForSourceNewsRemove() {
            this.sourceNews.tags.selected = this.sourceNews.tags.selected.filter(tagCode => !this.sourceNews.tags.movedRemove.includes(tagCode));
            this.sourceNews.tags.movedRemove = [];
        },
        tagsForSourceNewsCreateTag() {
            this.tags.insert.content = this.tags.search.query;
            this.tagCreate();
            this.tags.search.query = "";
        },
        tagsForSourceNewsSave() {
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
        tagCreate() {
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
                    .put(`http://localhost:8080/morpheus/tag/${Number(tag.tagCod)}`, {
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
            const endpoint = `http://localhost:8080/morpheus/source/${code}`;

            axios.delete(endpoint)
                .then(response => {
                    this.tagsMontedAlert('success', 'Foi excluido com sucesso a tag: ' + this.tags.delete.tagSelected.tagName, 'Tag excluido com sucesso');
                })
                .catch(error => {
                    this.tagsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar excluir!');
                })
                .finally(final => {
                    this.tags.modal.show();
                });
        },

        cronLoad(){
            this.isLoading = true;
            axios.get('http://localhost:8080/morpheus/config/properties')
                .then(response => {
                    const config = response.data;
                    this.cron.active = config.active === 'true';
                    this.cron.periodice = config.frequency;
                    this.cron.hour = config.time;
                    this.cron.timeZone = config.timeZone;
                })
                .catch(error => {
                    this.cronMontedAlert('danger', 'Houve uma indisponibilidade no sistema tente novamente mais tarde.', 'Erro ao carregar dados do cron');
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
        cronSalvarConfiguracao() {
            this.cron.isSubmited = true;
            if (this.cron.active && (!this.cron.periodice || this.cron.hour === '' || this.cron.minute === '' || !this.cron.timeZone)) {
                this.cronMontedAlert('danger', 'Por favor, preencha todos os campos obrigatórios.', 'Erro ao salvar o cron');
                return;
            }

            this.isLoading = true;
            const payload = {
                frequency: this.cron.periodice,
                time: this.cron.hour,
                timeZone: this.cron.timeZone,
                active: this.cron.active.toString()
            };

            axios.post('http://localhost:8080/morpheus/config/properties', payload)
                .then(response => {
                    const offcanvasElement = this.$refs.offcanvas;
                    const bsOffcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
                    if (bsOffcanvas) {
                        bsOffcanvas.hide();
                    }
                    this.rootMontedAlert('success', 'Configuração do Cron salva com sucesso', 'Portal salvo com sucesso');
                })
                .catch(error => {
                    this.cronMontedAlert('danger', 'Houve uma indisponibilidade no sistema tente novamente mais tarde.', 'Erro ao salvar os dados do cron');
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
            axios.get('https://morpheus-palavras2.free.beeceptor.com/all')
                .then(response => {
                    const words = response.data;
                    
                    this.regionalism.words = [];

                    words.forEach(word => {
                        let item = new Object();
                        item.content = word.content;
                        item.id=word.id;
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
                active:false,
                class:"primary",
                message:""
            };
        },
        alertRegionalism(messageInsert, classInsert){
            this.regionalism.alert = {
                active:true,
                class:classInsert,
                message:messageInsert
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
            const selectedWordId = this.regionalism.wordSelected.word.id;

            this.regionalism.wordSelected.filtered = this.regionalism.words
                .filter(word =>
                    word.content.toLowerCase().includes(query) &&
                    word.id !== selectedWordId 
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
            const idWord = this.regionalism.wordSelected.word.id;
            axios.patch(`https://morpheus-palavras2.free.beeceptor.com/${idWord}`, {
                synonyms: this.regionalism.wordSelected.synonyms,
                content:this.regionalism.wordSelected.word.content
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
            if(this.regionalism.insertWord === ""){
                this.alertRegionalism("Preencha o conteúdo", "danger");
                return false;
            }
            this.isLoading = true;
            axios.post('https://morpheus-palavras2.free.beeceptor.com/', {
                content:this.regionalism.insertWord
            })
            .then(response => {
                  this.regionalism.insert = "";
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
            return this.regionalism.wordSelected.synonyms.includes(word.id);
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
        confirmDeleteWord(){
            this.isLoading = true;
            axios.delete(`https://morpheus-palavras2.free.beeceptor.com/${this.regionalism.delete.wordSelected.id}`)
            .then(response => {
                this.alertRegionalism("Palavra deletada com sucesso", "success");
            })
            .catch(error => {
                this.alertRegionalism("Erro ao deletar palavra", "danger");
            })
            .finally(final=>{
                this.regionalism.modal.show();
                this.regionalism.delete.modal.hide();
                this.isLoading = false;
            });
        },
        cancelDeleteWord(){
            this.regionalism.modal.show();
            this.regionalism.delete.modal.hide();
        }
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
        this.cronLoad();

        this.isLoading = true;
        setTimeout(() => {
          this.isLoading = false; // Para o loading após 3 segundos
        }, 3000);
    }
});

app.mount('.container');
