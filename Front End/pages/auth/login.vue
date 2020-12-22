// An example of a login form for pages/auth/login.vue:
<template>
  <b-container class="w-25">
    <h2 class="text-center color-gray">Login</h2>
    <hr>
    <b-form @submit.prevent="onSubmit" @reset="onReset">
      <b-form-group label="Username" description="Enter your username">
        <b-input
          name="username"
          v-model.trim="username"
          :value="username"
          placeholder="Your username"
          style="background: inherit;"
          required />
      </b-form-group>
      <b-form-group label="Password" description="Enter your password">
        <b-input
          name="password"
          type="password"
          :value="password"
          placeholder="Your password"
          v-model="password"
          style="background: inherit;"
          required />
      </b-form-group>

      <div lg="12">
        <b-button type="submit" variant="primary" size="md" class="w-100">Login</b-button>
      </div>

      <div class="text-center pt-3">
        <div>
          <nuxt-link to="/auth/forgotPassword">
            Forgot the passsword?
          </nuxt-link>
        </div>
        <div>
          <nuxt-link to="/auth/register">
            Dont't have an account? Click here.
          </nuxt-link>
        </div>
      </div>
    </b-form>
  </b-container>
</template>
<script>
export default {
  //auth: false,
  data () {
    return {
      username: "draftsman", // default Value set to null
      password: "123" // default Value set to null
    }
  },
  methods: {
    onSubmit () {
      const promise = this.$auth.loginWith('local', {
        data: {
          username: this.username,
          password: this.password
        }
      })
      promise.then(() => {
        // TODO - redirect based on the user role
        if (this.$auth.user.groups.includes('Draftsman')) {
          this.$router.push('/draftsman')
        }else if(this.$auth.user.groups.includes('Supplier')){
          this.$router.push('/suppliers')
        }else{
          this.$router.push('/clients')
        }
      })
      promise.catch(() => {
        this.$toast.error('Sorry, you cannot login. Ensure your credentials are correct',{ duration: 2000 })
      })
    },
    onReset () {
      this.username = null
      this.password = null
    },
  }
}
</script>
