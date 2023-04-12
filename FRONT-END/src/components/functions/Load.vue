<template>
  <section>
    <header><h3>Upload file</h3></header>
 <!--https://medium.com/front-end-weekly/vues-v-model-directive-vs-sync-modifier-d1f83957c57c
 https://v3.vuejs.org/guide/migration/v-model.html#_2-x-syntax-->
    <b-form @submit.prevent="upload" @reset="reset">
      <File
        :file.sync="file"
        :fileName.sync="fileName"
        :hashtag.sync="hashtag"
        :extension.sync="extension"
      />
      <b-container fluid>
        <b-form-group
          id="consumer-group"
          label=" Username Consumer:"
          label-for="usernameConsumer"
        >
          <b-form-input
            id="usernameConsumer"
            type="text"
            name="usernameConsumer"
            v-model="usernameConsumer"
            placeholder="username consumer"
          />
        </b-form-group>
      </b-container>
      <AccountInfo
        :context="'load'"
        :required="false"
        :name.sync="nameConsumer"
        :email.sync="emailConsumer"
      />

      <b-button type="submit" variant="success">Upload</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
  </section>
</template>

<script>
import AccountInfo from "../values/AccountInfo.vue";
import File from "../values/File.vue";
import axios from "axios";

export default {
  name: "Load",
  components: {
    File,
    AccountInfo,
  },

  data() {
    return {
      file: "",
      fileName: "",
      hashtag: "",
      extension: "",
      usernameConsumer: "",
      nameConsumer: "",
      emailConsumer: "",
    };
  },
  methods: {
    upload() {
      axios
        .post(`${process.env.VUE_APP_APIROOT}/files/upload`, {
          file: this.file,
          fileName: this.fileName + this.extension,
          hashtag: this.hashtag,
          usernameConsumer: this.usernameConsumer,
          nameConsumer: this.nameConsumer,
          emailConsumer: this.emailConsumer,
        })
        .then((res) => {
          const consumer = {
            username: this.usernameConsumer,
            name: this.nameConsumer,
            email: this.emailConsumer,
            logo: "",
          };
          this.$emit("uploadconsumer", consumer);
          this.$emit("uploadfile", res.data);
          this.$emit("upload", "Upload :" + this.fileName + " completed");//usato per inviare un messaggio
          this.reset();
        })
        .catch((err) => {
          this.$emit("upload", err.response.data); //usato per trasemttere l'errore tramite le funzionalità messaggi
        })
    },
     reset() {
      this.file = "";
      this.fileName = "";
      this.hashtag = "";
      this.extension = "";
      this.usernameConsumer = "";
      this.nameConsumer = "";
      this.emailConsumer = "";
    },
  },
};
</script>
