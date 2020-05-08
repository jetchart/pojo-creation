<template>
    <div>
        <div class="row">
            <div class="col-md-4 col-sm-12 col-bottom">
                <b-card-text>
                    <span><b>Id</b></span>
                    <p>{{registro.id}}</p>
                </b-card-text>
            </div>
        </div>
    </div>
</template>

<script>

    import {mapState, mapActions, mapMutations} from 'vuex'

    export default {
        name: 'Ver::name::',
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