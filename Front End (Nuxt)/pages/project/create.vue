<template>
  <b-container>
    <div>
      <nuxt-link to="/draftsman">
        <b-icon icon="chevron-left"></b-icon> Back
      </nuxt-link>
      <h1>Create new Project</h1>
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

        <b-form-group>
          <label>Client</label>
          <b-select
            v-model="clientName"
            :options="clients"
            :state="isclientNameValid"
            required
            value-field="name"
            text-field="name">
            <template v-slot:first>
              <option :value="null" disabled>-- Choose a Client --
              </option>
            </template>
          </b-select>
        </b-form-group>

        <b-form-group>
          <label>Status</label>
          <b-select
            v-model="statusName"
            :options="status"
            :state="isStatusValid"
            required
            value-field="value"
            text-field="value">
            <template v-slot:first>
              <option :value="null" disabled>-- Please Pick a Status --
              </option>
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
    middleware: 'auth' // only allowed users can acesss
    return {
      name: null,
      state: null,
      clientName: null,
      statusName: null,
      clients: [],
      status: [],
      errorMsg: false
    }
  },
  async created () {
    await this.$axios.$get('/api/clients').then((c) => { this.clients = c })
    await this.$axios.$get('/api/draftsmen/projects/status').then((s) => {
      this.status = s
      this.statusName = s.find(st => "DRAFT" ===  st)
    })
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
    isclientNameValid () {
      if (!this.clientName) {
        return null
      }
      return this.clients.some(c => this.clientName === c.name)
    },
    isStatusValid (){
      if (!this.statusName) {
        return null
      }
      return this.status.some(c => this.statusName === c)
    },
    isFormValid () {
      if (!this.isNameValid) {
        return false
      }
      if (!this.isclientNameValid) {
        return false
      }
      if (!this.statusName) {
        return false
      }
      return true
    }
  },
  methods: {
    reset () {
      this.errorMsg = false
    },
    getUsernameClient (){
      return this.clients.find(c => this.clientName === c.name).username
    },
    create () {
      this.$axios.$post('/api/draftsmen/projects', {
        name: this.name,
        clientUsername: this.getUsernameClient(),
        draftsmanUsername: this.$auth.user.sub,
        status: this.statusName
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
