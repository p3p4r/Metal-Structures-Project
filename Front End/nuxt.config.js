export default {
  // Global page headers (https://go.nuxtjs.dev/config-head)
  head: {
    title: 'Metallic Structures Construction',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },
  
  // Disable Server Side render
  ssr: false,

  // Global CSS (https://go.nuxtjs.dev/config-css)
  css: [
  ],

  // Plugins to run before rendering page (https://go.nuxtjs.dev/config-plugins)
  plugins: [
  ],

  // Auto import components (https://go.nuxtjs.dev/config-components)
  components: true,

  // Modules for dev and build (recommended) (https://go.nuxtjs.dev/config-modules)
  buildModules: [
  ],

  // Modules (https://go.nuxtjs.dev/config-modules)
  modules: [
    // https://go.nuxtjs.dev/bootstrap
    ['bootstrap-vue/nuxt', {
      icons: true,
    }],
    '@nuxtjs/axios',
    '@nuxtjs/toast',
    '@nuxtjs/auth',
    'nuxt-vue-multiselect',
  ],

  // Axios module configuration (https://go.nuxtjs.dev/config-axios)
  axios: {
    proxy: true,
    credentials: true
  },

  proxy: {
    '/api/': {
      target: 'http://localhost:8080/project/api/',
      pathRewrite: {
        '^/api/': ''
      }
    }
  },

  // Auth module configuration (https://auth.nuxtjs.org/)
  auth: {
    redirect: {
      login: '/auth/login',
      logout: '/',
      home: '/'
    },
    watchLoggedIn: true,
    strategies: {
      local: {
        endpoints: {
          login: {
            url: '/api/login/token',
            method: 'post',
            propertyName: 'token'
          },
          logout: false,
          user: {
            url: '/api/login/claims',
            method: 'get',
            propertyName: ''
          }
        },
        // tokenRequired: true, -> default
        // tokenType: 'bearer' -> default
      }
    }
  },

  router: {
    middleware: [
      'auth'
    ]
  },

  // Build Configuration (https://go.nuxtjs.dev/config-build)
  build: {
  }
}
