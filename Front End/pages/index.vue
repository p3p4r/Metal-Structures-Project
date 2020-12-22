<template>
  <b-container>
    <h1 class="text-center color-gray">Welcome to Dashboard</h1>
    <hr class="mb-5">

    <b-card-group deck>
      <b-card
        class="bg-grad"
        footer-tag="footer"
      >
        <b-card-text style="height: 100%;">
         <b-row style="height: inherit;">
           <b-col align="center" class="align-self-center" style="font-size: 5rem;">
             <b-icon icon="folder" style="color:#62afff;" />
           </b-col>
           <b-col align="center" class="align-self-center">
             <p>Number of Projects</p>
             <h1>25</h1>
           </b-col>
         </b-row>
        </b-card-text>
        <template #footer>
          <b-icon icon="clock" class="mr-2"/> <em>Last Day</em>
        </template>
      </b-card>

      <b-card
        class="bg-grad"
        footer-tag="footer"
      >
        <b-card-text style="height: 100%;">
          <b-row style="height: inherit;">
            <b-col align="center" class="align-self-center" style="font-size: 5rem;">
              <b-icon icon="box" style="color:#499184;" />
            </b-col>
            <b-col align="center" class="align-self-center">
              <p>Number of Materials</p>
              <h1>265</h1>
            </b-col>
          </b-row>
        </b-card-text>
        <template #footer>
          <b-icon icon="calendar" class="mr-2"/> <em>Last Day</em>
        </template>
      </b-card>

      <b-card class="bg-grad"
              footer-tag="footer"
              >

      <b-card-text style="height: 100%;">
          <b-row style="height: inherit;">
            <b-col align="center" class="align-self-center" style="font-size: 5rem;">
              <b-icon icon="person-fill" style="color:#ff6262;" />
            </b-col>
            <b-col align="center" class="align-self-center">
              <p>Number of Users</p>
              <h1>1258</h1>
            </b-col>
          </b-row>
        </b-card-text>

        <template #footer>
          <b-icon icon="arrow-clockwise" class="mr-2"/> <em>Updated Now</em>
        </template>
      </b-card>
    </b-card-group>

  </b-container>
</template>
<style>
 .bg-grad{
   background: #f6f6f6;  /* fallback for old browsers */
   background: -webkit-linear-gradient(to right, #eef2f3,#eeeeee);  /* Chrome 10-25, Safari 5.1-6 */
   background: linear-gradient(to right, #ffffff, #eeeeee) /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
 }
</style>
