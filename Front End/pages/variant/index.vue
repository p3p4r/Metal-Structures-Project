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
      <b-table striped hover :items="variants" :fields="fields" :current-page="currentPage" :per-page="perPage" :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               show-empty
               empty-text="There are no variants to show"
               responsive="sm">
      </b-table>

      <div class="justify-content-center row my-1">
        <b-pagination size="md" :total-rows="this.variants.length" :per-page="perPage" v-model="currentPage" />
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
      ],
      currentPage: 1,
      perPage: 5,
      filter: null,
      variants: [],
      readMore: false
    }
  },
  methods: {},
  created () {
    this.$axios.$get('/api/suppliers/variants')
      .then((data) => {
        //console.log(data)
        this.variants = data.filter(variants => variants.supplier == this.$auth.user.sub);
      })
      .catch((err) => {
        //this.$router.push('/auth/login')
      })
  }
}
</script>
<style>
</style>
