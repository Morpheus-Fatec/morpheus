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

            sourceNews: {
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
                        type: 'warning',                         titleError: 'Erro!',
                        desc: 'Por favor, preencha todos os campos obrigatórios.'
                    }
            },

            filters:{
                portal:{
                    items: [],
                    selectItems: []
                },

                authors:{
                    items: [],
                    selectItems: []
                },

                date:{
                    start: " ",
                    end: " ",
                }
            },
            sourceFilters:[]

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
                            newsRegistryDate: element.newsRegistryDate  
                        };
                        this.newsList.push(itemAdd);
                    });
                    this.pagination.totalPages = data.totalPages;
                    this.pagination.totalElements = data.totalElements;
        
                    this.newsFilter();
                    this.initChoices();
                    this.populateFilterPortal();
                })
                .catch(error => {
                    this.newsMontedAlert('danger','Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde','Erro ao carregar notícias!');
                })
                .finally(() => {
                    this.isLoading = false;
                    });


        },

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


        newsFilter() {
            const query = this.sourceNews.search.query.toLowerCase();
            this.sourceNews.filtered = this.newsList
                .filter(news =>
                    news[this.sourceNews.search.field].toLowerCase().includes(query)
                )
                .sort((a, b) => {
                    const result = a['newsTitle'].toLowerCase().localeCompare(b['newsTitle'].toLowerCase());
                    return this.sourceNews.search.sort.order === 'asc' ? result : -result;
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
            }

            setTimeout(() => {
                this.root.formData.alert.show = false;
            }, 20000);
        },

        newsToggleSort(field) {
            this.sourceNews.search.sort.field = field;
            this.sourceNews.search.sort.order = this.sourceNews.search.sort.order === 'asc' ? 'desc' : 'asc';
            this.newsFilter();
        },

        populateFilterPortal() {
            const srcNames = [...new Set(this.newsList.map(element => element.srcName))];
            this.filters.portal.items = srcNames;
        },

        initChoices() {
            const choices = new Choices('#choices-select', {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
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

    

    computed: {
        filteredNews() {
            return this.newsFilter(); 
        }
    },


    mounted() {
        this.newsLoad();
    },
});

app.mount('#app');
