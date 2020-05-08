<template>
    <div>
		<h4>::name::</h4><hr>
        <b-card body-class="text-left" header-tag="nav" class="card-bottom">
            <component v-bind:is="component" 
            :registro="registro" 
            :editable="editable" 
            @edit="edit($event)"
            @cancel="cancel()"
            @saved="applySaved($event)"
            @sendTotalRegistros="sendTotalRegistros($event)"
            ></component>
        </b-card>
        <!-- Modal guardado -->
        <b-modal ref="modalSuccess" title="Registro guardado" centered ok-only ok-title="Cerrar">
            El registro ha sido guardado correctamente
        </b-modal>
    </div>
</template>

<script>

    import {mapState, mapActions, mapMutations} from 'vuex';
    import Alta::name:: from "@/components/::name_lower::/Alta::name::";
    import Lista::name:: from "@/components/::name_lower::/Lista::name::";

    export default {
        name: 'Container::name::',
        props: ['editable'],
        components: {Lista::name::, Alta::name::, },
        data() {
            return {
                component: 'Lista::name::',
                loadingSave: false,
                selected: null,
                registro: {},
            }
        },
        mounted() {
        },
        computed: {
            ...mapState([ 'relationSelected' ]),
        },
        methods: {
            cancel() {
                this.component = 'Lista::name::';
            },
            edit(registro) {
                this.registro = registro;
                this.component = 'Alta::name::';
            },
            applySaved() {
                this.$refs.modalSuccess.show();
                this.component = 'Lista::name::';
            },
            sendTotalRegistros(totalRegistros) {
                this.$emit('sendTotalRegistros', totalRegistros);
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
