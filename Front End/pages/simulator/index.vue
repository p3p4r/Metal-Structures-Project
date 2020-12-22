<template>
  <b-container>
    <div>
      <nuxt-link to="/">
        <b-icon icon="chevron-left" /> Back
      </nuxt-link>
      <h1>Simulation <b-badge pill :variant="resultMsgColor" class="smlt_badge">{{ resultMsg }}</b-badge>
      </h1>
      <p>Enter the geometric parameters for the desired simulation.</p>

      <p class="text-danger p-2 alert-danger" style="border-radius: 5px" v-show="errorMsg">{{ errorMsg }}</p>

      <b-row class="smlt_mb">
        <form @submit.prevent="isSimulationPossible" :disabled="!isFormValid" class="col-lg-6 col-sm-12" id="Form_smlt">

          <b-row>
            <b-form-group class="col-12" label="Family">
              <b-form-radio-group
                id="families"
                v-model="selectFamily"
                :options="families"
                name="radio-options"
                value-field="name"
                @change="onChangeFamily"
                text-field="name">
              </b-form-radio-group>
            </b-form-group>
            <b-form-group class="col-12" label="Variant" :hidden="isVariantHidden">
              <multiselect
                v-model="VariantCode"
                :options="variants"
                :close-on-select="true"
                :clear-on-select="false"
                :hide-selected="true"
                :preserve-search="true"
                placeholder="-- Please Pick a Variant --"
                required
                label="nome"
                track-by="codigo"
                :preselect-first="true"
                id="variant"
                @select="onSelect"
              ></multiselect>
            </b-form-group>

            <b-form-group
              id="bars"
              class="col-4"
              description="*required"
              label="Number of bars"
              label-for="bars"
              :state="isNumBarsValid">
              <b-input
                id="bars"
                v-model="numBars"
                type="number"
                min="0"
                required
                @focusin="changeImage('simulationC3')"
                :state="isNumBarsValid"
                trim></b-input>
            </b-form-group>


            <b-form-group
              id="fullLoad"
              class="col-4"
              description="*required"
              label="Full load it supports"
              label-for="fullLoad"
              :state="isFullLoadValid">
              <b-input
                id="fullLoad"
                @focusin="changeImage('simulationC2')"
                required
                min="0"
                v-model="fullLoad"
                :state="isFullLoadValid"
                trim></b-input>
            </b-form-group>

            <b-form-group
              id="strutureWith"
              class="col-4"
              description="*required"
              label="Width of each span"
              label-for="strutureWith"
              :state="isStrutureWithValid">
              <b-input
                id="strutureWith"
                type="number"
                min="0"
                v-model="strutureWith"
                @focusin="changeImage('simulationC4')"
                :state="isStrutureWithValid"
                trim></b-input>
            </b-form-group>
          </b-row>

          <b-button type="reset" @click="reset">Reset</b-button>
          <b-button @click.prevent="isSimulationPossible" :disabled="!isFormValid" variant="primary">Submit</b-button>
        </form>

        <div id="smlt_img_holder" class="col-lg-6 col-sm-12 align-middle" >
          <img class="border-0" :src="require(`@/assets/images/${imageName}.png`)"  alt="Simulator Structure Image" />
        </div>
      </b-row>

      <div id="materials"></div>
      <div class="col-12 pt-5" :hidden="materialList" style="border-top: 1px solid #fefefe;">
        <h4>Materials Approved (Variants)</h4>
        <hr>
        <b-table
          v-if="materials.length"
          striped
          over
          :items="materials"
          :fields="materialsFields"
        />
        <p v-else>No Materials found.</p>

      </div>

    </div>
  </b-container>
</template>

<style>
form#Form_smlt div { margin-top: 0.5rem;margin-bottom: 0.5rem; }
.smlt_mb img {width: 100%;}
.smlt_badge {font-size: 1rem;vertical-align: middle;    padding-top: 5px;padding-bottom: 5px;}
.multiselect__option--highlight{background-color: #007bff;}
.multiselect__element span::after {background-color: #007bff;}
#families label:first-letter{ text-transform: capitalize; }

@media (max-width: 992px) {
  .smlt_mb{ display: table; padding: 1rem;}
  form#Form_smlt { display: table-footer-group; }
  #smlt_img_holder{ display:table-header-group; }
}
</style>

<script>
export default {
  middleware: 'auth', // only allowed users can acesss
  data() {
    return {
      imageName: 'simulationC1',
      variants: [],
      VariantCode: null,
      isVariantHidden: false,
      numBars: 1,
      fullLoad: 1,
      strutureWith: 1,
      materialList: true,
      errorMsg: false,
      resultMsg: '',
      resultMsgColor: '',
      families: [],
      selectFamily: "all",
      materials: [],
      materialsFields: [ 'nome', 'material', 'sigmaC', 'pp', 'weff_n', 'weff_p', 'ar'],
    }
  },
  async created () {
    await this.$axios.$get('/api/simulator/variants').then((variants) => { this.variants = variants })
    .catch(() => { this.$router.push('/auth/login') })
    await this.$axios.$get('/api/structures/families').then((family) => {
      this.families = family;
      this.families.unshift({ "name": "all", "description": "All"} )
    })

  },
  computed: {
    isNumBarsValid (){
      if (!this.numBars || this.numBars <= 0 || this.numBars >1000) {
        return false
      }
      return true;
    },
    isFullLoadValid (){
      if (!this.fullLoad || this.fullLoad <= 0 || this.fullLoad >100000) {
        return false
      }
      return true;
    },
    isStrutureWithValid (){
      if (!this.strutureWith || this.strutureWith <= 0 || this.strutureWith >50) {
        return false
      }
      return true;
    },
    isFormValid () {
      /*if (!this.VariantCode) {
        return false
      }*/
      if (!this.isNumBarsValid) {
        return false
      }
      if (!this.isFullLoadValid) {
        return false
      }
      if (!this.isStrutureWithValid) {
        return false
      }
      return true
    }
  },
  methods: {
    onChangeFamily (){
      if(this.selectFamily != "all"){
        this.isVariantHidden = true;
        this.variantCode = null;
      }else{
        this.isVariantHidden = false;
      }

      this.VariantCode = null;
    },
    onSelect(option, id) {
      this.variantCode = option.codigo
      if (!this.VariantCode) {
        return null
      }
      return this.variants.some(code => this.VariantCode.codigo === code.codigo)
    },
    reset () {
      this.errorMsg = false
    },
    changeImage(name) {
      this.imageName = name;
    },
    isSimulationPossible () {
      this.materialList = true;

      let url = `/api/simulator/variants/${this.selectFamily}`;

      if(this.variantCode != null) {
        url = `/api/simulator/`;
      }

      this.$axios.$post(url, {
        nb: this.numBars,
        LVao: this.strutureWith,
        q: this.fullLoad,
        variantCode: this.VariantCode != null ? this.VariantCode.codigo : 0,
      })
        .then((e) => {
          this.errorMsg = false;

          if (e.length > 0 || e.value){
            this.resultMsgColor = "success";
            this.resultMsg = "Approved";

            if(this.variantCode == null){
              this.materialList = false;
              this.materials = e;

              setTimeout(function(){
                var elem = document.getElementById("materials");
                elem.scrollIntoView({ behavior: 'smooth', block: 'center' });
              }, 250);
            }

          }else{
            this.resultMsgColor = "danger";
            this.resultMsg = "Denied"
          }
        })
        .catch((err) => {
          this.errorMsg = "An error occured please check if you entered the valid data."
        })
    },
  }
}
</script>
