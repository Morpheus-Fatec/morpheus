const app = Vue.createApp({
    data() {
        return {
            api: {
                search: {
                    field: 'address',  
                    query: '',
                    sort: {
                        order: 'asc'
                    }
                },
            },

            modalSource: '',

            filters: {
                address: {
                    items: [],
                    selectItems: []
                },
                tags: {
                    items: [],
                    selectItems: []
                },
                date: {
                    dateRange: '',
                    dateWrite: '',
                    dateInit: '',
                    dateFinal: ''
                },
            },
            pagination: {
                totalElements: 0,
                totalPages: 0,
                page: 1,
                items: 50
            },
            root: {
                alert: {
                    show: false,
                    type: 'warning',
                    titleError: 'Erro!',
                    desc: ''
                }
            },
            isLoading: false,
            apiList: [],
            tagFilters: [],
            searchText: [],
            filteredApiList: [],
        }
    },

    methods: {
        apiLoad() {
            this.isLoading = true;
            axios.get(`https://morpheus-api37.free.beeceptor.com/todos?page=${this.pagination.page}&itens=${this.pagination.items}`)
                .then(response => {
                    const data = response.data;
                    this.apiList = [];
                    data.forEach(element => {
                        const itemAdd = {
                            code: element.code,
                            address: element.address,
                            source: element.source,
                            method: element.method
                        };
                        this.apiList.push(itemAdd);
                    });
                    this.apiFilter();
                    this.pagination.totalPages = data.totalPages;
                    this.pagination.totalElements = data.totalElements;
                    this.initAddress();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal')
                })
                .finally(() => {
                    this.isLoading = false;
                });
        },

        changePage(page) {
            if (page < 1 || page > this.pagination.totalPages) return;
            this.pagination.page = page;
            this.apiLoad();
        },

        tagsLoad() {
            this.tagFilters = [];
            axios.get('http://localhost:8080/morpheus/tag')
                .then(response => {
                    this.tagFilters = response.data.map(tag => ({
                        tagCode: tag.tagCode,
                        tagName: tag.tagName
                    }));
                    this.initTags();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'tag');
                })
        },

        apiUpdateSearch() {
            this.apiFilter();
        },

        _apiFilter() {
            const query = this.api.search.query.toLowerCase();
            this.filteredApiList = this.apiList.filter(item =>
                item.address.toLowerCase().includes(query)
            );
        },

        apiFilter() {
            const query = this.api.search.query.toLowerCase();
            if (query === '') {
                this.filteredApiList = this.apiList;
            } else {
                this.filteredApiList = this.apiList
                    .filter(api =>
                        api['address'].toLowerCase().includes(query)
                    );
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

        openModal(source) {
            this.modalSource = source.replace(/\\/g, "");
            const modal = new bootstrap.Modal(document.getElementById('contentModal'));
            modal.show();
        },

        closeModal() {
            const modalElement = document.getElementById('contentModal');
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal.hide();
        },

        formatSource() {
            this.modalSource = this.formatContent(this.modalSource); 
        },
        
        initAddress() {
            const element = document.querySelector('#choices-address');
            if (!element) return;

            const choicesAddress = new Choices(element, {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });

            choicesAddress.clearStore();

            this.apiList.forEach(item => {
                choicesAddress.setChoices([{ value: item.code, label: item.address }], 'value', 'label', false);
            });

            choicesAddress.passedElement.element.addEventListener('change', (event) => {
                this.filters.address.selectItems = Array.from(event.target.selectedOptions).map(option => ({
                    value: option.value,
                    label: option.textContent
                }));
            });
        },

        initTags() {
            const choicesTags = new Choices('#choices-tags', {
                removeItemButton: true,
                addItems: true,
                duplicateItemsAllowed: false,
                paste: true,
                editItems: true,
                allowHTML: true,
            });
            choicesTags.clearStore();
            this.tagFilters.forEach(item => {
                choicesTags.setChoices([{ value: item.tagCode, label: item.tagName }], 'value', 'label', false);
            });

            choicesTags.passedElement.element.addEventListener('change', (event) => {
                this.filters.tags.selectItems = Array.from(event.target.selectedOptions).map(option => option.textContent);
            });
        },

        insertionText() {
            const choicesText = new Choices('#choices-text-remove-button', {
                removeItemButton: true,
                delimiter: ',',
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

        filtersData() {
            const dataFilter = {
                address: this.filters.address.selectItems.map(item => parseInt(item.value, 10)),
                text: this.searchText,
                tags: this.filters.tags.selectItems,
                dateStart: this.filters.date.dateInit,
                dateEnd: this.filters.date.dateFinal
            };
            axios.post('https://morpheus-api37.free.beeceptor.com/todos', dataFilter)
                .then(response => {
                    const data = response.data;
                    this.apiList = [];
                    data.forEach(element => {
                        const itemAdd = {
                            code: element.code,
                            address: element.address,
                            source: element.source,
                            method: element.method
                        };
                        this.apiList.push(itemAdd);
                    });
                    this.pagination.totalPages = data.totalPages;
                    this.pagination.totalElements = data.totalElements;
                    this.initAddress();
                })
                .catch(error => {
                    this.rootMontedAlert('danger', 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados.')
                });
            const offcanvasElement = document.getElementById('offcanvasWithBothOptions');
            const offcanvasInstance = bootstrap.Offcanvas.getInstance(offcanvasElement);

            if (offcanvasInstance) {
                offcanvasInstance.hide();
            }
        },

        filteredAddresses() {
            const query = this.api.search.query?.toLowerCase() || "";
            return this.apiList.filter(item => item.address?.toLowerCase().includes(query));
        },

        startUserGuideAPI(guide) {
            const vm = this;
            let stepsGuide = [];
        
            if (guide === 'main') {
                stepsGuide = [
                    {
                        element: '#titleAPI',
                        intro: 'Aqui você pode ver o título da seção de APIs.',
                        position: 'bottom'
                    },
                    {
                        element: '#searchElementsAPI',
                        intro: 'Aqui você pode pesquisar por APIs.',
                        position: 'bottom'
                    },
                    {
                        element: '#btnFilterAPI',
                        intro: 'Clique aqui para abrir os filtros de pesquisa.',
                        position: 'right'
                    },
                    {
                        element: '#allAPIs',
                        intro: 'Aqui estão listadas todas as APIs.',
                        position: 'top'
                    },
                    {
                        element: '#spanApiMethod',
                        intro: 'Aqui você pode ver o método da API (GET, POST, etc.).',
                        position: 'top'
                    },
                    {
                        element: '#linkApiAddress',
                        intro: 'Aqui você pode ver o endereço da API.',
                        position: 'top'
                    },
                    {
                        element: '#btnViewContentAPI',
                        intro: 'Clique aqui para ver o conteúdo da API.',
                        position: 'left'
                    }
                ];
            }
        
            introJs().setOptions({
                steps: stepsGuide,
                nextLabel: 'Próximo',
                prevLabel: 'Anterior',
                skipLabel: '<i class="fa fa-times"></i>',
                doneLabel: 'Concluir'
            })
            .onchange(async function(element) {
                // Adicione aqui eventos específicos para cada passo, se necessário
            })
            .onchange(async function(element) {
                const stepIndex = this._currentStep;
                if (guide === 'main') {
                    if (vm.apiList.length === 0) {
                        if (stepIndex === 4) {
                            const apiMethodElement = document.querySelector('#spanApiMethod');
                            if (apiMethodElement) {
                                apiMethodElement.innerText = 'Método';
                            }
                        }
                        if (stepIndex === 5) {
                            const apiAddressElement = document.querySelector('#linkApiAddress');
                            if (apiAddressElement) {
                                apiAddressElement.innerText = 'Endereço';
                            }
                        }
                        if (stepIndex === 6) {
                            const apiContentElement = document.querySelector('#btnViewContentAPI');
                            if (apiContentElement) {
                                apiContentElement.innerText = 'Conteúdo';
                            }
                        }
                    }
                }
            })
            .start();
        }
    },
    mounted() {
        this.apiLoad();
        this.tagsLoad();
        this.insertionText();
        this.initDatePicker();
        this.filteredAddresses();
    },
});

app.mount('#api');