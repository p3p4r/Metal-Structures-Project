<template>
  <div>
    <b-container>

      <div class="col-12 text-right pb-4 pt-2">
        <nuxt-link to="/" style="float:left"><b-icon icon="chevron-left" /> Back</nuxt-link>
        <b>Shortcuts: </b>
        <b-button variant="light" size="sm" to="material/create" >Create Material <b-icon class="ml-1" icon="box" /></b-button>
        <b-button variant="light" size="sm" to="variant/create" >Create Variant <b-icon class="ml-1" icon="grid" /></b-button>
        <b-button to="/adjudication" size="sm" variant="light">Adjudicated Materials <b-icon class="ml-1" icon="eye-fill" /></b-button>
        <b-button to="/variant" size="sm" variant="light">All Variants <b-icon class="ml-1" icon="eye-fill" /></b-button>
        <hr>
      </div>

      <div class="justify-content-centermy-1 row">
        <b-form-fieldset horizontal label="Filter" class="col-10">
          <b-form-input v-model="filter" placeholder="Search..."></b-form-input>
        </b-form-fieldset>

        <b-form-fieldset horizontal label="Rows per page" class="col-2">
          <b-form-select :options="[{text:5,value:5},{text:10,value:10},{text:15,value:15}]" v-model="perPage">
          </b-form-select>
        </b-form-fieldset>
      </div>
      <b-table striped hover :items="materials" :fields="fields" :current-page="currentPage" :per-page="perPage" :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               responsive="sm">
        <template v-slot:cell(actions)="row">
          <b-dropdown split :split-to="`/material/${row.item.id}`" text="Details" class="m-2">
            <b-dropdown-item v-b-modal.modal-delete-item @click.prevent="sendInfo(row.item.id,row.index)">Remove</b-dropdown-item>
          </b-dropdown>
        </template>
      </b-table>

      <!-- modal to Delete -->
      <b-modal id="modal-delete-item" centered title="Confirm Delete?">
        <p class="my-4">Are you sure you want to delete {{deletedItem}}?</p>
        <template #modal-footer="{ Confirm, cancel }">
          <b-button size="sm" variant="secondary" @click="cancel()">
            Cancel
          </b-button>
          <b-button size="sm" variant="primary" @click="deleteItem(deletedItem)">
            Confirm
          </b-button>
        </template>
      </b-modal>

      <div class="justify-content-center row my-1">
        <b-pagination size="md" :total-rows="this.materials.length" :per-page="perPage" v-model="currentPage" />
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'auth', // only allowed users can acesss
  data () {
    return {
      sortBy: 'name',
      sortDesc: false,
      fields: [
        { key: 'name', sortable: true },
        { key: 'family', sortable: true },
        { key: 'type', sortable: true },
        { key: 'actions'}
      ],
      currentPage: 1,
      perPage: 5,
      filter: null,
      materials: [],
      deletedItem: '',
    }
  },
  methods: {
    sendInfo(item) {
      this.deletedItem = item;
    },
    deleteItem(item) {
      this.$axios.$delete(`/api/suppliers/materials/${item}`)
        .then(() => {
          let idx = this.materials.findIndex(i => i.name === item)
          this.materials.splice(idx,1)
          this.$toast.success('Row deleted successfully!',{ duration: 2000 })
        })
        .catch((err) => {
          console.log(err);
          this.$toast.error('Unable to Delete Item!', { duration: 2000 })
        })

      this.$bvModal.hide('modal-delete-item')
    }
  },
  created () {
    this.$axios.$get('/api/suppliers/materials')
      .then((data) => {
        this.materials = data.filter(materials => materials.status == "visible")
      })
      .catch((err) => {
        this.materials = err;
        this.$router.push('/auth/login')
      })
  }
}
</script>
<style>
</style>
