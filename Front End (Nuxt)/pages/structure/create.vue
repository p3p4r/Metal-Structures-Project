<template>
  <b-container>
    <div>
      <nuxt-link to="/draftsman">
        <b-icon icon="chevron-left"></b-icon> Back
      </nuxt-link>
      <h1>Create new Structure</h1>
      <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
      <form @submit.prevent="create" :disabled="!isFormValid">
        <b-form-group
          id="name"
          description="The name is required"
          label="Name"
          label-for="name"
          :invalid-feedback="invalidUsernameFeedback"
          :state="isNameValid">
          <b-input
            id="name"
            v-model.trim="name"
            :state="isNameValid"
            trim></b-input>
        </b-form-group>

        <b-form-group
          id="dimensions"
          description="Dimensions are required"
          label="Dimensions"
          label-for="dimensions"
          :invalid-feedback="invalidDimensionsFeedback"
          :state="isDimensionsValid">
          <b-input
            id="dimensions"
            v-model.trim="dimensions"
            :state="isDimensionsValid"
            trim></b-input>
        </b-form-group>

        <b-form-group
          id="calculationParameters"
          description="Calculation parameters are required"
          label="Calculation Parameters"
          label-for="calculationParameters"
          :invalid-feedback="invalidCalculationParametersFeedback"
          :state="isCalculationParametersValid">
          <b-input
            id="calculationParameters"
            v-model.trim="calculationParameters"
            :state="isDimensionsValid"
            trim></b-input>
        </b-form-group>

        <b-form-group>
          <label>Type</label>
          <b-select
            v-model="typeName"
            :options="types"
            :state="isTypeNameValid"
            required
            value-field="name"
            text-field="name">
            <template v-slot:first>
              <option :value="null" disabled>-- Choose a Type --</option>
            </template>
          </b-select>
        </b-form-group>

        <b-form-group>
          <label>Project</label>
          <b-select
            v-model="projectName"
            :options="projects"
            :state="isProjectNameValid"
            required
            value-field="name"
            text-field="name">
            <template v-slot:first>
              <option :value="null" disabled>-- Choose a Project --</option>
            </template>
          </b-select>
        </b-form-group>

        <b-button type="reset" @click="reset">RESET</b-button>
        <b-button @click.prevent="create" :disabled="!isFormValid" variant="primary">CREATE</b-button>
      </form>
    </div>
  </b-container>
</template>
<script>
export default {
  data () {
    middleware: 'auth' // only allowed users can acess
    return {
      name: null,
      dimensions: null,
      calculationParameters: null,
      typeName: null,
      projectName: null,
      projects: [],
      types: [],
      draftsman: null,
      errorMsg: false
    }
  },
  async created () {
    await this.$axios.$get('/api/projects').then((p) => { this.projects = p })
    await this.$axios.$get('/api/types/').then((t) => { this.types = t })
  },
  computed: {
    invalidUsernameFeedback () {
      if (!this.name) {
        return null
      }
      const nameLen = this.name.length
      if (nameLen < 3 || nameLen > 15) {
        return 'The name must be between [3, 15] characters.'
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
    invalidDimensionsFeedback () {
      if (!this.dimensions) {
        return null
      }
      return true
    },
    isDimensionsValid () {
      if (!this.dimensions) {
        return null
      }
      return true
    },
    isFormValid () {
      if (!this.isNameValid) {
        return false
      }

      return true
    },
    invalidCalculationParametersFeedback () {
      if (!this.name) {
        return null
      }

      return true
    },
    isCalculationParametersValid () {
      if (!this.name) {
        return null
      }

      return true
    },
    invalidTypeFeedback () {
      if (!this.typeName) {
        return null
      }
      return true

    },
    isTypeValid () {
      if (!this.typeName) {
        return null
      }

      return true
    },
    isTypeNameValid () {
      if (!this.typeName) {
        return null
      }
      return this.types.some(t => this.typeName === t.name)
    },
    isProjectNameValid () {
      if (!this.projectName) {
        return null
      }
      return this.projects.some(p => this.projectName === p.name)
    },
  },
  methods: {
    reset () {
      this.errorMsg = false
    },
    create () {
      this.$axios.$post('/api/structures/', {
        name: this.name,
        dimensions: this.dimensions,
        calculationParameters: this.calculationParameters,
        type: this.typeName,
        project: this.projectName,
        draftsman: this.$auth.user.sub
      })
        .then(() => {
          this.$router.push('/draftsman')
        })
        .catch((error) => {
          this.errorMsg = error.response.data
        })
    }
  }
}
</script>
