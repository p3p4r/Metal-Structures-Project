// An example of a login form for pages/auth/login.vue:
<template>
  <b-container class="w-25">
    <h2 class="text-center color-gray">Register Here</h2>
    <hr>
    <b-form @submit.prevent="onSubmit" class="mb-5">

      <b-form-group
        label="Name"
        description="*Required">
        <b-input
          name="name"
          v-model.trim="name"
          :state="isInvalidName"
          :value="name"
          placeholder="John doe"
          style="background: inherit;"
          required />
      </b-form-group>

          <b-form-group
            label="Username"
            description="*Required">
            <b-input
              name="username"
              v-model.trim="username"
              aria-describedby="password-help input-username-feedback"
              :state="isUsernameValid"
              :value="username"
              placeholder="johndoe123"
              style="background: inherit;"
              required />
            <b-form-invalid-feedback id="input-username-feedback">
              {{ invalidUsernameFeedback }}
            </b-form-invalid-feedback>
          </b-form-group>

          <b-form-group
            label="Password"
            description="*Required">
            <b-input
              name="password"
              type="password"
              :value="password"
              :state="isValidPassword"
              placeholder="p@ssW0rd"
              aria-describedby="password-help input-password-feedback"
              v-model="password"
              style="background: inherit;"
              required />
            <b-form-invalid-feedback id="input-password-feedback">
              {{ invalidPasswordFeedback }}
            </b-form-invalid-feedback>
          </b-form-group>

          <b-form-group for="confirmPassword" label="Confirm Your Password" description="*Required">
            <b-input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              :value="confirmPassword"
              placeholder="p@ssW0rd"
              aria-describedby="confirmPassword-help input-confirmPassword-feedback"
              :state="passwordsMatch"
              v-model="confirmPassword"
              style="background: inherit;"
              required />
            <b-form-invalid-feedback id="input-confirmPassword-feedback">
              Passwords do not match.
            </b-form-invalid-feedback>
          </b-form-group>

          <b-form-group label="Email" description="*Required">
            <b-input
              name="Email"
              v-model.trim="email"
              type="email"
              :state="isEmailValid"
              :value="email"
              placeholder="email@email.com"
              style="background: inherit;"
              required />
          </b-form-group>

      <b-form-group label="Phone Number" description="*Required">
        <b-input
          name="Phone Number"
          :value="phoneNumber"
          placeholder="123456789"
          v-model="phoneNumber"
          type="tel"
          aria-describedby="phone-number-help input-phone-number-feedback"
          :state="isPhoneNumberValid"
          style="background: inherit;"
          required />
        <b-form-invalid-feedback id="input-phone-number-feedback">
          Phone Number is not valid.
        </b-form-invalid-feedback>
      </b-form-group>

      <b-form-group label="Address">
        <b-input
          name="address"
          type="text"
          :value="address"
          placeholder="9203 Wilson Dr.Hamburg, NY 14075"
          v-model="address"
          style="background: inherit;"/>
      </b-form-group>

      <div lg="12">
        <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
        <b-button type="submit" variant="primary" size="md" class="w-100" :disabled="!isFormValid">Create Account</b-button>
      </div>

    </b-form>
  </b-container>
</template>
<script>
export default {
  auth: false,
  data () {
    return {
      username: "",
      password: "",
      confirmPassword: "",
      name: "",
      email: "",
      phoneNumber:"",
      address: "",
      errorMsg: false,
    }
  },
  computed: {
    invalidUsernameFeedback () {
      if (!this.username) {
        return null
      }
      const usernameLen = this.username.length
      if (usernameLen < 3 || usernameLen > 15) {
        return 'The username must be between [3, 15] characters.'
      }
      return ''
    },

    isUsernameValid () {
      if (this.invalidUsernameFeedback === null) {
        return null
      }
      return this.invalidUsernameFeedback === ''
    },
    passwordsMatch (){
      if (!this.confirmPassword) {
        return null
      }

      if (this.password != this.confirmPassword)
        return false

      return true
    },
    invalidPasswordFeedback (){
      if (!this.password) {
        return null
      }

      const passwordLen = this.password.length
      if (passwordLen < 3) {
        return 'The password is too Short.'
      }
      // TODO - Change with an regex
      if (passwordLen < 6) {
        return 'The password is to weak.'
      }
      return ''
    },
    isValidPassword (){
      if (this.invalidPasswordFeedback === null) {
        return null
      }
      return this.invalidPasswordFeedback === ''
    },
    isEmailValid () {
      if (!this.email) {
        return null
      }
      return true
    },
    isPhoneNumberValid (){
      if (!this.phoneNumber)
        return null

      if (this.phoneNumber.length < 9)
        return false

      return true
    },
    isInvalidName (){
      if (!this.name)
        return null

      if (this.name.length < 2)
        return false

      return true
    },
    isFormValid () {
      if(!this.isValidPassword)
        return false

      if(!this.isPhoneNumberValid)
        return false

      if(!this.isEmailValid)
        return false

      if(!this.isUsernameValid)
        return false

      if(!this.isInvalidName)
        return false

      if(!this.passwordsMatch)
        return false

      return true
    }
  },
  methods: {
    reset () {
      this.errorMsg = false
    },
    onSubmit () {
      const typeUser = 0;
      this.$axios.$post(`/api/user/${typeUser}`, {
        username: this.username.trim(),
        password: this.password.trim(),
        name: this.name.trim(),
        address: this.address.trim(),
        contactPerson: this.phoneNumber.trim(),
        email: this.email.trim()
      })
      .then(() => {
        this.$toast.success('Account created successfully!',{ duration: 2000 })
        this.$router.push('/auth/login')
      })
      .catch((err) => {
        this.errorMsg = "Error while creating..Try again later."
        //console.log(err)
      })
    },
  }
}
</script>
