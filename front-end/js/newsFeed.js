const app = Vue.createApp({
    data() {
        return {
            newsList: [],
            pagination: {
                totalElements: 0,
                totalPages: 0,
                page: 1,
                itens: 50
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
                    field: 'srcName',
                    query: ''
                }
            }
        };
    },

    methods: {
        newsLoad() {
            axios.get(`https://morpheus-api15.free.beeceptor.com/todos?page=${this.pagination.page}&items=${this.pagination.itens}`)
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
                })
                .catch(error => {
                    console.error('Erro ao carregar as notícias', error);
                });
        },

        newsFilter() {
            const query = this.sourceNews.search.query.toLowerCase();
            const field = this.sourceNews.search.field;

            return this.newsList.filter(news => {
                if (field === 'name') {
                    return news.srcName.toLowerCase().includes(query);
                } else if (field === 'address') {
                    return news.scrAddress.toLowerCase().includes(query);
                }
                return true;
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
        }
    
    },

    computed: {
        filteredNews() {
            return this.newsFilter(); 
        }
    },

    mounted() {
        this.newsLoad();
    }
});

app.mount('#app');
