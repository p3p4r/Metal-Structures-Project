<template>
  <form @submit.prevent="upload">
    <!-- Styled -->
    <b-form-file
      v-model="file"
      :state="hasFile"
      placeholder="Choose a file or drop it here..."
      drop-placeholder="Drop file here..."
    ></b-form-file>
    <div class="mt-3">
      Selected file: {{ file ? file.name : '' }}
    </div>
    <b-button type="submit" :disabled="!hasFile">Upload</b-button>
  </form>
</template>
<script>
export default {
  auth: false,
  data () {
    return {
      file: null
    }
  },
  computed: {
    hasFile () {
      return this.file != null
    },
    formData () {
      const formData = new FormData()
      formData.append('username', this.$auth.user.sub)
      if (this.file) {
        return formData.append('file', this.file)
      }
      return formData
    }
  },
  methods: {
    upload () {
      if (!this.hasFile) {
        return
      }
      this.$axios.$post('/api/documents', this.formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
    }
  }
}
</script>
