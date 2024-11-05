const app = Vue.createApp({
    data() {
        return {
            newsList: [],
            pagination: {
                totalElements: 0,
                totalPages: 0,
                page: 1,
                items: 50
            },
            modalContent: {
                title: '',
                content: '',
                author: '',
                registryDate: '',
                autName: '',
                srcName: '',
                srcAddress: ''
            },
            news: {
                search: {
                    field: 'newsTitle',
                    query: '',
                    sort: {
                        field: 'name',
                        order: 'asc'
                    },
                },
                filteredNews: [],
            },
            isLoading: false,
            root: {
                alert: {
                    show: false,
                    type: 'warning',
                    titleError: 'Erro!',
                    desc: 'Por favor, preencha todos os campos obrigatórios.'
                }
            },
            filters: {
                portal: {
                    items: [],
                    selectItems: []
                },

                authors: {
                    items: [],
                    selectItems: []
                },
                date: {
                    dateRange: '',
                    dateWrite: '',
                    dateInit: '',
                    dateFinal: ''
                },
                title: {
                    items: [],
                    selectItems: []
                },
                tags: {
                    items: [],
                    selectItems: [],
                }
            },

            sourceFilters: [],
            authorFilters: [],
            tagFilters: [],
            searchTitle: [],
            searchText: [],
        };
    },

    methods: {
        newsLoad() {
            this.isLoading = true;
            axios.get(`http://localhost:8080/morpheus/News?page=${this.pagination.page}&itens=${this.pagination.items}`)
                .then(response => {
                    const data = response.data;
                    this.newsList = [];
                    data.news.forEach(element => {
                        const itemAdd = {
                            newsTitle: element.newsTitle,
                            newsContent: element.newsContent,
                            srcName: element.srcName,
                            scrAddress: element.srcAddress,
                            autName: element.autName,
                            newsRegistryDate: element.newsRegistryDate,
                            srcURL: element.srcURL
                        };
                        this.newsList.push(itemAdd);
                    });
                    this.pagination.totalPages = data.totalPages;
                    this.pagination.totalElements = data.totalElements;

                    this.newsFilter();
                })
                .catch(error => {
                    throw error;
                    this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao carregar notícias!');
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },
        srcLoad() {
            axios.get('http://localhost:8080/morpheus/source')
                .then(response => {
                    this.sourceFilters = response.data.map(portalNoticia => ({
                        code: portalNoticia.code,
                        name: portalNoticia.srcName
                    }));
                    this.initChoices();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'portal:Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal');
                });
        },

        authorLoad() {
            axios.get('https://morpheus-api21.free.beeceptor.com/todos')
                .then(response => {
                    this.authorFilters = response.data.map(author => ({
                        code: author.code,
                        name: author.name
                    }));
                    this.initChoicesAuthors();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'autor');
                });
        },


        tagsLoad() {
            this.tagFilters = [];
            axios.get('http://localhost:8080/morpheus/tag')
                .then(response => {
                    this.tagFilters = response.data.map(tag => ({
                        tagCod: tag.tagCod,
                        tagName: tag.tagName
                    }));
                    this.initChoicesTags();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'tag');
                })
        },

        newsFilter() {
            const query = this.news.search.query.toLowerCase();
            this.news.filtered = this.newsList
                .filter(news =>
                    news[this.news.search.field].toLowerCase().includes(query)
                )
                .sort((a, b) => {
                    const result = a['newsTitle'].toLowerCase().localeCompare(b['newsTitle'].toLowerCase());
                    return this.news.search.sort.order === 'asc' ? result : -result;
                });
        },

        changePage(page) {
            this.pagination.page = page;
            this.newsLoad();
        },

        openModal(content, title, author, data) {
            this.modalContent.content = content;
            this.modalContent.title = title;
            this.modalContent.author = author;
            this.modalContent.data = data;
            const modal = new bootstrap.Modal(document.getElementById('content'), {
                keyboard: false
            });
            modal.show();
        },
        closeModal() {
            const modal = bootstrap.Modal.getInstance(document.getElementById('content'));
            if (modal) {
                modal.hide();
            }
        },

        rootMontedAlert(type, message, title) {
            this.root.alert = {
                show: true,
                type: type,
                titleError: title,
                desc: message
            };

            setTimeout(() => {
                this.root.alert.show = false;
            }, 20000);
        },

        newsToggleSort(field) {
            this.news.search.sort.field = field;
            this.news.search.sort.order = this.news.search.sort.order === 'asc' ? 'desc' : 'asc';
            this.newsFilter();
        },
        initChoices() {
            if (this.sourceFilters.length) {
                const choices = new Choices('#choices-select', {
                    removeItemButton: true,
                    addItems: true,
                    duplicateItemsAllowed: false,
                    paste: true,
                    editItems: true,
                    allowHTML: true
                });

                choices.clearStore();
                choices.setChoices(this.sourceFilters, 'code', 'name', false);

                choices.passedElement.element.addEventListener('change', (event) => {
                    this.filters.portal.selectItems = Array.from(event.target.selectedOptions).map(option => ({
                        value: option.value,
                        label: option.textContent
                    }));
                });
            }
        },

        initChoicesAuthors() {
            const choicesAuthors = new Choices('#choices-select-authors', {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });

            choicesAuthors.clearStore();

            this.authorFilters.forEach(item => {
                choicesAuthors.setChoices([{ value: item.code, label: item.name }], 'value', 'label', false);
            });

            choicesAuthors.passedElement.element.addEventListener('change', (event) => {
                this.filters.authors.selectItems = Array.from(event.target.selectedOptions).map(option => ({
                    value: option.value,
                    label: option.textContent
                }));
            });
        },

        initChoicesTags(selector) {
            const choicesTags = new Choices(selector, {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });
        
            choicesTags.clearStore();
        
            this.tagFilters.forEach(item => {
                choicesTags.setChoices([{ value: item.tagCod, label: item.tagName }], 'value', 'label', false);
            });
        
            choicesTags.passedElement.element.addEventListener('change', (event) => {
                this.filters.tags.selectItems = Array.from(event.target.selectedOptions).map(option => option.textContent);
            });
        },
        

        insertionTitle() {
            const choice = new Choices('#choices-tags-remove-button', {
                removeItemButton: true,
                delimiter: ','
            });
            choice.passedElement.element.addEventListener('change', () => {
                this.searchTitle = choice.getValue(true);
            });
        },


        insertionText() {
            const choice = new Choices('#choices-text-remove-button', {
                removeItemButton: true,
                delimiter: ','
            });
            choice.passedElement.element.addEventListener('change', () => {
                this.searchText = choice.getValue(true);
            });

        },



        initDatePicker() {
            flatpickr("#dateRange", {
                dateFormat: "d/m/Y",
                locale: {
                    firstDayOfWeek: 1,
                    weekdays: {
                        shorthand: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
                        longhand: ['Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado']
                    },
                    months: {
                        shorthand: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
                        longhand: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro']
                    }
                },
                mode: "range",
                onChange: (selectedDates) => {
                    if (selectedDates.length === 2) {
                        this.filters.date.dateRange = `${this.formatDate(selectedDates[0])} até ${this.formatDate(selectedDates[1])}`;
                        this.filters.date.dateInit = this.formatDate(selectedDates[0]);
                        this.filters.date.dateFinal = this.formatDate(selectedDates[1]);
                        this.filters.date.dateWrite = this.filters.date.dateRange; // Atualiza o modelo de entrada
                        this.filterData(); // Chame filterData após a seleção de datas
                    } else {
                        this.filters.date.dateRange = '';
                        this.filters.date.dateWrite = ''; // Limpa o campo se menos de duas datas forem selecionadas
                    }
                }
            });
        },
        formatDate(date) {
            // Exemplo de formatação de data
            const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
            return date.toLocaleDateString('pt-BR', options); // Formatação para o padrão brasileiro
        },


        filterData() {
            console.log(this.tagFilters)

            const combinedTitleSearch = [
                ...this.filters.tags.selectItems,
                ...this.searchTitle
            ];
            const combinedTextSearch = [
                ...this.filters.tags.selectItems,
                ...this.searchText
            ];


            const dataFilter = {
                textSearch: combinedTextSearch,
                sourcesOrigin: this.filters.portal.selectItems.map(item => item.value),
                titleSearch: combinedTitleSearch,
                dateStart: this.filters.date.dateInit,
                dateEnd: this.filters.date.dateFinal,
                author: this.filters.authors.selectItems.map(item => item.value),
            };

            axios.post('https://morpheus-api21.free.beeceptor.com/todos', dataFilter)
                .then(response => {
                    const data = response.data;
                    this.filters.portal.selectItems = data.sourcesOrigin || this.filters.portal.selectItems;
                    this.filters.authors.selectItems = data.author || this.filters.authors.selectItems;
                    this.filters.date.dateInit = data.dateStart || this.filters.date.dateInit;
                    this.filters.date.dateFinal = data.dateEnds || this.filters.date.dateFinal;
                    combinedTextSearch = data.textSearch;
                    combinedTitleSearch = data.titleSearch;
                })

                .catch(error => {
                    console.error('Erro:', error);
                });

        }
    },

    computed: {
        filteredNews() {
            return this.newsFilter();
        }
    },


    mounted() {
        this.newsLoad();
        this.srcLoad();
        this.authorLoad();
        this.tagsLoad();
        this.initDatePicker();
        this.textInsertionTitle();
        this.insertionText();
        this.initChoicesTags('#choices-select-tags'); // Filtro por título
        this.initChoicesTags('#choices-select-tags-text'); // Filtro por texto

    },
});

app.mount('#app');
