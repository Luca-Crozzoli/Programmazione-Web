<template>
  <b-container fluid>
    <b-row>
      <!-- opzioni delle pagine, quanti elementi si volgio visualizzare per pagina-->
      <b-col sm="6">
        <b-form-group
          label="For page:"
          :label-for="per_page_select"
          label-align="left"
          label-size="sm"
          label-cols="auto"
          content-cols="auto"
        >
          <b-form-select
            :id="per_page_select"
            v-model="perPage"
            :options="pageOptions"
            size="sm"
          ></b-form-select>
        </b-form-group>
      </b-col>

      <!-- indice delle pagine che sono presenti nel codice-->
      <b-col sm="6">
        <b-pagination
          v-model="currentPage"
          :total-rows="rows"
          :per-page="perPage"
          align="fill"
          size="sm"
        ></b-pagination>
      </b-col>
    </b-row>
    <!--inizio della tabella,
striped -> righe adiacenti colorate in modo diverso,
hover -> evidenzia la riga quadno passo sorpa col mouse 
stacked-> ci consente di mettere gli elementi di una
riga uno sopra l'altro per visualizzarli meglio quando lo schermo e small, 
responsive-> add an horizontal scrollbar if the table is too large
tbody-tr-class ->consente di cambiare lo stile di una singola riga andando
a richiamare un afunzione specifica che ha come parametri item, type
sticky header->mantiene fisso l'header mentre sulla tabella posso fare scroll

currentpage-> valore della pagina corrente di defualt parte da 1
perPage-> numero di righe da visualizzare per pagina
tbody-tr-class aspetto css dell'elemnto tablerow-->
    <b-table
      caption-top
      striped="true"
      hover="true"
      responsive="true"
      bordered="true"
      outlined="true"
      stacked="sm"
      head-variant="light"
      table-variant="secondary"
      sticky-header="600px"
      :items="items"
      :fields="fields"
      :current-page="currentPage"
      :per-page="perPage"
      :tbody-tr-class="highlightNew"
      :busy="busy"
    >
      <!--se sono nello stato di busy aggiungo uno spinner 
    table-busy Optional slot to place loading message when table is in busy state-->
      <template #table-busy>
        <div class="text-center text-primary my-2">
          <b-spinner class="align-middle"></b-spinner>
          <strong>Loading...</strong>
        </div>
      </template>
      <!--descrizione della tabella
    table-caption slot to diplay the table caption element-->
      <template #table-caption>{{ tableDescription }}</template>
      <!--cell(key) scoped slot for costum data rendering of field data key is the fields' key name-->
      <template #cell(logo)="data">
        <b-img
          rounded="circle"
          :src="`${data.item.logo}`"
          alt="Logo uploader"
        ></b-img
      ></template>

      <template #cell(info)="data">
        <b-button @click="data.toggleDetails">
          {{ data.detailsShowing ? "Hide" : "Show" }} Details
        </b-button>
      </template>

      <template #cell(files)="data">
        <b-button @click="$emit('showFiles', data.item.username)" variant="info"
          >Show files</b-button
        >
      </template>

      <template #cell(download)="data">
        <b-button @click="$emit('downloadFile', data.item.id)">
          Download
        </b-button>
      </template>

      <!-- template per i dettagli della tabella
      row-details scoped slot for optional rendering additional record details-->
      <template #row-details="data">
        <b-card>
          <b-row>
            <b-col sm="3"><b>Username:</b></b-col>
            <b-col>{{ data.item.username }}</b-col>
          </b-row>

          <b-row>
            <b-col sm="3"><b>Email:</b></b-col>
            <b-col>{{ data.item.email }}</b-col>
          </b-row>
        </b-card>
      </template>
    </b-table>

    <div v-if="items.length === 0"><p>No element</p></div>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "List",
  props: [
    "items",
    "fields",
    "busy",
    "tableDescription",
    "itemsUploader",
    "fieldsUploader",
  ],
  data() {
    return {
      currentPage: 1,
      perPage: 5,
      per_page_select: "",
      pageOptions: [5, 10, 15, { value: 100, text: "Show many" }],
    };
  },
  methods: {
    //utilizzato per evidenziare l'elemento non ancora visualizzato
    highlightNew(item, type) {
      if (!item || type !== "data") return;
      if (item.dataView === "") return "table-primary";
    },
  },

  computed: {
    totalRows() {
      return this.items.length;
    },
  },
  created() {
    this.per_page_select = uuidv4();
  },
};
</script>

<style scoped>
.btn {
  margin: 0px;
}
img {
  height: 5em;
  width: 5em;
}
</style>
