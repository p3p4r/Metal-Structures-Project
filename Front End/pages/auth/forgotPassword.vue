// An example of a login form for pages/auth/login.vue:
<template>
  <b-container class="w-25">
    <h2 class="text-center color-gray">Forgot Your Password</h2>
    <hr>
    <b-form @submit.prevent="onSubmit">

      <b-form-group class="mt-3" label="Enter Your Email/Username">
        <b-input
          name="value"
          :value="value"
          placeholder="Your Email/Username"
          v-model="value"
          style="background: inherit;"
          required />
      </b-form-group>

      <div lg="12">
        <b-button type="submit" variant="primary" size="md" class="mt-3 w-100">Send</b-button>
      </div>


    </b-form>
  </b-container>
</template>
<script>
export default {
  auth: false,
  data () {
    return {
      value: ""
    }
  },
  methods: {
    onSubmit () {
      let username = "";
      let email = "";

      if (this.value.includes('@')) // if contains '@' then asssume that is an Email
        email = this.value;
      else
        username = this.value;

      this.$axios.$post(`/api/user/forgot`, {
        username: username.trim(),
        email: email.trim()
      })
        .then(() => {
          this.$toast.success('Email Sent!',{ duration: 2000 })
          this.$router.push('/auth/login')
        })
        .catch((err) => {
          this.$toast.error('Error Username/Email incorrect!',{ duration: 2000 })
          //console.log(err)
        })
    }
  },
}
</script>
