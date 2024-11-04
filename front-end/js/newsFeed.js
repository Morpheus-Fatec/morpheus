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

<<<<<<< HEAD
            filters: {
                portal: {
=======
            filters:{
                portal:{
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5
                    items: [],
                    selectItems: []
                },

<<<<<<< HEAD
                authors: {
=======
                authors:{
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5
                    items: [],
                    selectItems: []
                },

<<<<<<< HEAD
                date: {
                    dateRange: '',
                    dateWrite: '', 
                    dateInit:'',
                    dateFinal:''
                },

                title: {
                    items: [],
                    selectItems: []
                },
            },

            sourceFilters: [],
            authorFilters: [],
            tagFilters: [],
=======
                date:{
                    start: " ",
                    end: " ",
                }
            },
            sourceFilters:[]
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5

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
<<<<<<< HEAD
                             })
=======
                    this.initChoices();
                    this.populateFilterPortal();
                })
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5
                .catch(error => {
                    throw error;
                    this.newsMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Erro ao carregar notícias!');
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },

<<<<<<< HEAD
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
            axios.get('https://morpheus-api20.free.beeceptor.com/todos')
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
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'tag');
                })
        },
=======
        srcLoad(){
            axios.get('http://localhost:8080/morpheus/source')
            .then(response => {
                this.sourceFilters = [];
     
                response.data.forEach(portalNoticia => {

                    const itemAdd = new Object();
                    itemAdd.code = portalNoticia.code;
                    itemAdd.name = portalNoticia.srcName;
                    this.sourceFilters.push(itemAdd);
                });
           
            })
            .catch(error => {
                this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal');
            });

        },

>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5

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

<<<<<<< HEAD
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

        initChoicesAuthors(){
            const choicesAuthors = new Choices('#choices-select-authors', {
=======
        populateFilterPortal() {
            const srcNames = [...new Set(this.newsList.map(element => element.srcName))];
            this.filters.portal.items = srcNames;
        },

        initChoices() {
            const choices = new Choices('#choices-select', {
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
<<<<<<< HEAD
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
            const dataFilter = {
                textSearch: [],
                sourcesOrigin: this.filters.portal.selectItems.map(item => item.value),
                titleSearch: [],
                dateStart: this.filters.date.dateInit,
                dateEnd: this.filters.date.dateFinal,
                author: this.filters.authors.selectItems.map(item => item.value),
            };

            axios.post('https://morpheus-api20.free.beeceptor.com/todos', dataFilter)
                .then(response => {
                    const data = response.data;
                    this.filters.portal.selectItems = data.sourcesOrigin || this.filters.portal.selectItems;
                    this.filters.authors.selectItems = data.author || this.filters.authors.selectItems;
                    this.filters.date.dateInit = data.dateStart || this.filters.date.dateInit;
                    this.filters.date.dateFinal = data.dateEnds || this.filters.date.dateFinal;
      })
                .catch(error => {
                    console.error('Erro:', error);
                });

        }
    },
=======
                allowHTML: true
            });
        
            // Limpa as opções existentes e adiciona as novas opções ao seletor
            choices.clearStore();
            this.sourceFilters.forEach(item => {
                choices.setChoices([{ value: item.code, label: item.name }], 'value', 'label', false);
            });
        
            // Evento para capturar as opções selecionadas ou adicionadas
            choices.passedElement.element.addEventListener('change', (event) => {
                // Captura apenas os valores dos itens selecionados
                this.filters.portal.selectedItems = Array.from(event.target.selectedOptions).map(option => ({
                    value: option.value,
                    label: option.textContent
                }));
            });
    
    },
    },

    
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5

    computed: {
        filteredNews() {
            return this.newsFilter();
        }
    },


    mounted() {
    
        this.newsLoad();
<<<<<<< HEAD
        this.srcLoad();
        this.authorLoad();
        this.initDatePicker();


=======
>>>>>>> 4604398506257bb7deb9f9bc55ad94e31323a5d5
    },
});

app.mount('#app');
