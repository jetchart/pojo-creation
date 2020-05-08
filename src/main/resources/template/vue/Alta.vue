<template>
    <div>
        <template>
            <div class="row">
                <div class="col-md-3 col-sm-12 col-bottom">
                    <b-card-text>
                        <span>Campo 1</span>
                        <b-form-input type="text" v-model="registroNew.campo1"></b-form-input>
                    </b-card-text>
                </div>
                <div class="col-md-3 col-sm-12 col-bottom">
                    <b-card-text>
                        <span>Campo 2</span>
                        <b-form-input type="text" v-model="registroNew.campo2"></b-form-input>
                    </b-card-text>
                </div>
                <div class="col-md-3 col-sm-12 col-bottom">
                    <b-card-text>
                        <span>Campo 3</span>
                        <b-form-input type="text" v-model="registroNew.campo3"></b-form-input>
                    </b-card-text>
                </div>
                <div class="col-md-3 col-sm-12 col-bottom">
                    <b-card-text>
                        <span>Campo 4</span>
                        <b-form-input type="text" v-model="registroNew.campo4"></b-form-input>
                    </b-card-text>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12" align="right">
                    <b-button v-show="editable" v-if="!loadingSave" class="btn-sm card-bottom" type="submit" variant="primary" @click="save();">Guardar</b-button>
                    <b-button v-show="editable" v-else class="btn-sm card-bottom loading"><i class="fa fa-spinner fa-spin"></i></b-button>
                    <b-button v-show="editable" class="btn-sm card-bottom" type="submit" variant="primary" @click="$emit('cancel')">Cancelar</b-button>
                </div>
            </div>
        </template>
    </div>
</template>

<script>

    import { ::name::Service } from '@/js/services/::name::Service';
    import {mapState, mapActions, mapMutations} from 'vuex'

    export default {
        name: 'Alta::name::',
        props: ['editable', 'registro'],
        data() {
            return {
                loadingSave: false,
                loading: false,
                selected: null,
                registroNew: {},
            }
        },
        mounted() {
            this.registroNew = Object.assign({}, this.registro);
        },
        computed: {
            ...mapState([ 'relationSelected' ]),
        },
        methods: {
            save() {
                this.loadingSave = true;
                let params = { cuit: this.relationSelected.id, };
                ::name::Service.baseActions().save$(this.registroNew, params)
                    .then(response => {
                        this.$emit('saved', response.data);
                        this.loadingSave = false;
                    })
                    .catch(error => this.loadingSave = false);
            },
        },
    }
</script>

<style scoped>
    .card-bottom {
    margin-bottom: 1.25rem;
    }

    .row-bottom {
    margin-bottom: 1rem;
    }

    .col-bottom {
    margin-bottom: 1rem;
    }
</style>
