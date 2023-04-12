<template>
  <article>
    <header><h2>Home page</h2></header>
    <section v-show="section === 'home'">
      <Login @login="displayMessage" @loginuser="loginuserpage" />
      <b-button @click="section = 'registration'">Sign in</b-button>
    </section>

    <section v-show="section === 'registration'">
      <Registration
        :applicant="'consumer'"
        :role="'consumer'"
        @registration="registration"
      />
      <b-button @click="section = 'home'">Already have an account</b-button>
    </section>

    <Toasts :error="error" :warning="warning" :success="success" />

  </article>
</template>

<script>
import Login from "../components/functions/Login.vue";
import Registration from "../components/functions/Registration.vue";
import Toasts from "../components/layout/Toasts.vue";
export default {
  name: "Home_page",
  components: {
    Registration,
    Login,
    Toasts,
  },

  data() {
    return {
      section: "home",
      error: "",
      warning: "",
      success: "",
    };
  },
  methods: {
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
    //--------------------------------------------

    loginuserpage(username) {
      setTimeout(() => {
        if (username.includes("@")) {
          this.$router.push("/admin_page");
        } else if (username.length === 4) {
          this.$router.push("/uploader_page");
        } else if (username.length === 16) {
          this.$router.push("/consumer_page");
        } else {
          this.displayMessage("ERR: User unrecognized");
        }
      }, 3000);
    },
    registration(message) {
      this.displayMessage(message);
      this.section = "home";
    },
  },
  created() {
    console.clear();
    console.log(process.env.VUE_APP_APIROOT);
  },
};
</script>