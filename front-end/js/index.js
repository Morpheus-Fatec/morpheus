const app = Vue.createApp({
    data() {
        return {
            newsList: [],
            newsSelected: null,
            delete: {
                news: {
                    name: null,
                    code: null
                },
                modal: null
            },
            managerTags: {
                insert: false,
                allTags: [],
                filtered: [],
                modal: null,
                search: '',
                sort: {
                    order: 'asc'
                }
            },
            tags: {
                insertTag: '',
                search: {
                    available: null,
                    selected: null,
                },
                modal: null,
                available: [],
                selected: [],
                movedAdd: [],
                movedRemove: []
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
            created: {
                create: true,
                mapInsert: false,
                mapTag: false,
                modal: null,
                news: {
                    code: null,
                    name: null,
                    address: null,
                    map: {
                        author: null,
                        body: null,
                        title: null,
                        url: null
                    },
                    mapTag: {
                        author: null,
                        body: null,
                        title: null
                    }
                }
            },
            sort: {
                field: 'name',
                order: 'asc'
            },
            filteredNews: [],
            searchField: 'name',
            searchQuery: '',
            isVisible: true,
            selectedTime: '12:00',
            currentEditingTag: null
        };
    },
    methods: {
        loadNews() {
            
        },
        toggleVisibility() {
            this.isVisible = !this.isVisible; // Alterna a visibilidade
        },
        updateSearch() {
            this.filterNews();
        },
        filterNews() {
            const query = this.searchQuery.toLowerCase();
            this.filteredNews = this.newsList
                .filter(news =>
                    news[this.searchField].toLowerCase().includes(query)
                )
                .sort((a, b) => {
                    const result = a[this.sort.field].toLowerCase().localeCompare(b[this.sort.field].toLowerCase());
                    return this.sort.order === 'asc' ? result : -result;
                });
        },
        filterTags() {
            const query = this.managerTags.search.toLowerCase();
            this.managerTags.filtered = this.managerTags.allTags
                .filter(tag => tag.tagName.toLowerCase().includes(query))
                .sort((a, b) => {
                    const result = a.tagName.toLowerCase().localeCompare(b.tagName.toLowerCase());
                    return this.managerTags.sort.order === 'asc' ? result : -result;
                });
        },
        toggleSort(field) {
            this.sort.field = field;
            this.sort.order = this.sort.order === 'asc' ? 'desc' : 'asc';
            this.filterNews();
        },
        showTags(news) {
            this.created.news = news;
            const modalElement = this.$refs.tagsModal;
            this.tags.modal = new bootstrap.Modal(modalElement);
            this.tags.modal.show();
            this.tags.selected = news.tags;
        },
        createdNews() {
            this.created.create = true;
            this.enabledMap();
            const modalElement = this.$refs.createModal;
            this.created.modal = new bootstrap.Modal(modalElement);
            this.created.modal.show();
        },
        enabledMap() {
            this.created.mapInsert = true;
            this.created.mapTag = false;
        },
        enabledEditTags() {
            this.created.mapTag = true;
            this.created.mapInsert = false;
        },
        editNews(news) {
            news.map = {
                author: null,
                body: null,
                title: null,
                url: null
            }
            news.mapTag = {
                author: null,
                body: null,
                title: null
            }
            this.enabledMap();
            this.created.create = false;
            this.created.news = news;
            const modalElement = this.$refs.createModal;
            this.created.modal = new bootstrap.Modal(modalElement);
            this.created.modal.show();
        },
        deleteNews(news) {
            this.delete.news = news;
            const modalElement = this.$refs.deleteModal;
            this.delete.modal = new bootstrap.Modal(modalElement);
            this.delete.modal.show();
        },
        confirmCreateNews() {
            this.created.modal.hide();
        },
        confirmDeleteNews() {
            this.delete.modal.hide();
        },
        getTags() {
            this.tags.available = [];

            this.managerTags.allTags = this.tags.available;
        },
        addOption() {
            this.tags.selected.push(...this.tags.movedAdd);
            this.tags.movedAdd = [];
        },
        deleteOption() {
            this.tags.selected = this.tags.selected.filter(tagCode => !this.tags.movedRemove.includes(tagCode));
            this.tags.movedRemove = [];
        },
        saveTags() {
            this.created.news.tags = this.tags.selected;
            this.saveNews('edited');
        },
        saveNews(action) {
            console.log(this.created.news);
        },
        createTag() {
            const insertTag = this.tags.search.available;
            this.getTags();
        },
        managerTagsOpen() {
            const modalElement = this.$refs.tagManangerModal;
            this.managerTags.modal = new bootstrap.Modal(modalElement);
            this.managerTags.modal.show();
            this.filterTags();
        },
        editTag(tag) {
            tag.isEditing = !tag.isEditing;
        },
        saveTag(tag) {
            tag.isEditing = false;
        },
        addTag() {
            this.managerTags.insertTag = "";
        },
        regionalismOpen() {
            const modalElement = this.$refs.regionalismModal;
            this.regionalism.modal = new bootstrap.Modal(modalElement);
            this.regionalism.modal.show();
            this.getWordsRegionalism();
        },
        getWordsRegionalism() {
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
            });
        },
        addWord() {
            if(this.regionalism.insertWord === ""){
                this.alertRegionalism("Preencha o conteúdo", "danger");
                return false;
            }
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
            });
        },
        cancelDeleteWord(){
            this.regionalism.modal.show();
            this.regionalism.delete.modal.hide();
        }
    },
    computed: {
        selectedTags() {
            return this.tags.available
                .filter(tag => this.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.selected ||
                        tag.tagName.toLowerCase().includes(this.tags.search.selected.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        },
        unselectedTags() {
            return this.tags.available
                .filter(tag => !this.tags.selected.includes(tag.tagCode))
                .filter(tag => {
                    return !this.tags.search.available ||
                        tag.tagName.toLowerCase().includes(this.tags.search.available.toLowerCase());
                })
                .sort((a, b) => a.tagName.localeCompare(b.tagName));
        }
    },
    mounted() {
        this.getTags();
        this.loadNews();
        this.filterNews();
    }
});

app.mount('.container');