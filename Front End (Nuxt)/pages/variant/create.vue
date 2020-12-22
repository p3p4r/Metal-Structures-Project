<template>
  <b-container>
    <nuxt-link to="/suppliers">
      <b-icon icon="chevron-left"></b-icon> Back
    </nuxt-link>
    <h1>Create new Variant</h1>
    <div class="text-right col-lg-12">
      <b-button @click.prevent="create" variant="info" :disabled="!isFormValid"> Create</b-button>
      <!--  <button @click.prevent="create" :disabled="!isFormValid">CREATE</button> -->
    </div>
    <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
    <form @submit.prevent="create" :disabled="!isFormValid" class="mt-5">
      <b-container fluid>
        <b-row class="my-1">
          <b-col sm="2">
            <label for="name">Codigo</label>
          </b-col>
          <b-col sm="10">
            <b-form-group
              id="codigo">
              <b-input
                id="codigo"
                type="number"
                v-model.trim="codigo"
                placeholder="4FR4645"
                required
                trim></b-input>
            </b-form-group></b-col>
        </b-row>

        <b-row class="my-1">
          <b-col sm="2">
            <label for="name">Name</label>
          </b-col>
          <b-col sm="10">
            <b-form-group
              id="name"
              description="*required"
              :invalid-feedback="invalidMaterialNameFeedback"
              :state="isNameValid">
              <b-input
                id="name"
                v-model.trim="name"
                placeholder="Material X"
                :state="isNameValid"
                required
                trim></b-input>
            </b-form-group></b-col>
        </b-row>

        <b-row class="my-1">
          <b-col sm="2"><label>Materials</label></b-col>
          <b-col sm="10">
            <b-form-group>
              <b-select
                id="family"
                v-model="materialSelected"
                :options="materials"
                :state="isMaterialValid"
                required
                value-field="id"
                text-field="name">
                <template v-slot:first>
                  <option :value="null" disabled>-- Please Pick a Material --
                  </option>
                </template>
              </b-select>
            </b-form-group>
          </b-col>
        </b-row>

        <b-row class="my-1">

          <b-col>
              <b-form-group
                label="Weff P"
                description="*required">
                <b-input
                  v-model.trim="weff_p"
                  type="number"
                  required
                  trim></b-input>
              </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              label="Weff N"
              description="*required">
              <b-input
                v-model.trim="weff_n"
                type="number"
                required
                trim></b-input>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              label="Ar"
              description="*required">
              <b-input
                v-model.trim="ar"
                type="number"
                required
                trim></b-input>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              label="Sigma C"
              description="*required">
              <b-input
                v-model.trim="sigmaC"
                type="number"
                required
                trim></b-input>
            </b-form-group>
          </b-col>
        </b-row>

      </b-container>
    </form>
  </b-container>
</template>


<style>
#family{ text-transform: uppercase; }
</style>
<script>
export default {
  middleware: 'auth',
  data () {
    return {
      codigo: null,
      name: null,
      weff_p: null,
      weff_n: null,
      ar: null,
      sigmaC: null,
      materialSelected: null,
      materials: [],
      errorMsg: false,
    }
  },
  computed: {
    invalidMaterialNameFeedback () {
      if (!this.name) {
        return null
      }
      const nameLen = this.name.length
      if (nameLen < 3 || nameLen > 50) {
        return 'The name must be between [3, 50] characters.'
      }
      return ''
    },
    isNameValid () {
      if (!this.name) {
        return null
      }
      const nameLen = this.name.length
      if (nameLen < 3 || nameLen > 25) {
        return false
      }
      return true
    },
    isMaterialValid (){
      if (!this.materialSelected) {
        return null
      }
      return this.materials.some(m => this.materialSelected === m.id)
    },
    isFormValid () {
      if (!this.isNameValid) {
        return false
      }
      if (!this.materialSelected) {
        return false
      }
      return true
    }
  },
  async created () {
    await this.$axios.$get(`/api/suppliers/materials`)
      .then((listMaterials) => {
        this.materials = listMaterials || {};
      })
  },
  methods: {
    reset () {
      this.errorMsg = false
    },
    create () {
      var supplier = [];
      supplier.push({username: this.$auth.user.sub})

      this.$axios.$post('/api/suppliers/variant', {
        codigo: this.codigo,
        material: this.materialSelected,
        nome: this.name,
        weff_p: this.weff_p,
        weff_n: this.weff_n,
        ar: this.ar,
        sigmaC: this.sigmaC,
        suppliers: supplier
      })
        .then(() => {
          this.$toast.error('Created successfully!',{ duration: 2000 })
          this.$router.push('/suppliers')
        })
        .catch(() => {
          this.errorMsg = "An error Ocurred while Creating. Try again later or talk to the draftsman."
        })
    }
  }
}
</script>
