<template>
    <div>
        <div class="row">
            <div class="col-md-4 col-sm-12 col-bottom">
                <b-card-text>
                    <span><b>Campo 1</b></span>
                    <p>{{registro.campo1}}</p>
                </b-card-text>
            </div>
            <div class="col-md-4 col-sm-12 col-bottom">
                <b-card-text>
                    <span><b>Campo 2</b></span>
                    <p>{{registro.campo2}}</p>
                </b-card-text>
            </div>
            <div class="col-md-4 col-sm-12 col-bottom">
                <b-card-text>
                    <span><b>Campo 3</b></span>
                    <p>{{registro.campo3}}</p>
                </b-card-text>
            </div>
        </div>
    </div>
</template>

<script>

    import {mapState, } from 'vuex'

    export default {
        name: 'Detalle::name::',
        props: ['editable', 'registro'],
        data() {
            return {
                loadingSave: false,
            }
        },
        mounted() {
        },
        computed: {
            ...mapState([ 'relationSelected' ]),
        },
        methods: {
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
