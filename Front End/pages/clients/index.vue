<template>
  <div>
    <b-container>
      <div class="col-12 text-left pb-4 pt-2">
        <nuxt-link to="/"><b-icon icon="chevron-left"></b-icon> Back</nuxt-link>
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
        <b-button :to="`/project/${row.item.name}`"  class="m-2">Details</b-button>
      </template>
    </b-table>
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

  },

  created () {
    //console.log(this.$auth.user.sub)
    //console.log(`/api/clients/${this.username}`)
    this.$axios.$get(`/api/clients/${this.$auth.user.sub}`)
      .then((data) => {
        this.projects = data.filter(projects => projects.status != 'DRAFT')
        this.materials = data.filter(materials => materials.status == "visible")
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
