<template>
  <b-container class="align-items-center">
    <div class="col-sm-12 align-middle text-center" >
      <img v-if="error.statusCode === 404 || error.statusCode === 503"  class="border-0" :src="require(`@/assets/images/notfound.png`)"  alt="Simulator Structure Image" />
      <img v-if="error.statusCode === 500"  class="border-0" :src="require(`@/assets/images/servererror.png`)"  alt="Simulator Structure Image" />
      <div class="col-lg-12">
        <b-button to="/" variant="danger">Home Page</b-button>
      </div>
    </div>
  </b-container>
</template>

<script>
export default {
  props: ['error'],
  layout: 'error' // you can set a custom layout for the error page
}
</script>
