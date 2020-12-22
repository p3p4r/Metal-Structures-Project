<template>
  <div>
    <b-container>

      <div class="col-12 text-right pb-4 pt-2">
        <nuxt-link to="/" style="float:left"><b-icon icon="chevron-left" /> Back</nuxt-link>
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
      <b-table striped hover :items="adjudications" :fields="fields" :current-page="currentPage" :per-page="perPage" :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               show-empty
               empty-text="There are no materials adjudicated to show"
               responsive="sm">
        <template v-slot:cell(observation)="row">
          <span>{{ row.item.observation.substring(0,20)}}
            <span v-if="readMore"></span>
            <span v-else>...</span>
          </span>
          <span v-show="readMore">{{ row.item.observation.substring(20) }}</span>
          <a class="ui-icon-link" @click="readMore=!readMore">
            <span v-if="readMore">Show Less</span>
            <span v-else>Show More</span>
          </a>
        </template>
        <template v-slot:cell(actions)="row">
          <span>-</span>
        </template>
      </b-table>

      <div class="justify-content-center row my-1">
        <b-pagination size="md" :total-rows="this.adjudications.length" :per-page="perPage" v-model="currentPage" />
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
        { key: 'adjudicate', sortable: true },
        { key: 'client', sortable: true },
        { key: 'structure', sortable: true },
        { key: 'variantes[0].material', label: 'Secção Material', sortable: true },
        { key: 'variantes[0].nome', label: 'Nome', sortable: true },
        { key: 'observation', sortable: true },
        { key: 'actions'}
      ],
      currentPage: 1,
      perPage: 5,
      filter: null,
      adjudications: [],
      readMore: false
    }
  },
  methods: {},
  created () {
    this.$axios.$get('/api/suppliers/adjudications')
      .then((data) => {
        this.adjudications = data.filter(materials => materials.adjudicate == "Adjudicated");
      })
      .catch((err) => {
        //this.$router.push('/auth/login')
      })
  }
}
</script>
<style>
</style>
