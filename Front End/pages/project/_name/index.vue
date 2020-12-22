<template>
  <b-container>
    <div class="col-lg-12 text-right pb-4 pt-2">
      <b-button @click="$router.back()" variant="light" class="mb-3"  style="float: left">
        <b-icon icon="chevron-left"></b-icon> Back
      </b-button>
      <b v-show="draftsman">Shortcuts: </b>
      <b-button v-show="draftsman" to="/structure/create" variant="light" size="sm">Create Structure <b-icon class="ml-1"  icon="nut-fill" /></b-button>
    </div>

    <h4>Project: {{ project.name }}</h4>
    <div class="text-right col-lg-12">
      <b-button @click.prevent="update" variant="info">Update</b-button>
    </div>
    <form @submit.prevent="update">
      <b-row>
        <div class="col-lg-6">
          <p>Client: {{ project.clientUsername }}</p>
          <span>Status:</span>
          <div v-if="this.$auth.$state.loggedIn && this.$auth.user.groups.includes('Draftsman')">
            <b-select
              v-model="statusName"
              :options="status"
              :state="isStatusValid"
              required
              class="col-lg-6"
              value-field="value"
              text-field="value">
              <template v-slot:first>
                <option :value="null" disabled>-- Please Pick a Status --
                </option>
              </template>
            </b-select>
          </div>
          <span v-else> {{ project.status }}</span>

        </div>

        <div class="col-lg-6" style="border-left: 1px solid #dedede;">
          <h4>Documents</h4>
            <div id="documents"v-for="doc in documents" :key="doc.id">
              <b-row>
                <b-col cols="12">
                  <b-btn
                    class="btn btn-link"
                    variant="light"
                    size="sm"
                    @click.prevent="download(doc.id,doc.namePath)"
                    target="_blank"><b-icon icon="file-earmark" class="mr-1" />{{ doc.namePath }}</b-btn>
                </b-col>
              </b-row>
            </div>
          <!--<b-table
            v-if="documents.length" striped
            over
            :items="documents"
            :fields="documentsFields">
            <template v-slot:cell(actions)="row">
              <b-btn
                class="btn btn-link"
                variant="light"
                @click.prevent="download(row.item)"
                target="_blank">Download</b-btn>
            </template>
          </b-table>
          <p v-else>No documents.</p>-->
        </div>
      </b-row>

      <b-row class="pt-5 pb-4 mb-3" style=" border-bottom: 1px solid #e2e2e2;">
        <b-col>
          <h4>Structures</h4>
        </b-col>
        <b-col>
          <div v-if="draftsman" class="text-right" :hidden="buttonAdd">
            <b-button class="mr-4" @click.prevent="showFieldStructures(true)">Add Structure</b-button>
          </div>
        </b-col>
      </b-row>

      <b-row v-if="draftsman" class="pb-4 pt-4" :hidden="fieldListStructures">
        <b-col lg="10" sm="8" xs="12">
          <b-select
            id="listStrucutres"
            v-model="listStructureSelected"
            :options="listStructures"
            :state="isStructureValid"
            required
            value-field="id"
            text-field="name">
            <template v-slot:first>
              <option :value="null" disabled>-- Please Pick a Structure --
              </option>
            </template>
          </b-select>
        </b-col>
       <b-col lg="2" sm="4" xs="12">
         <b-button class="w-50" variant="primary" @click.prevent="addStructureToProject">Add</b-button>
         <b-button variant="light" @click.prevent="showFieldStructures(false)">
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
      <b-table striped hover  v-if="structures.length"
               striped
               over
               :items="structures"
               :fields="structuresFields"
               :current-page="currentPage"
               :per-page="perPage"
               :filter="filter"
               :sort-by.sync="sortBy"
               :sort-desc.sync="sortDesc"
               responsive="sm">


        <template v-if="draftsman" v-slot:cell(adjudication)="row">
          <b-button v-if="getAdjudication(row.item.id, row.item.adjudications)==''" variant="danger" size="sm" class="m-2" @click.prevent="() => { reloadPage();createAdjudication(row.item.id,project.clientUsername,project.name);}">Sent To Approval</b-button>
          <div v-else>
            <span>{{ getAdjudication(row.item.id, row.item.adjudications) }}</span>
          </div>
        </template>
        <template v-else-if="client" v-slot:cell(adjudication)="row">
            <span>{{ getAdjudication(row.item.id, row.item.adjudications) }}</span>
        </template>

        <template v-if="draftsman" v-slot:cell(actions)="row">
          <b-dropdown split :split-to="`/structure/${row.item.id}`" text="Details" class="m-2">
            <b-dropdown-item v-b-modal.modal-delete-item @click.prevent="sendInfo(row.item.id)">Remove</b-dropdown-item>
          </b-dropdown>
        </template>
        <template v-else v-slot:cell(actions)="row">
          <b-button :to="`/structure/${row.item.id}`" text="Details" class="m-2">Details</b-button>
        </template>

      </b-table>
      <p v-else>No structures associated.</p>

      <!-- modal to Delete -->
      <b-modal id="modal-delete-item" centered title="Confirm Delete?">
        <p class="my-4">Are you sure you want to delete {{deletedItem}}?</p>
        <template #modal-footer="{ Confirm, cancel }">
          <b-button size="sm" variant="secondary" @click="cancel()">
            Cancel
          </b-button>
          <b-button size="sm" variant="primary" @click="deleteItem(deletedItem)">
            Confirm
          </b-button>
        </template>
      </b-modal>

    </form>
  </b-container>
