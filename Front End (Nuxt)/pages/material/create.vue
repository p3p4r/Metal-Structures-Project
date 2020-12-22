<template>
  <b-container>
    <nuxt-link to="/suppliers">
      <b-icon icon="chevron-left"></b-icon> Back
    </nuxt-link>
    <h1>Create new Material</h1>
    <div class="text-right col-lg-12">
      <b-button @click.prevent="create" variant="info" :disabled="!isFormValid"> Create</b-button>
      <!--  <button @click.prevent="create" :disabled="!isFormValid">CREATE</button> -->
    </div>
    <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
    <form @submit.prevent="create" :disabled="!isFormValid" class="mt-5">
      <b-container fluid>
        <b-row class="my-1">
          <b-col sm="2">
            <label for="name">Name</label>
          </b-col>
          <b-col sm="10">
            <b-form-group
            id="name"
            description="The name is required"
            :invalid-feedback="invalidMaterialNameFeedback"
            :state="isNameValid">
            <b-input
              id="name"
              v-model.trim="name"
              placeholder="Material X"
              :state="isNameValid"
              trim></b-input>
          </b-form-group></b-col>
        </b-row>

        <b-row class="my-1">
          <b-col sm="2"><label>Family</label></b-col>
          <b-col sm="10">
            <b-form-group>
              <b-select
                id="family"
                v-model="familySelected"
                :options="families"
                :state="isfamilyValid"
                required
                value-field="name"
                text-field="description">
                <template v-slot:first>
                  <option :value="null" disabled>-- Please Pick a Family --
                  </option>
                </template>
              </b-select>
            </b-form-group>
          </b-col>

          <b-col sm="2"><label>Type</label></b-col>
          <b-col sm="10">
            <b-form-group>
              <b-select
                id="type"
                v-model="typeSelected"
                :state="isTypeValid"
                :options="types"
                required
                value-field="name"
                text-field="name">
                <template v-slot:first>
                  <option :value="null" disabled>-- Please Pick a Type --
                  </option>
                </template>
              </b-select>
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
      name: null,
      families: [],
      familySelected: null,
      types: [],
      typeSelected: null,
      errorMsg: false
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
      if (nameLen < 3 || nameLen > 50) {
        return false
      }
      return true
    },
    isfamilyValid (){
      if (!this.familySelected) {
        return null
      }
      return this.families.some(f => this.familySelected === f.name)
    },
    isTypeValid (){
      if (!this.typeSelected) {
        return null
      }
      return this.types.some(t => this.typeSelected === t.name)
    },
    isFormValid () {
      if (!this.isNameValid) {
        return false
      }
      if (!this.familySelected) {
        return false
      }
      if (!this.typeSelected) {
        return false
      }
      return true
    }
  },
  async created () {
    await this.$axios.$get(`/api/structures/families`)
      .then((listFamilies) => {
        this.families = listFamilies;
      })
    await this.$axios.$get(`/api/suppliers/materials/types`)
      .then((listTypes) => {
        this.types = listTypes;
      })
  },
  methods: {
    reset () {
      this.errorMsg = false
    },
    create () {
      this.$axios.$post('/api/suppliers/materials', {
        name: this.name,
        family: this.familySelected,
        type: this.typeSelected,
        supplier: this.$auth.user.sub,
      })
        .then(() => {
          this.$router.push('/suppliers')
        })
        .catch((error) => {
          this.errorMsg = error.response.data
        })
    }
  }
}
</script>
