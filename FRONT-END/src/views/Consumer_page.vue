<template>
    <article>
      <hader>
        <Navigationbar
          :applicant="'consumer'"
          :mainReport="'Uploaders'"
          :secondaryReport="'Uploader Files'"
          @showSection="showSection"
          @logout="displayMessage"
          >Consumer {{ usernameConsumer }}
        </Navigationbar>
      </hader>

      <section v-show="section === 'home'">
        <header><h3>Uploader List</h3></header>

        <List
          :items="uploaderList"
          :fields="uploaderListFields"
          :busy="loadData"
          @showFiles="showFiles"
        />
      </section>

      <section v-show="section === 'list2'">
        <header>
          <h3>Files uploaded by {{ uploaderConfirmed.name }}</h3>
        </header>
        <b-img
          id="Pagelogo"
          rounded="circle"
          :src="`${uploaderConfirmed.logo}`"
          alt="uploader logo"
        ></b-img>

        <b-form inline @submit.prevent="hashtagFilter">
          <b-form-input
            id="hashtag"
            name="hashtag"
            type="text"
            maxlength="100"
            v-model="hashtag"
            placeholder="hashtag"
          />
          <b-button type="submit" variant="primary" size="sm"
            >Filter hashtag
          </b-button>
          <!--se non ho hashtag allora rimane vuoto altimenti fai il filtro-->
          <b-button
            variant="danger"
            size="sm"
            @click="(hashtag = ''), hashtagFilter()"
          >
            Reset</b-button
          >
        </b-form>

        <List
          :items="uploaderFilesList"
          :fields="uploaderFilesListFields"
          :busy="loadData"
          @downloadFile="download"
        />
      </section>

      <Modify
        v-show="section === 'modifyInfo'"
        :applicant="'consumer'"
        :role="'consumer'"
        @modInfo="displayMessage"
      />
      <Toasts :error="error" :warning="warning" :success="success" />
    </article>
</template>

<script>
import Modify from "../components/functions/Modify.vue";
import Navigationbar from "../components/layout/Navigationbar.vue";
import List from "../components/layout/List.vue";
import Toasts from "../components/layout/Toasts.vue";
import axios from "axios";

export default {
  name: "Consumer_page",
  components: {
    Modify,
    Navigationbar,
    List,
    Toasts,
  },
  data() {
    return {
      usernameConsumer: "",
      section: "home",
      error: "",
      warning: "",
      success: "",

      hashtag: "",
      uploaderConfirmed: "",
      consumerFilesList: [],
      uploaderList: [],
      uploaderListFields: ["logo", "name", "info", "files"],
      uploaderFilesList: [],
      uploaderFilesListFields: [
        "name",
        { key: "dataUpload", sortable: true },
        { key: "dataView", sortable: true },
        "hashtag",
        "download",
      ],
      loadData: false,
    };
  },
  watch: {
    uploaderConfirmed: function () {
      if (this.uploaderConfirmed !== "")
        this.showFiles(this.uploaderConfirmed.username);
    },
  },
  methods: {
    showSection(sectionin) {
      this.section = sectionin;
    },
    displayMessage(message) {
      if (message.startsWith("ERR") || message.startsWith("Er")) {
        this.error = message;
        this.$bvToast.show("error_toast");
      } else if (message.startsWith("WAR")) {
        this.warning = message;
        this.$bvToast.show("warning_toast");
      } else {
        this.success = message;
        this.$bvToast.show("success_toast");
      }
    },
    //-----------------------------------------

    showFiles(usernameUploader) {
      this.section = "list2";
      this.uploaderFilesList = this.consumerFilesList.filter(
        (item) => item.usernameUpl === usernameUploader
      );
      this.uploaderList.every((item) => {
        if (item.username === usernameUploader) {
          this.uploaderConfirmed = item;
          return false;
        }
        return true;
      });
    },
    orederingFile() {
      this.consumerFilesList.sort(function (a, b) {
        if ((a.visualizationDate === "") ^ (b.visualizationDate === "")) {//^XOR
          if (a.visualizationDate < b.visualizationDate) return -1;
          if (a.visualizationDate > b.visualizationDate) return 1;
        }//piu recente in termine di load date
        if (a.loadDate < b.loadDate) return 1;
        if (a.loadDate > b.loadDate) return -1;
        return 0;
      });
      this.uploaderFilesList = this.consumerFilesList.filter(
        (item) => item.usernameUpl === this.uploaderConfirmed.username
      );
    },
    download(fileId) {
      this.loadData = true;
      axios
        .get(`${process.env.VUE_APP_APIROOT}/files/download/${fileId}`, {
          responseType: "arraybuffer",
        })
        .then((res) => {
          let indexCurrent = this.consumerFilesList.findIndex(
            (file) => file.id === fileId
          );

          //https://stackoverflow.com/questions/53772331/vue-html-js-how-to-download-a-file-to-browser-using-the-download-tag
          //https://developer.mozilla.org/en-US/docs/Web/API/Blob
          //https://stackoverflow.com/questions/60915291/download-a-png-file-served-as-binary-octet-stream
          const link = document.createElement("a");
          link.style.display = "none";
          link.download = this.consumerFilesList[indexCurrent].name; //label
          var blob = new Blob([res.data], { type: "application/octet-stream" });
          link.href = window.URL.createObjectURL(blob); 
          document.body.appendChild(link);
          link.click();
          window.URL.revokeObjectURL(link.href);
   
          if (this.consumerFilesList[indexCurrent].visualizationDate === "") {
            let now = new Date();
            let dataString =
              now.toISOString().substring(0, 10) +
              " " +
              now.getUTCHours(); 
            this.consumerFilesList[indexCurrent].visualizationDate =
              dataString;
            this.orederingFile();
          }
          this.displayMessage("Download done");
        })
        .catch((err) => {
          this.displayMessage(err.response.data);
        })
        .finally(() => {
          this.loadData = false;
        });
    },
    hashtagFilter() {
      this.uploaderFilesList = null;
      this.uploaderFilesList = this.consumerFilesList.filter(
        (item) =>
          item.hashtag.includes(this.hashtag) &&
          item.usernameUpl === this.uploaderConfirmed.username
      );
    },
  },
  created() {
    this.loadData = true;
    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/uploaders`)
      .then((res) => {
        this.uploaderList = res.data;
        if (this.uploaderList.length === 1)
          this.uploaderConfirmed = this.uploaderList[0];
      })
      .catch((err) => this.displayMessage(err.toString()));

    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/consumer/files`)
      .then((res) => {
        this.consumerFilesList = res.data;
        if (this.uploaderConfirmed != "")
          this.showFiles(this.uploaderConfirmed.username);
      })
      .catch((err) => this.displayMessage(err.toString()))
      .finally(() => {
        this.loadData = false;
      });
  },
  mounted() {
    this.usernameConsumer = localStorage.getItem("Username");
  },
};
</script>

<style scoped>
#Pagelogo {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 64px;
  height: 64px;
}
</style>