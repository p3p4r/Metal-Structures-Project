<template>
  <b-container>
    <b-button @click="$router.back()" variant="light" class="mb-3">
      <b-icon icon="chevron-left"></b-icon> Back
    </b-button>

    <h4>Structure Details</h4>
    <hr>

    <b-row>
      <b-col>
        <div class="col-lg-6">
          <p><b>Name:</b> {{structures.name}}</p>
          <p><b>Project:</b> {{structures.project}}</p>
          <p><b>Type:</b> {{structures.type}}</p>
          <p><b>Dimensions:</b> {{structures.dimensions}}</p>
          <p><b>Calculation Parameters:</b> {{structures.calculationParameters}}</p>
          <p><b>Create at:</b> {{structures.create_at}}</p>
        </div>
      </b-col>

      <b-col lg="12">
        <div v-if="draftsman" class="pb-3 mb-2 text-right" style=" border-bottom: 1px solid #e2e2e2;">
          <div class="text-right" :hidden="buttonAdd">
            <b-button class="mr-4" @click.prevent="showFieldVariants(true)">Add Variant</b-button>
          </div>
        </div>
      </b-col>

      <b-col>
        <div class="text-right" v-if="client" v-for="adjudication in structures.adjudications" :key="adjudication.id">
          <div v-once v-if="adjudication.length != 0 && adjudication.adjudicate.toLowerCase() == 'onapproval'">
            <b-button variant="success" v-b-modal.modal-adjudication-item @click.prevent="sendInfo(structures.id,structures.adjudications,true)">Approve</b-button>
            <b-button variant="danger" v-b-modal.modal-adjudication-item @click.prevent="sendInfo(structures.id,structures.adjudications)">Decline</b-button>
          </div>
          <div v-once v-else>
            <h4>
              <b-badge v-if="adjudication.adjudicate.toLowerCase() == 'notadjudicated'" variant="danger">Not adjudicated</b-badge>
              <b-badge v-else variant="success">{{ adjudication.length != 0 ? adjudication.adjudicate : "" }}</b-badge>
            </h4>
          </div>
        </div>
      </b-col>

    </b-row>

    <div class="pt-2">

      <b-row v-if="draftsman" class="pb-4 pt-4" :hidden="fieldListVariants">
        <b-col lg="10" sm="8" xs="12">
          <b-select
            id="listVariants"
            v-model="listVariantSelected"
            :options="listVariants"
            :state="isVariantValid"
            required
            value-field="codigo"
            text-field="nome">
            <template v-slot:first>
              <option :value="null" disabled>-- Please Pick a Variants --
              </option>
            </template>
          </b-select>
        </b-col>
        <b-col lg="2" sm="4" xs="12">
          <b-button class="w-50" variant="primary" @click.prevent="addVariantToProject">Add</b-button>
          <b-button variant="light" @click.prevent="showFieldVariants(false)">
            <b-icon icon="x"/>
          </b-button>
        </b-col>
      </b-row>

      <div class="justify-content-centermy-1 row">
        <b-form-fieldset horizontal label="Filter" class="col-10">
          <b-form-input v-model="filter" placeholder="Search..."></b-form-input>
        </b-form-fieldset>

        <b-form-fieldset horizontal label="Rows per page" class="col-2">
          <b-form-select :options="[{text:5,value:5},{text:10,value:10},{text:15,value:15}]" v-model="perPage">
          </b-form-select>
        </b-form-fieldset>
      </div>
      <b-table striped hover  v-if="structures.variantes"
               striped
               over
               :items="structures.variantes"
               :fields="variantesFields"
               :current-page="currentPage"
               :per-page="perPage"
               :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               responsive="sm">
        <template v-slot:cell(adjudications)="row">
          <div v-if="row.item.length != 0 && row.item.adjudications.adjudicate && row.item.adjudications.adjudicate.toLowerCase() == 'onapproval'">
            <b-button variant="success" v-b-modal.modal-adjudication-item @click.prevent="sendInfo(row.item.id,true)">
              <b-icon icon="check2"></b-icon>
            </b-button>
            <b-button variant="danger" v-b-modal.modal-adjudication-item @click.prevent="sendInfo(row.item.id)">
              <b-icon icon="x"></b-icon>
            </b-button>
          </div>
        </template>
      </b-table>
      <p v-else>No variants associated.</p>
    </div>

    <!-- modal to Delete -->
    <b-modal id="modal-adjudication-item" centered title="Confirm Adjudication?">
      <p class="mt-2">Observation:</p>
      <b-form-textarea name="observation" id="observation" v-model="observation" placeholder="Enter your observation here..." rows="3" max-rows="6"></b-form-textarea>
      <!-- Começa aqui a c-->
      <div v-show="isApproved==false">
        <label class="mt-2">Select Variants to approve:</label>
        <div v-for="variant in structures.variantes" :key="variant.variantCode">
          <input type="checkbox" v-model="checkedVariants" :value="variant.codigo"/>
          <label>{{variant.nome}}</label>
          <br>
        </div>
      </div>
      <!-- Acaba aqui a c-->
      <template #modal-footer="{ Confirm, cancel }">
        <b-button size="sm" variant="secondary" @click="() => {observation = ''; cancel(); }">
          Cancel
        </b-button>
        <b-button size="sm" variant="primary" @click="adjudicate(adjudicationId)">
          Confirm
        </b-button>
      </template>
    </b-modal>
    &nbsp;
  </b-container>
