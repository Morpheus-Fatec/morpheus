const app = Vue.createApp({
    data() {
        return {
            api: {
                search: {
                    field: 'newsTitle',  // Ou qualquer valor padrão desejado
                    query: '',
                    sort: {
                        order: 'asc'
                    }
                },
            },

            modalSource: {
                source: '',
            },

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
            


        }
    },

    methods: {

        apiLoad() {
            this.isLoading = true;
            axios.get(`https://morpheus-api35.free.beeceptor.com/todos?page=${this.pagination.page}&itens=${this.pagination.items}`)
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
                    this.apiFilter();
                })
                .catch(error => {
                    this.rootMontedAlert('danger' , 'Alguma indisponibilidade ocorreu no sistema. Tente novamente mais tarde', 'Não foi possível carregar os dados do portal')
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

        apiFilter() {
            const query = this.api.search.query.toLowerCase();
            this.api.filtered = this.apiList
                .filter(api =>
                    api[this.api.search.field]?.toLowerCase().includes(query)
                )
                .sort((a, b) => {
                    const result = a['newsTitle']?.toLowerCase().localeCompare(b['newsTitle']?.toLowerCase());
                    return this.api.search.sort.order === 'asc' ? result : -result;
                });
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
            this.modalSource.source = source;
            const modal = new bootstrap.Modal(document.getElementById('contentModal'));
            modal.show();

        },

        closeModal() {
            const modalElement = document.getElementById('contentModal');
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal.hide();
        },
    
        formatSource() {
            this.modalSource = this.formatContent(this.modalSource); // Formata o conteúdo ao clicar
        },
        // Função para formatar o conteúdo dependendo de ser JSON ou XML
        formatContent(content) {
            // Se for JSON, formate com JSON.stringify
            if (this.isValidJSON(content)) {
                return this.formatJSON(content);
            }
            // Se for XML, formate com a função de XML
            else if (this.isValidXML(content)) {
                return this.formatXML(content);
            }
            // Caso não seja JSON nem XML, apenas retorne o conteúdo
            return content;
        },
        // Função para formatar JSON
        formatJSON(json) {
            try {
                return JSON.stringify(JSON.parse(json), null, 2); // Indentação de 2 espaços
            } catch (e) {
                return "Erro ao formatar JSON";
            }
        },
        // Função para formatar XML
        formatXML(xml) {
            let formatted = '';
            let reg = /(>)\s*(<)(\/*)/g;
            xml = xml.replace(reg, '$1\n$2$3');
            let pad = 0;
            xml.split('\n').forEach(function(node) {
                let indent = 0;
                if (node.match(/.+<\/\w[^>]*>$/)) {
                    indent = 0;
                } else if (node.match(/^<\/\w/)) {
                    if (pad > 0) {
                        pad--;
                    }
                } else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
                    indent = 1;
                } else {
                    indent = 0;
                }

                formatted += new Array(pad + 1).join('  ') + node + '\n';
                pad += indent;
            });
            return formatted;
        },
        // Função para verificar se o conteúdo é um JSON válido
        isValidJSON(str) {
            try {
                JSON.parse(str);
                return true;
            } catch (e) {
                return false;
            }
        },
        // Função para verificar se o conteúdo é um XML válido
        isValidXML(str) {
            try {
                let parser = new DOMParser();
                let xmlDoc = parser.parseFromString(str, "text/xml");
                return xmlDoc.getElementsByTagName("parsererror").length === 0;
            } catch (e) {
                return false;
            }
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
                this.filters.address.selectItems = Array.from(event.target.selectedOptions).map(option => option.textContent);
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
                console.log(this.searchText);
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
                address: this.filters.address.selectItems,
                text: this.searchText,
                tags: this.filters.tags.selectItems,
                dateStart: this.filters.date.dateInit,
                dateEnd: this.filters.date.dateFinal
            };
            axios.post('https://morpheus-api35.free.beeceptor.com/todos', dataFilter)
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
                    console.error('Erro:', error);
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