</template>
<script>
export default {
  middleware: 'auth',
  data () {
    return {
      sortBy: 'name',
      sortDesc: false,
      currentPage: 1,
      perPage: 5,
      filter: null,
      project: {},
      status: [],
      structures: {},
      structuresFields: ['name', 'project', 'type', 'draftsman', 'create_at','adjudication','actions'],
      documentsFields: [{key: 'namePath', label:"Name"}, 'actions'],
      statusName: {},
      deletedItem: '',
      client: false,
      draftsman: false,
      supplier: false, // in case of future need
      listStructures: [],
      listStructureSelected: null,
      buttonAdd: false,
      fieldListStructures: true,
    }
  },
  computed: {
    name () {
      return this.$route.params.name
    },
    subjects () {
      return this.project.subjects || []
    },
    documents () {
      return this.project.files || []
    },
    isStatusValid (){
      if (!this.statusName) {
        return null
      }
      return this.status.some(c => this.statusName === c)
    },
    isStructureValid (){
      if (!this.listStructureSelected) {
        return null
      }
      return this.listStructures.some(s => this.listStructureSelected === s.id)
    },
    isFormValid () {
      if (!this.statusName) {
        return false
      }
      return true
    },
  },
  async created () { // on Page Load
    // Get Type of Role LoggedIn
    if(this.$auth.$state.loggedIn){
      if(this.$auth.user.groups.includes('Draftsman'))
        this.draftsman = true

      if(this.$auth.user.groups.includes('Client'))
        this.client = true
      else
        this.supplier = true
    }

    await this.$axios.$get(`/api/projects/${this.name}`)
      .then((project) => {
        this.project = project || {}
        //this.structures = project.structures || {}
      })
    await this.$axios.$get('/api/projects/status')
      .then((s) => {
        this.status = s;
        this.statusName = s.find(st => this.project.status === st)
    })
    // TODO [Demo Purpose]- Change Later to reduce the request
    this.$axios.$get(`/api/structures/project/${this.name}`)
      .then((data) => {
        this.structures = data
      })

  },
  methods: {
    // TODO - Temporally
    reloadPage(){
      window.location.reload()
    },
    sendInfo(item) {
      this.deletedItem = item;
    },
    getAdjudication (id,list){
      let value =  list.find((adjudicacao) => adjudicacao.project === this.project.name && adjudicacao.structure ===  id && adjudicacao.client === this.project.clientUsername)
      return value !== undefined ? value.adjudicate : ''
    },
    createAdjudication (structureId,clientName,projectname){
      this.$axios.post(`/api/structures/adjudication`,{
        client: clientName,
        structure: structureId,
        project: projectname
        })
        .then(() => {
          this.$toast.success('Adjudication sent!', { duration: 2000 })
        })
        .catch(() => {
          this.$toast.error('Unable to sent Adjudication!', { duration: 2000 })
        })
    },
    deleteItem(item) {
      this.$axios.$delete(`/api/structures/${item}`)
        .then(() => {
          let idx = this.structures.findIndex(i => i.id === item)
          this.structures.splice(idx,1)
          this.$toast.success('Row deleted successfully!',{ duration: 2000 })
        })
        .catch((err) => {
          console.log(err);
          this.$toast.error('Unable to Delete Item!', { duration: 2000 })
        })

      this.$bvModal.hide('modal-delete-item')
    },
    update () {
      this.$axios.$put(`/api/projects/${this.name}`,
        {
          name: this.name,
          clientUsername: this.project.clientUsername,
          draftsmanUsername: this.$auth.user.sub,
          status: this.statusName,
        })
        .then(this.$toast.success('Updated successfully!',{ duration: 2000 }))
        .catch((err) => {
          this.$toast.error('Error whille updating!',{ duration: 2000 })
          console.log(err)
        })
    },
    getStructuresList (){
      this.$axios.$get(`/api/structures`)
        .then((listOfStructures) => {
          this.listStructures = listOfStructures || {}
        })
      .catch((err) => console.log(err))
    },
    showFieldStructures (value){
      if (value == true) { // show div
        this.buttonAdd = true;
        this.fieldListStructures = false;
        this.getStructuresList ();
      }else{ // hide Field
        this.buttonAdd = false;
        this.fieldListStructures = true;
      }
    },
    updateStructure (id,structure){
      this.$axios.$put(`/api/structures/${this.listStructureSelected}`,
        {
          name: structure.name,
          dimensions: structure.dimensions,
          calculationParameters: structure.calculationParameters,
          type: structure.type,
          project: this.project.name,
          draftsman: this.$auth.user.sub
        })
        .then(() => { return true; })
        .catch((err) => {
          return false;
          console.log(err)
        })
    },
    addStructureToProject () {
      if (this.listStructureSelected > 0){
        try{
          let structure = this.listStructures.find(s => this.listStructureSelected === s.id)

          // Update Structure [ Temporally -> future change because of too many calls ]
          if (this.updateStructure(this.listStructureSelected ,structure) == false){
            this.$toast.error(`Error adding item.`)
            return
          }

          if (structure != undefined || structure.length != 0) { // if array not empty
            // if structures already have structure
            if(this.structures.find(st => st.id === this.listStructureSelected) === undefined){
              this.structures.push(structure)
              this.$toast.success(`Added item.`)
            }

          }else{
            this.$toast.error(`Error adding item.`)
          }
        }catch (err){
          this.$toast.error(`Error adding item.`)
        }
      }
    },
    download (documentId, name) {
      this.$axios.$get('/api/file/download/' + documentId, { responseType: 'arraybuffer' })
        .then((file) => {

          const url = window.URL.createObjectURL(new Blob([file]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', name)
          document.body.appendChild(link)
          link.click()
        })
    }
  }
}
</script>