</template>
<script>
export default {
  data () {
    return {
      structures: [],
      variantesFields: ['nome', 'material', 'pp', 'sigmaC', 'weff_n', 'weff_p'],
      currentPage: 1,
      perPage: 5,
      filter: null,
      sortBy: 'nome',
      sortDesc: false,
      client: false,
      draftsman: false,
      supplier: false, // in case of future need
      observation: '',
      adjudicationId: null,
      isAdjudicationApproved: false,
      listVariants: [],
      checkedVariants: [],
      listVariantSelected: null,
      buttonAdd: false,
      fieldListVariants: true,
      isApproved: false,
    }
  },
  methods:{
    sendInfo(item,list, isApproved = false) {
      this.isApproved = isApproved

      if(this.getAdjudicationDetails(list))
        this.adjudicationId  = this.getAdjudicationDetails(list).id

      if (isApproved)
        this.isAdjudicationApproved = isApproved;
    },
    getAdjudicationDetails (list){
      let details =  list.find((adjudicacao) => adjudicacao.project === this.structures.project && adjudicacao.structure ===  this.structures.id && adjudicacao.client === this.$auth.user.sub)
      return details !== undefined ? details : false
    },
    adjudicate(id){
      let adjudicationValue = "NotAdjudicated";
      if(this.isAdjudicationApproved)
        adjudicationValue = "Adjudicated" ; // Adjudicated

      this.$axios.$put(`/api/structures/adjudication/${id}`,
        {
          id: this.adjudicationId,
          adjudicate: adjudicationValue,
          observation: this.observation,
          client: this.$auth.user.sub,
          structure: this.structures.id,
          variantes: this.GetListVariants()
        })
        .then(() => {
          this.$toast.success('Updated successfully!',{ duration: 2000 })
          window.location.reload();
        })
        .catch((err) => {
          this.$toast.error('Error whille updating!',{ duration: 2000 })
          console.log(err)
        })

      this.isAdjudicationApproved = false;
      this.$bvModal.hide('modal-adjudication-item')
    },
    GetListVariants(){
      var data = [];
      this.checkedVariants.forEach((value, index) => {
        data.push({codigo: value})
      })
      return data
    },
    getVariantsList (){
      this.$axios.$get(`/api/structures/variants`)
        .then((listOfVariants) => {
          // TODO [See if needed] - get Variants that arent in the Structure
          this.listVariants = listOfVariants || {}
        })
        .catch((err) => console.log(err))
    },
    showFieldVariants (value){
      if (value == true) { // show div
        this.buttonAdd = true;
        this.fieldListVariants = false;
        this.getVariantsList ();
      }else{ // hide Field
        this.buttonAdd = false;
        this.fieldListVariants = true;
      }
    },
    updateVariant (id, structureId){
      this.$axios.$post(`/api/structures/variant/${id}`,
        {
            id: structureId
        })
        .then(() => { return true; })
        .catch((err) => {
          return false;
          console.log(err)
        })
    },
    addVariantToProject () {
      if (this.listVariantSelected > 0){
        // TODO - Enviar id da Estrutura
        try{
          let variant = this.listVariants.find(s => this.listVariantSelected === s.codigo)

          // Update Variant [ Temporally -> future change because of too many calls ]
          if (this.updateVariant(this.listVariantSelected , this.structures.id) == false){
            this.$toast.error(`Error adding item.`)
            return
          }

          if (variant != undefined || variant.length != 0) { // if array not empty
            // if variant already have structure
            if(!this.structures.variantes.find(st => st.codigo === this.listVariantSelected)){
              this.structures.variantes.push(variant)
              this.$toast.success(`Added item.`)
            }
          }else{
            this.$toast.error(`Error adding item.`)
          }
        }catch (err){
          this.$toast.error(`Error adding item.`)
        }
      }
    }
  },
  computed: {
    id () {
      return this.$route.params.id
    },
    isVariantValid (){
      if (!this.listVariantSelected) {
        return null
      }
      return this.listVariants.some(s => this.listVariantSelected === s.codigo)
    },
  },
  created () {
    if(this.$auth.$state.loggedIn){
      if(this.$auth.user.groups.includes('Draftsman'))
        this.draftsman = true

      if(this.$auth.user.groups.includes('Client'))
        this.client = true
      else
        this.supplier = true
    }

    this.$axios.$get(`/api/structures/${this.id}`)
      .then((structure) => {
        this.structures = structure || {}
      })
      .catch(error => {
        console.info(error.message)
      })
  },

}
</script>
