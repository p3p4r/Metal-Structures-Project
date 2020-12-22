<template>
  <div>
    <b-container>

      <div class="col-12 text-right pb-4 pt-2">
        <nuxt-link to="/" style="float:left"><b-icon icon="chevron-left"></b-icon> Back</nuxt-link>
        <b>Shortcuts: </b>
        <b-button to="/file/upload" variant="light" size="sm">Upload file <b-icon class="ml-1"  icon="cloud-upload-fill" /></b-button>
        <b-button variant="light" to="/project/create" size="sm">Create Project <b-icon class="ml-1"  icon="folder-fill" /></b-button>
        <b-button to="/structure/create" variant="light" size="sm">Create Structure <b-icon class="ml-1"  icon="nut-fill" /></b-button>
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
      <b-table striped hover :items="projects" :fields="fields" :current-page="currentPage" :per-page="perPage" :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               responsive="sm">
        <template v-slot:cell(actions)="row">
          <b-dropdown split :split-to="`/project/${row.item.name}`" text="Details" class="m-2">
            <b-dropdown-item v-b-modal.modal-delete-item @click.prevent="sendInfo(row.item.name,row.index)">Remove</b-dropdown-item>
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
        <b-pagination size="md" :total-rows="this.projects.length" :per-page="perPage" v-model="currentPage" />
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  data () {
    middleware: 'auth' // only allowed users can acesss
    return {
      sortBy: 'name',
      sortDesc: false,
      fields: [
        { key: 'name', sortable: true },
        { key: 'clientUsername', sortable: true },
        { key: 'draftsmanUsername', sortable: true },
        { key: 'actions'}
        ],
      currentPage: 1,
      perPage: 5,
      filter: null,
      projects: [],
      deletedItem: '',
    }
  },
  methods: {
    sendInfo(item) {
      this.deletedItem = item;
    },
    deleteItem(item) {
      this.$axios.$delete(`/api/draftsmen/projects/${item}`)
        .then((data) => {
          //console.log(data)
          let idx = this.projects.findIndex(i => i.name === item)
          this.projects.splice(idx,1)
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
    this.$axios.$get(`/api/draftsmen/${this.$auth.user.sub}`)
      .then((data) => {
        this.projects = data
      })
    this.$axios.$get('/api/structures')
      .then((data) => {
        this.structures = data
      })
    .catch((err) => {
      this.projects = err;
      this.$router.push('/auth/login')
    })
  }
}
</script>
<style>
</style>
