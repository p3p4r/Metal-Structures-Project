<template>
  <b-container>
  <form @submit.prevent="upload"><!--Styled -->
    <b-form-group>
      <label>Project</label>
      <b-select
        v-model="project"
        :options="projects"
        required
        value-field="name"
        text-field="name">
        <template v-slot:first>
          <option :value="null" disabled>-- Choose a Project --</option>
        </template>
      </b-select>
      <label>Observations</label>
      <b-textarea
      value-field="name"
      text-field="name">
      </b-textarea>
    </b-form-group>
    <b-form-file
      v-model="file"
      :state="hasFile"
      placeholder="Choose a file or drop it here..."
      drop-placeholder="Drop file here...">
    </b-form-file>
    <div class="mt-3">Selected file: {{ file ? file.name : '' }}</div>
    <nuxt-link class="btn btn-link" :to="`/draftsman`">Back</nuxt-link>
    <b-button type="submit" :disabled="!hasFile">Upload</b-button>
  </form>
  </b-container>
</template>
<script>
export default {
  auth: false,
  data() {
    return {
      username: this.$auth.user.sub,
      file: null,
      project: null,
      projects: [],
      observations: null
    }
  },
  async created () {
    await this.$axios.$get('/api/projects').then((p) => { this.projects = p })
  },
  computed: {
    hasFile() {
      return this.file != null
    }, formData() {
      let formData = new FormData()

      if (this.file) {
        formData.append('file', this.file)
      }
      formData.append('observations', this.observations)
      formData.append('project', this.project)
      return formData
    }
  },
  isProjectNameValid () {
    if (!this.project) {
      return null
    }
    return this.projects.some(p => this.project === p.name)
  },
  methods: {
    upload() {
      if (!this.hasFile) {
        return
      }
      let promisse = this.$axios.$post('/api/file/upload', this.formData, {
        headers: {'Content-Type': 'multipart/form-data'}})
      promisse.then(() => {
        this.$router.push('/draftsman')
        this.$toast.success('File uploaded!').goAway(3000)
      })
      promisse.catch(() => {
        this.$toast.error('Sorry, could no upload file!').goAway(3000)
      })
    },
  }
}</script>
