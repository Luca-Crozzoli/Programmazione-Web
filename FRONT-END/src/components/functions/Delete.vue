<template>
  <article>
    <section>
      <header><h3>Delete Account</h3></header>
      <b-container fluid>
        <b-form @submit.prevent="deleteActor">
          <label for="username">Username account to delete</label>
          <b-form-input
            list="username-list"
            id="username"
            type="text"
            name="username"
            v-model="username"
            placeholder="username"
            required
          ></b-form-input>
          <b-form-datalist
            id="username-list"
            :options="listUsername"
          ></b-form-datalist>
          <b-button type="submit" variant="danger">Delete</b-button>
        </b-form>
      </b-container>
    </section>

    <section v-if="applicant === 'uploader'">
      <header><h3>Delete File</h3></header>
      <b-container fluid>
        <b-form @submit.prevent="deleteFile">
          <label for="file">File Id</label>
          <b-form-input
            list="file-list"
            id="file"
            type="text"
            name="file"
            v-model="fileId"
            placeholder="file Id"
            required
          ></b-form-input>
          <b-form-datalist id="file-list" :options="listFile"></b-form-datalist>
          <b-button type="submit" variant="danger">Delete</b-button>
        </b-form>
      </b-container>
    </section>
  </article>
</template>

<script>
import axios from "axios";

export default {
  name: "Delete",
  props: ["applicant", "listUsername", "listFile"],
  data() {
    return {
      username: "",
      fileId: "",
    };
  },
  methods: {
    deleteActor() {
      axios
        .delete(
          `${process.env.VUE_APP_APIROOT}/accounts/delete/${this.username}`
        )
        .then((res) => {
          this.$emit("deleteusername", this.username);
          this.username = "";
          this.$emit("delete", res.data); //usato per trasmetterei dati della respone tramite la funzionalità messaggi
        })
        .catch((err) => {
          this.$emit("delete", err.response.data); // usato per trasmetter l'errore tramiete la funzionalità messaggio
        })
    },
    deleteFile() {
      axios
        .delete(`${process.env.VUE_APP_APIROOT}/files/delete/${this.fileId}`)
        .then((res) => {
          this.$emit("deletefile", this.fileId);
          this.fileId = "";
          this.$emit("delete", res.data); //usato per trasmetterei dati della respone tramite la funzionalità messaggi
        })
        .catch((err) => {
          this.$emit("delete", err.response.data); //usato per trasmetter l'errore tramiete la funzionalità messaggio
        })
    },
  },
};
</script>
