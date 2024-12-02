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
                tagsTitle: {
                    items: [],
                    selectItems: [],
                },
                tagsText: {
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
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal.');
                });
        },

        authorLoad() {
            axios.get('http://localhost:8080/morpheus/authors')
                .then(response => {
                    this.authorFilters = response.data.map(author => ({
                        autId: author.autId,
                        autName: author.autName
                    }));
                    this.initChoicesAuthors();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados dos autores.');
                });
        },

        tagsLoad() {
            this.tagFilters = [];
            axios.get('http://localhost:8080/morpheus/tag')
                .then(response => {
                    this.tagFilters = response.data.map(tag => ({

                        tagCode: tag.tagCode,
                        tagName: tag.tagName
                    }));
                    this.choicesTagsText();
                    this.choicesTagsTitle();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados das tags');
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
                choicesAuthors.setChoices([{ value: item.autId, label: item.autName }], 'value', 'label', false);
            });

            choicesAuthors.passedElement.element.addEventListener('change', (event) => {
                this.filters.authors.selectItems = Array.from(event.target.selectedOptions).map(option => ({
                    value: option.value,
                    label: option.textContent
                }));
            });
        },

        choicesTagsTitle() {
            const choicesTagsTitle = new Choices('#choices-tags-title', {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });
            choicesTagsTitle.clearStore();
            this.tagFilters.forEach(item => {
                choicesTagsTitle.setChoices([{ value: item.tagCode, label: item.tagName }], 'value', 'label', false);
            });

            choicesTagsTitle.passedElement.element.addEventListener('change', (event) => {
                this.filters.tagsTitle.selectItems = Array.from(event.target.selectedOptions).map(option => option.textContent);
            });
        },

        choicesTagsText() {
            const choicesTagsText = new Choices('#choices-tags-text', {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });
            choicesTagsText.clearStore();
            this.tagFilters.forEach(item => {
                choicesTagsText.setChoices([{ value: item.tagCode, label: item.tagName }], 'value', 'label', false);
            });

            choicesTagsText.passedElement.element.addEventListener('change', (event) => {
                this.filters.tagsText.selectItems = Array.from(event.target.selectedOptions).map(option => option.textContent);
            });
        },

        insertionTitle() {
            const choicesTitle = new Choices('#choices-title-remove-button', {
                removeItemButton: true,
                delimiter: ','
            });
            choicesTitle.passedElement.element.addEventListener('change', () => {
                this.searchTitle = choicesTitle.getValue(true);
            });
        },

        insertionText() {
            const choicesText = new Choices('#choices-text-remove-button', {
                removeItemButton: true,
                delimiter: ','
            });
            choicesText.passedElement.element.addEventListener('change', () => {
                this.searchText = choicesText.getValue(true);
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
                        this.filters.date.dateWrite = this.filters.date.dateRange;
                    } else {
                        this.filters.date.dateRange = '';
                        this.filters.date.dateWrite = '';
                    }
                }
            });
        },

        formatDate(date) {
            const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
            return date.toLocaleDateString('pt-BR', options);
        },

        filterData() {

            let combinedTitleSearch = [
                ...this.filters.tagsTitle.selectItems,
                ...this.searchTitle
            ];
            let combinedTextSearch = [
                ...this.filters.tagsText.selectItems,
                ...this.searchText
            ];

            const dataFilter = {
                textSearch: combinedTextSearch,
                sourcesOrigin: this.filters.portal.selectItems.map(item => parseInt(item.value, 10)),
                titleSearch: combinedTitleSearch,
                dateStart: this.filters.date.dateInit,
                dateEnd: this.filters.date.dateFinal,
                author: this.filters.authors.selectItems.map(item => parseInt(item.value, 10)),
            };

            axios.post('http://localhost:8080/morpheus/NewsFilter/search', dataFilter)
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
                    console.error('Erro:', error);
                });

                const offcanvasElement = document.getElementById('offcanvasWithBothOptions');
                const offcanvasInstance = bootstrap.Offcanvas.getInstance(offcanvasElement);
                
                if (offcanvasInstance) {
                    offcanvasInstance.hide();
        }
    },

    startUserGuide() {
        let stepsGuide = [];
        stepsGuide = [
            {
                element: '#titulo-pagina',
                intro: 'Aqui você pode consultar todas as noticias salvas.',
                position: 'bottom'
            },
            {
                element: '#busca-viva',
                intro: 'Aqui você pode realizar uma busca entre todas as noticias já listadas',
                position: 'bottom'
            },
            {
                element: '#filtros-busca',
                intro: 'Aqui você pode adicionar filtros relacionados ao conteudos das notícias como também dos portais que as mesmas são provenientes',
                position: 'bottom'
            },
            {
                element: '#paginacao',
                intro: 'Aqui você pode navegar entre as notícias, definindo a melhor forma de consultar os dados',
                position: 'bottom'
            },
            {
                element: '#tabela-ver-dados',
                intro: 'Aqui você pode ver os dados da notícias e ao clicar em ver conteudo será possível ver a notícia completa',
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
        this.insertionText();
        this.insertionTitle();

    },
});

app.mount('#app');
