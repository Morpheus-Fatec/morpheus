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
                        address: null,
                        map: {
                            author: null,
                            body: null,
                            title: null,
                            date: null
                        }
                    },
                    alert: {
                        show: false,
                        type: 'warning',
                        titleError: 'Erro!',
                        desc: 'Por favor, preencha todos os campos obrigatórios.'
                    }
                },
                automaticMap: {
                    modal: null,
                    map: {
                        author: null,
                        body: null,
                        title: null,
                        url: null,
                        date: null
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

                        const itemAdd = new Object();
                        itemAdd.code = portalNoticia.code;
                        itemAdd.name = portalNoticia.srcName;
                        itemAdd.address = portalNoticia.address;
                        itemAdd.tags = portalNoticia.tagCodes;
                        itemAdd.map = portalNoticia.map;
                        this.sourceNews.all.push(itemAdd);

                    });
                    this.newsFilter();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal');
                });

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
            this.sourceNews.formData.sourceSelected = {
                code: "",
                name: "",
                address: "",
                tags: [],
                map: {
                    author: null,
                    body: null,
                    title: null,
                    date: null
                }
            };
            this.sourceNews.formData.isSubmitted = false;
            this.root.formData.alert.show = false;
            this.sourceNews.formData.action = "create";
            const modalElement = this.$refs.sourceNewsFormModal;
            this.sourceNews.formData.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.formData.modal.show();
        },
        newsStartEdit(news) {
            this.root.formData.alert.show = false;
            this.sourceNews.formData.action = "edit";
            this.sourceNews.formData.sourceSelected = {
                code: news.code,
                name: news.name,
                address: news.address,
                tags: news.tags,
                map: news.map
            };
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
                    code: this.sourceNews.formData.sourceSelected.code,
                    srcName: this.sourceNews.formData.sourceSelected.name,
                    address: this.sourceNews.formData.sourceSelected.address,
                    tagCodes: this.sourceNews.formData.sourceSelected.tags,
                    type: "1",
                    map: this.sourceNews.formData.sourceSelected.map
                };

                if (this.sourceNews.formData.action != 'create') {

                }

                const request = this.sourceNews.formData.action === 'create'
                    ? axios.post(endpoint, payload)
                    : axios.put(endpoint, payload);

                request
                    .then(response => {
                        this.rootMontedAlert('success', 'Foi salvo com sucesso o portal: ' + this.sourceNews.formData.sourceSelected.name, 'Portal salvo com sucesso');
                        this.newsLoad();
                        this.sourceNews.formData.isSubmitted = false;
                    })
                    .catch(error => {
                        this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                        this.sourceNews.formData.isSubmitted = false;
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
                    this.newsLoad();
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
                code: this.sourceNews.tags.newsSelected.code,
                srcName: this.sourceNews.tags.newsSelected.name,
                address: this.sourceNews.tags.newsSelected.address,
                tagCodes: this.sourceNews.tags.selected,
                map: this.sourceNews.tags.newsSelected.map,
                type: 1
            };

            axios.put(endpoint, payload)
                .then(response => {
                    this.rootMontedAlert('success', 'Foi salvo com sucesso as tags do portal: ' + this.sourceNews.tags.newsSelected.name, 'Tags salvas com sucesso');
                })
                .catch(error => {
                    this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar salvar!');
                })
                .finally(final => {
                    this.sourceNews.tags.modal.hide();
                    this.newsLoad();
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
                    this.tagsMontedAlert('success', 'Foi excluido com sucesso a tag: ' + this.tags.delete.tagSelected.tagName, 'Tag excluido com sucesso');
                })
                .catch(error => {
                    this.tagsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao tentar excluir!');
                })
                .finally(final => {
                    this.tags.modal.show();
                    this.tagsLoad();
                });
        },
        automaticMapOpen() {
            this.automaticMap ={
                map : {
                author: null,
                body: null,
                title: null,
                url: null,
                date: null
                },
                modal:null
            };
            this.sourceNews.automaticMap.isSubmitted = false;
            const modalElement = this.$refs.automaticMapModal;
            this.sourceNews.automaticMap.modal = new bootstrap.Modal(modalElement);
            this.sourceNews.automaticMap.modal.show();
            this.sourceNews.formData.modal.hide();
        },
        automaticMapExecute() {
            this.sourceNews.automaticMap.isSubmitted = true;
            if (this.sourceNews.automaticMap.map.url && this.sourceNews.automaticMap.map.title && this.sourceNews.automaticMap.map.body && this.sourceNews.automaticMap.map.date) {
                this.sourceNews.automaticMap.modal.hide();
                this.sourceNews.formData.modal.show();

                const payload = {
                    url: this.sourceNews.automaticMap.map.url,
                    title: this.sourceNews.automaticMap.map.title,
                    body: this.sourceNews.automaticMap.map.body,
                    date: this.sourceNews.automaticMap.map.date,
                    author: this.sourceNews.automaticMap.map.author
                };


                axios.post('http://localhost:8080/morpheus/source/mapping', payload)
                    .then(response => {
                        const data = response.data;
                        this.sourceNews.formData.sourceSelected.map.author = data.author || this.sourceNews.formData.sourceSelected.map.author;
                        this.sourceNews.formData.sourceSelected.map.body = data.body || this.sourceNews.formData.sourceSelected.map.body;
                        this.sourceNews.formData.sourceSelected.map.title = data.title || this.sourceNews.formData.sourceSelected.map.title;
                        this.sourceNews.formData.sourceSelected.map.date = data.date || this.sourceNews.formData.sourceSelected.map.date;
                        this.sourceNews.automaticMap.map.url = "";
                        this.sourceNews.automaticMap.map.title = "";
                        this.sourceNews.automaticMap.map.body = "";
                        this.sourceNews.automaticMap.map.date = "";
                        this.sourceNews.automaticMap.map.author = "";

                        this.newsMontedAlert('success', 'Foi realizado com sucesso a busca pelo mapeamento automático, para salvar o novo mapeamento clique em salvar.', 'Mapeamento realizado com sucesso.');
                    })
                    .catch(error => {

                        this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde.', 'Erro ao tentar realizar o mapeamento automático!');
                    });
            }
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
        clearInvalidFeedback() {
            if (this.sourceNews.automaticMap.isSubmitted && !this.sourceNews.automaticMap.map.body) {
                this.sourceNews.automaticMap.isSubmitted = false;
            }
        },
        submitForm() {
            this.sourceNews.automaticMap.isSubmitted = true;  
    
            if (this.sourceNews.automaticMap.map.body) {
                axios.post('http://localhost:8080/morpheus/source/mapping', payload)
                    .then(response => {
                        this.sourceNews.automaticMap.map.body = "";  
                    })
                    .catch(error => {
                    });
            } else {
                this.sourceNews.automaticMap.isSubmitted = false;
            }
        },
        startUserGuide(guide) {
            const vm = this;
            let stepsGuide = [];
            if(guide == 'main'){
                stepsGuide = [
                {
                    element: '#titleSources',
                    intro: 'Aqui você pode gerenciar todas as fontes de dados utilizadas para alimentar a base de notícias.',
                    position: 'bottom'
                  },
                  {
                    element: '#searchElements',
                    intro: 'Aqui você pode pesquisar usando uma busca viva por um portal de notícia',
                    position: 'bottom'
                  },
                  {
                    element: '#optionsSearch',
                    intro: 'Aqui é possível selecionar um campo a sua escolha para utilizar na filtragem de dados',
                    position: 'top'
                  },
                  {
                    element: '#allSources',
                    intro: 'Aqui ficam exibidos todos as fontes de dados resultantes da pesquisa',
                    position: 'right'
                  },
                  {
                    element: '#editTagsSource',
                    intro: 'Aqui é possível gerenciar as tags vinculadas a uma fonte de dados, sendo o numero exibido nesse botão o resultado total de tags vinculadas, caso não existam tags vínculadas o botão fica em vermelho, pois isso indica que a fonte de dados nunca terá dados salvos',
                    position: 'bottom'
                  },
                  {
                    element: '#removeTagsSource',
                    intro: 'Aqui é possível excluir uma fonte de dados',
                    position: 'top'
                  },
                  {
                    element: '#btnCreateSource',
                    intro: 'Aqui é possível realizar o cadastro de uma nova fonte de dados',
                    position: 'right'
                  },
                  {
                    element: '#btnManTags',
                    intro: 'Aqui é possível realizar o gerenciamento das tags usadas no sistema',
                    position: 'right'
                  },
                  {
                    element: '#btnManRegionalism',
                    intro: 'Aqui é possível realizar o gerenciamento dos termos usados no regionalismo',
                    position: 'bottom'
                  },
                  {
                    element: '#btnConfig',
                    intro: 'Aqui é possível ajustar as configurações técnicas do sistema como ajuste do cron',
                    position: 'top'
                  }
                ]
            }

            if(guide == 'sources'){
                stepsGuide = [
                {
                    element: '#titleSourcesModal',
                    intro: 'Aqui você pode editar ou cadastrar fontes de dados',
                    position: 'bottom'
                  },
                  {
                    element: '#nameSourcesModal',
                    intro: 'o campo novo representa o nome pelo qual a fonte de dados será representada dentro do sistema, esse campo é livre porem deve ser único',
                    position: 'bottom'
                  },
                  {
                    element: '#addresSourcesModal',
                    intro: 'O campo endereço representa a URL dessa fonte de dados, deve-se cadastrar o endereço mais especifico possível, isso para otimizar todo o funcionamento do armazenamento de notícias, Importante destacar que somente notícias que tiverem dentro do seu endereço o próprio endereço da fonte de dados, que podem ser consideradas para analise de conteúdo',
                    position: 'left'
                  },
                  {
                    element: '#mapAutSourcerModal',
                    intro: 'Aqui estão disponíveis para configuração o mapeamento, são dados referentes a como a fonte de dados organiza cada parte da notícia, de modo que o sistema consiga buscar os dados com mais precisão',
                    position: 'top'
                  },
                  {
                    element: '#btnMapAutSourcerModal',
                    intro: 'Aqui é possível abrir a funcionalidade de mapeamento automático',
                    position: 'bottom'
                  },
                  {
                    element: '#titleMapAutModal',
                    intro: 'Aqui é possível realizar a ação de mapear automaticamente um porta de notícias',
                    position: 'bottom'
                  },
                  {
                    element: '#urlMapAutModal',
                    intro: 'Primeiramente você deve colar aqui uma notícia utilizada no portal de notícias pertencente a fonte que você deseja cadastrar',
                    position: 'right'
                  },
                  {
                    element: '#frameMapAutModal',
                    intro: 'uma miniatura da notícia ira carregar na sequência, permitindo com que você copie cada dado da notícia, importante destacar que a miniatura é apenas uma ferramenta facilitadora podendo você copiar as informações diretamente da página.',
                    position: 'right'
                  },
                  {
                    element: '#titleInputMapAutModal',
                    intro: 'Insira aqui o título presente na notícia',
                    position: 'bottom'
                  },
                  {
                    element: '#bodyMapAutModal',
                    intro: 'Insira parte do corpo da notícia, não é preciso inserir a notícia inteira',
                    position: 'bottom'
                  },
                  {
                    element: '#dateMapAutModal',
                    intro: 'Insira aqui a data presente na notícia, utilizar no formato dd/mm/aaaa',
                    position: 'bottom'
                  },
                  {
                    element: '#authorMapAutModal',
                    intro: 'Insira aqui os autores presentes na notícia',
                    position: 'bottom'
                  },
                  {
                    element: '#mapAutSourcerModal',
                    intro: 'Os dados devem ser exibidos aqui de maneira automática, podendo também ser editado livremente.',
                    position: 'top'
                  },
                ]
            }

            let modalElement = this.$refs.sourceNewsFormModal;
            const createModal = new bootstrap.Modal(modalElement);
            modalElement = this.$refs.automaticMapModal;
            const automaticMapModal = new bootstrap.Modal(modalElement);

            const mD = this.sourceNews.formData.modal;

            introJs().setOptions({
                steps: stepsGuide,
                nextLabel: 'Próximo',
                prevLabel: 'Anterior',
                skipLabel: '<i class="fa fa-times"></i>',
                doneLabel: 'Concluir'
              })
              .onchange(async function(element) {
                if(guide == 'sources'){
                    const stepIndex = this._currentStep;
                    if (stepIndex == '5') {
                        mD.hide();
                        await showModal(automaticMapModal);
                    }
                    if (stepIndex == '6') {
                        vm.sourceNews.automaticMap.map.url = '../assets/newsExample.html';
                    }
                    if (stepIndex == '8') {
                        vm.sourceNews.automaticMap.map.body ="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam vestibulum nisi at felis commodo, ut";
                    }
                    if (stepIndex == '9') {
                        vm.sourceNews.automaticMap.map.date ="05/11/2024";
                    }
                    if (stepIndex == '10') {
                        vm.sourceNews.automaticMap.map.author ="João Silva";
                    }
                    if (stepIndex == '12') {
                        automaticMapModal.hide();
                        showModal(vm.sourceNews.formData.modal);
                    }
                }
              })
              .start();

              function showModal(modal) {
                return new Promise((resolve) => {
                    modal.show();
                    modal._element.addEventListener('shown.bs.modal', resolve, { once: true });
                });
            }
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
