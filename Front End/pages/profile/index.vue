<template>
  <b-container>
    <nuxt-link to="/">
      <b-icon icon="chevron-left"></b-icon> Back
    </nuxt-link>

    <b-row class="pt-5" >

      <div class="order-xl-1 col-xl-8">
        <b-card>
          <b-form-group
            label="Edit Profile"
            label-size="sm"
            label-class="font-weight-bold pt-0"
            class="mb-0"
          >
            <hr>

            <div class="row">
              <fieldset class="col-6">
                <b-form-group
                  id="username"
                  label="Enter your name"
                  label-for="input-username"
                >
                  <b-form-input id="input-username" trim placeholder="roberto"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-6">
                <b-form-group
                  id="email"
                  label="Email Address"
                  label-for="input-email"
                >
                  <b-form-input id="input-email" trim type="email" placeholder="user@email.com"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-6">
                <b-form-group
                  id="name"
                  label="First name"
                  label-for="input-name"
                >
                  <b-form-input id="input-name" trim placeholder="R. do centro, Lisboa,Portugal"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-6">
                <b-form-group
                  id="password"
                  label="Password"
                  label-for="input-contact"
                >
                  <b-form-input id="input-password" type="password" trim placeholder="Sd4'$sd125s$"></b-form-input>
                </b-form-group>
              </fieldset>
            </div>

          </b-form-group>

          <b-form-group
            label="Contact Information"
            label-size="sm"
            label-class="font-weight-bold pt-0"
            class="mt-4 mb-0"
          >
            <hr>
            <div class="row">
              <fieldset class="col-12">
                <b-form-group
                  id="address"
                  label="Address"
                  label-for="input-address"
                >
                  <b-form-input id="input-address" trim placeholder="Rua do centro, n.8, Lisboa"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-4">
                <b-form-group
                  id="cidade"
                  label="City"
                  label-for="input-cidade"
                >
                  <b-form-input id="input-cidade" trim placeholder="Lisboa"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-4">
                <b-form-group
                  id="pais"
                  label="Country"
                  label-for="input-pais"
                >
                  <b-form-input id="input-pais" trim placeholder="(+351) 912456985"></b-form-input>
                </b-form-group>
              </fieldset>

              <fieldset class="col-4">
                <b-form-group
                  id="codido-postal"
                  label="Zip Code"
                  label-for="input-codido-postal"
                >
                  <b-form-input id="input-codido-postal" trim placeholder="(+351) 912456985"></b-form-input>
                </b-form-group>
              </fieldset>

            </div>
          </b-form-group>
        </b-card>
      </div>

      <div class="order-xl-2 mb-5 col-xl-4">
        <b-card>
          <b-row class="justify-content-center" style="margin-top: -95px;"><b-avatar size="150px"></b-avatar></b-row>

          <b-row style="color: #858585;">
            <b-card-body class="text-center col-4">
              <b-card-title class="font-weight-bold">22</b-card-title>
              <b-card-text class="light-color">Projects</b-card-text>
            </b-card-body>
            <b-card-body class="text-center col-4">
              <b-card-title class="font-weight-bold">50</b-card-title>
              <b-card-text class="light-color">Emails</b-card-text>
            </b-card-body>
            <b-card-body class="text-center col-4">
              <b-card-title class="font-weight-bold">135</b-card-title>
              <b-card-text class="light-color">Messages</b-card-text>
            </b-card-body>
          </b-row>

          <b-col class="text-center p-2">
            <span><b-badge variant="success">Online</b-badge></span>
            <h4>{{ $auth.user.sub }}</h4>
            <div> Lisboa, Portugal</div>
          </b-col>
          <hr>
          <b-col class="mt-3 text-justify">
            <h6 class="text-center">Company description</h6>
            It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution
          </b-col>

        </b-card>
      </div>

    </b-row>

  </b-container>
</template>
<style>
.light-color{color:#c7c7c7;}
</style>
