<template>
  <client-only> <!-- To hide html erros -->
  <div id="app">
    <b-navbar toggleable="lg" type="light">
      <b-navbar-brand to="/" class="col-lg-2 col-sm-12 text-center">
        <b-img id="logo-h" class="border-0 border-bottom-1" src="../assets/images/logo-h.png" fluid alt="Company Logo"></b-img>
      </b-navbar-brand>
      <b-button v-b-toggle.sidebar style="background-color: inherit; color:gray" class="border-0">
        <span class="navbar-toggler-icon mr-1"></span>Menu
      </b-button>
        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown v-if="$auth.loggedIn" right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <em>Hello, {{ $auth.user.sub }}</em>
              <b-avatar size="md"></b-avatar>
            </template>
            <b-dropdown-item @click.prevent="signOut">Sign Out</b-dropdown-item>
          </b-nav-item-dropdown>
          <li class="nav-item" v-else>
            <nuxt-link class="nav-link" to="/auth/login">Sign In</nuxt-link>
          </li>
        </b-navbar-nav>
    </b-navbar>

    <b-sidebar v-if="this.$auth" id="sidebar" backdrop shadow>
      <div class="px-3 py-2">
        <b-img class="border-0 border-bottom-1" src="../assets/images/logo.png" fluid thumbnail alt="Company Logo"></b-img>
        <nav class="mb-3">
          <b-nav vertical class="align-content-center">

            <b-nav-item to="/" active class="mt-2 pb-3 h5"><b-icon icon="house-door-fill" class="mr-2"/>Homepage</b-nav-item>
            <b-nav-item to="/profile" class="pb-3 h5"><b-icon icon="person-circle" class="mr-2"/>Profile</b-nav-item>
            <b-nav-item v-if="this.$auth.$state.loggedIn && this.$auth.user.groups.includes('Draftsman')" to="/simulator" class="pb-3 h5"><b-icon icon="layout-wtf" class="mr-2"/>Simulator</b-nav-item>
            <b-nav-item v-if="this.$auth.$state.loggedIn && this.$auth.user.groups.includes('Draftsman')" to="/draftsman" class="pb-3 h5"><b-icon icon="folder" class="mr-2"/>Projects</b-nav-item>
            <b-nav-item v-if="this.$auth.$state.loggedIn && this.$auth.user.groups.includes('Client')"    to="/clients" class="pb-3 h5"><b-icon icon="people" class="mr-2"/>Clients</b-nav-item>
            <b-nav-item v-if="this.$auth.$state.loggedIn && this.$auth.user.groups.includes('Supplier')"  to="/suppliers" class="pb-3 h5"><b-icon icon="box" class="mr-2"/>Materials</b-nav-item>
            <b-nav-item to="/settings" class="pb-3 h5"><b-icon icon="gear" class="mr-2"/>Settings</b-nav-item>

          </b-nav>
        </nav>
      </div>
    </b-sidebar>
    <span v-else></span>
    <main class="pt-5">
      <Nuxt/>
    </main>
  </div>
  </client-only>
</template>
<script>
export default {
  middleware: 'auth',
  methods: {
    signOut() {
      this.$auth.logout()
      this.$router.push('/')
    },
  }
}
</script>
<style>
#logo-h{height: 60px;}
body{background-color: #fafafa;}
nav:first-child{box-shadow: 0px 1px 5px 0px #d9d9d9;}
#sidebar img {background-color: inherit}
#sidebar a.nav-link{ color: #727270}
#sidebar a.nav-link.active{color: #7ac5e9;  font-weight: bold;}
#sidebar a.nav-link:hover{color: #59add5}

<!-- Change later -->
table td{vertical-align: middle !important;}
table th{text-align: center;}
.table th, .table td{ text-align: center !important;}
.color-gray {color: #838383;}
</style>

