<template>
  <article>
    <header>
      <Navigationbar
        :applicant="'uploader'"
        :mainReport="'Consumers'"
        :secondaryReport="'Files'"
        @showSection="showSection"
        @formRole="manageRole"
        @logout="displayMessage"
        >Uploader {{ usernameUploader }}
      </Navigationbar>
    </header>

    <section v-show="section === 'home'">
      <header><h3>Consumer List</h3></header>

      <List
        :items="consumerList"
        :fields="consumerListFields"
        :busy="loadData"
        @showFiles="showFiles"
      />
    </section>

    <section v-show="section === 'list2'">
      <header>
        <h3>Files of {{ consumerSelected }}</h3>
      </header>

      <List
        :items="consumerFilesList"
        :fields="consumerFilesListFields"
        :busy="loadData"
      />
    </section>

    <Registration
      v-show="section === 'registration'"
      :applicant="'uploader'"
      :role="'consumer'"
      @registrationuser="registration_user"
      @registration="displayMessage"
    />

    <ModInfo
      v-show="section === 'modifyInfo'"
      :applicant="'uploader'"
      :role="formRole"
      @modinfouser="modinfo_user"
      @modInfo="displayMessage"
    />

    <Load
      v-show="section === 'load'"
      @uploadconsumer="upload_consumer"
      @uploadfile="upload_file"
      @upload="displayMessage"
    />

    <Delete
      v-show="section === 'delete'"
      :applicant="'uploader'"
      :listFile="listFile"
      :listUsername="listUsername"
      @deleteusername="delete_user"
      @deletefile="delete_file"
      @delete="displayMessage"
    />

    <Toasts :error="error" :warning="warning" :success="success" />
  </article>
</template>

<script>
import List from "../components/layout/List.vue";
import Navigationbar from "../components/layout/Navigationbar.vue";
import Delete from "../components/functions/Delete.vue";
import Load from "../components/functions/Load.vue";
import ModInfo from "../components/functions/Modify.vue";
import Registration from "../components/functions/Registration.vue";
import Toasts from "../components/layout/Toasts.vue";
import axios from "axios";

export default {
  name: "Uploader_page",
  components: {
    Registration,
    ModInfo,
    Load,
    Delete,
    Navigationbar,
    List,
    Toasts,
  },
  data() {
    return {
      usernameUploader: "",
      section: "home",
      error: "",
      warning: "",
      success: "",

      formRole: "",
      consumerSelected: "",

      uploaderFilesList: [],
      consumerList: [],
      consumerListFields: ["name", "files", "info"],
      consumerFilesList: [],
      consumerFilesListFields: [
        "id",
        "name",
        { key: "dataUpload", sortable: true },
        { key: "dataView", sortable: true },
        "indIp",
        "hashtag",
      ],
      loadData: false,
    };
  },
  computed: {
    listUsername: function () {
      let listAccounts = [];
      this.consumerList.forEach((item) => {
        listAccounts.push(item.username);
      });
      return listAccounts;
    },
    listFile: function () {
      let listFiles = [];
      this.uploaderFilesList.forEach((item) => {
          listFiles.push(item.id);
      });
      return listFiles;
    },
  },
  methods: {
    updateBartitle() {
      this.bartitle = localStorage.getItem("Username");
    },
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

    manageRole(roleIn) {
      this.formRole = roleIn;
    },

    showFiles(usernameConsumer) {
      this.consumerSelected = usernameConsumer;
      this.consumerFilesList = null;
      this.consumerFilesList = this.uploaderFilesList.filter(
        (item) => item.usernameCons === usernameConsumer
      );
      this.section = "list2";
    },

    registration_user(newUser) {
      const { username, name, email } = newUser;
      const consumer = {
        username: username,
        name: name,
        email: email,
      };
      this.consumerList.push(consumer);
    },

    modinfo_user(userIn) {
      const { user, role } = userIn;
      if (role === "consumer") {
        this.consumerList = this.consumerList.filter(
          (item) => item.username === user.username
        );
        this.consumerList.push(user);
      }
    },

    upload_consumer(newConsumer) {
      const { username } = newConsumer;
      if (
        this.consumerList.findIndex((el) => el.username === username) === -1
      ) {
        this.consumerList.push(newConsumer);
      }
    },
    upload_file(fileLoaded) {
      this.uploaderFilesList.push(fileLoaded);
      if (fileLoaded.usernameCons === this.consumerSelected) {
        this.consumerFilesList.push(fileLoaded);
      }
      this.orederingFile();
    },
    delete_user(usernameIn) {
      this.consumerList = this.consumerList.filter(
        (item) => item.username !== usernameIn
      );
      this.uploaderFilesList = this.uploaderFilesList.filter(
        (item) => item.usernameConsumer !== usernameIn
      );
      if (usernameIn === this.consumerSelected) {
        this.consumerSelected = "";
        this.consumerFilesList = [];
      }
    },
    delete_file(fileInId) {
      this.uploaderFilesList = this.uploaderFilesList.filter(
        (item) => item.id !== fileInId
      );
      this.consumerFilesList = this.consumerFilesList.filter(
        (item) => item.id !== fileInId
      );
    },

    orederingFile() {
      this.uploaderFilesList.sort(function (a, b) {
        if ((a.visualizationDate === "") ^ (b.visualizationDate === "")) {
          //^XOR
          if (a.visualizationDate < b.visualizationDate) return -1;
          if (a.visualizationDate > b.visualizationDate) return 1;
        }
        if (a.loadDate < b.loadDate) return 1;
        if (a.loadDate > b.loadDate) return -1;
        return 0;
      });
      this.consumerFilesList = this.uploaderFilesList.filter(
        (item) => item.usernameConsumer === this.consumerSelected
      );
    },
  },
  created() {
    this.loadData = true;
    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/consumers`)
      .then((res) => (this.consumerList = res.data))
      .catch((err) => this.displayMessage(err.toString()));

    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/uploader/files`)
      .then((res) => {
        this.uploaderFilesList = res.data;
        console.log("response from uploader files api: " + res.data);
      })
      .catch((err) => this.displayMessage(err.toString()))
      .finally(() => {
        this.loadData = false;
      });
  },
  mounted() {
    this.usernameUploader = localStorage.getItem("Username");
  },
};
</script>