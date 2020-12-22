<template>
  <b-container>
    <nuxt-link to="/suppliers">
      <b-icon icon="chevron-left"></b-icon> Back
    </nuxt-link>
    <h4>Material: <b>{{ material.name }}</b></h4>
    <div class="text-right col-lg-12">
      <b-button @click.prevent="update" variant="info" :disabled="!isFormValid"> Update</b-button>
      <!--  <button @click.prevent="create" :disabled="!isFormValid">CREATE</button> -->
    </div>
    <form @submit.prevent="update" :disabled="!isFormValid" class="mt-5">
      <b-container fluid>
        <b-row class="my-1">
          <b-col sm="2">
            <label>Name</label>
          </b-col>
          <b-col sm="10">
            <b-form-input type="text" v-model="name"></b-form-input>
          </b-col>
        </b-row>

        <b-row class="my-1">
          <b-col sm="2">
            <label>Family</label>
          </b-col>
          <b-col sm="10">
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
          </b-col>
        </b-row>

        <b-row class="my-1">
          <b-col sm="2">
            <label>Type</label>
          </b-col>
          <b-col sm="10">
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
      name: '',
      material: {},
      families: [],
      familySelected: null,
      types: [],
      typeSelected: null,
    }
  },
  computed: {
    id () {
      return this.$route.params.name
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
    isFormValid () {
      if (!this.familySelected) {
        return false
      }
      if (!this.typeSelected) {
        return false
      }
      return true
    }
  },
  async created () { // on Page Load
    await this.$axios.$get(`/api/suppliers/materials/${this.id}`)
      .then((material) => {
        this.material = material || {}
        this.name = material.name;
      })

    //rota sem acesso
    await this.$axios.$get(`/api/structures/families`)
      .then((listFamilies) => {
        this.families = listFamilies;
        this.familySelected = listFamilies.find(f => this.material.family === f.name).name;
      })
    await this.$axios.$get(`/api/suppliers/materials/types`)
      .then((listTypes) => {
        this.types = listTypes;
        this.typeSelected = listTypes.find(t => this.material.type === t.name).name;
      })
  },
  methods: {
    update () {
      this.$axios.$put(`/api/suppliers/materials/${this.id}`,
        {
          name: this.name,
          family: this.familySelected,
          type: this.typeSelected,
          supplier: this.$auth.user.sub,
        })
        .then(this.$toast.success('Updated successfully!',{ duration: 2000 }))
        .catch((err) => {
          this.$toast.error('Error while updating!',{ duration: 2000 })
          console.log(err)
        })
    },
  }
}
</script>

