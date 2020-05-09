<template>
<div>
    <div class="row v-align">
		<!-- Búsqueda -->
		<div class="col-md-2 mg-bottom align="left">
            <h5 class="tacum inlineb">Filtro</h5>
            <b-input v-model="filtro"></b-input>
		</div>
        <!-- Agregar/Eliminar -->
        <div class="col mg-bottom" align="right">
            <b-button class="fa fa-trash btn-danger" @click="showModalEliminarTodo()"></b-button>
            <b-button class="fa fa-plus btn-success" @click="crear()"></b-button>
        </div>
    </div>
    <!-- Tabla -->
    <div class="row">
      <div class="col">
        <div class="table-responsive">
          <table class="table table-striped">
            <thead>
              <tr class="text-center">
                <th>Campo 1</th>
                <th>Campo 2</th>
                <th>Campo 3</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="totalRegistros > 0 && !loading">
                <tr v-for="(registro, index) in registros" :key="index">
                  <td class="text-center">{{registro.campo1}}</td>
                  <td class="text-center">{{registro.campo2}}</td>
                  <td class="text-center">{{registro.campo3}}</td>
                  <td class="text-center">
                    <button class="btn fa fa-align-justify pointer" @click="verDetalle(registro)"></button>
                    <button class="btn fa fa-edit pointer" @click="editar(registro)"></button>
                    <button class="btn fa fa-trash pointer" @click="showModalEliminar(registro)"></button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
        <!-- Sin registros -->
      <template v-if="!totalRegistros > 0 && !loading">
        <div class="empty-row">
          <small>No se encontraron registros</small>
        </div>
      </template>
      <!-- Spinner cargando -->
      <template v-if="loading" class="loadingRegistros">
        <div class="empty-row">
          <i class="fas fa-spinner fa-spin"></i>
        </div>
      </template>
      <!-- Paginator -->
      <b-pagination
        class="pagTop"
        align="center"
        size="md"
        :total-rows="totalRegistros"
        v-model="currentPage"
        :per-page="perPage"
      ></b-pagination>
      </div>
    </div>
    <!-- Modal Detalle -->
    <b-modal ref="modalDetalle" title="Detalle" centered ok-only ok-title="Cerrar" size="lg">
        <Detalle::name:: :registro="registroSelected"></Detalle::name::>
    </b-modal>
    <!-- Modal eliminar un registro -->
    <b-modal centered ref="modalEliminar" title="Eliminar registro" @ok="eliminar()">
        ¿Desea eliminar el registro?
    </b-modal>
    <!-- Modal eliminar todo -->
    <b-modal centered ref="modalEliminarTodo" title="Eliminar todo" @ok="deleteAll()">
        ¿Desea eliminar todos los registros?
    </b-modal>
</div>

</template>

<script>

    import { ::name::Service } from '@/js/services/::name::Service';
    import {mapState, mapActions, mapMutations} from 'vuex'
    import Detalle::name:: from "@/components/::name_lower::/Detalle::name::";

    export default {
        name: 'Lista::name::',
        props: ['editable'],
        data() {
            return {
				filtro: null,
                registros: [],
                loading: false,
                registroSelected: {},
                perPage: 10,
                currentPage: 1,
                totalRegistros: 0,
                numRows: 30,
            }
        },
        components: { Detalle::name::, },
        mounted() {
            this.lists();
        },
        computed: {
            ...mapState([ 'relationSelected' ]),
        },
        methods: {
            crear() {
            this.$emit('edit', { operacion: {} });
            },
            showModalEliminar(registro) {
              this.registroSelected = registro;
              this.$refs.modalEliminar.show();
            },
            showModalEliminarTodo() {
              this.$refs.modalEliminarTodo.show();
            },
            editar(registro) {
              this.$emit('edit', registro);
            },
            verDetalle(registro) {
              this.registroSelected = registro;
              this.$refs.modaDetalle.show();
            },
            deleteAll() {
              this.loading = true;
              const params = { cuit: this.relationSelected.id, filtro: this.filtro};
              ::name::Service.baseActions().deleteAll$(params)
                .then(response => {
                  this.loading = false;
                  this.lists();
                })
                .catch(error => {
                    this.loading = false;
                });
            },
            eliminar() {
              this.loading = true;
              const params = { cuit: this.relationSelected.id, };
              ::name::Service.baseActions().deleteById$(this.registroSelected.id, params)
                .then(response => {
                  this.loading = false;
                  this.lists();
                })
                .catch(error => {
                    this.loading = false;
                });
            },
            lists() {
                this.loading = true;
                const params = { filtro: this.filtro, 
                    cuit: this.relationSelected.id, 
                    page: this.currentPage - 1, 
                    size: this.perPage, };
                ::name::Service.baseActions().list$(params)
                    .then(response => {
                        this.registros = response.data.content;
                        this.totalRegistros = response.data.totalElements;
                        this.$emit('sendTotalRegistros', this.totalRegistros);
                        this.loading = false;
                    })
                    .catch(error => {
                        this.loading = false;
                    });
            },
        },
        watch: {
            currentPage() {
                this.lists();
            }
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

.empty-row {
    margin: 10px;
    text-align: center;
}

.mg-bottom {
  margin-bottom: 12px;
}

.mg-bottom {
  margin-bottom: 12px;
}

.mg-top {
  margin-top: 20px;
}

.inlineb{
  display: inline-block;
}

.tacum{
  text-transform: uppercase;
  font-weight: 500;
  font-size: 10px;
}

.pointer {
  cursor: pointer;
}

.v-align {
    align-items: center;
}


</style>
