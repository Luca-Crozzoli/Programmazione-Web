<template>
  <section>
   <header><h3>Login</h3></header>
    <b-form @submit.prevent="login">
      <!--.sync per fare un binding bidirezionale-->
      <Credential
        :required="true"
        :username.sync="username"
        :password.sync="password"
      />
      <b-button type="submit" variant="primary">Login</b-button>
    </b-form>
  </section>
</template>

<script>
import Credential from "../values/Credential.vue";
import axios from "axios";

export default {
  name: "Login",
  components: {
    Credential,
  },
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    login() {
      axios
        .post(
          `${process.env.VUE_APP_APIROOT}/login`,
          {
            username: this.username,
            password: this.password,
          })
        //sezione per ricevere il token che viene dato come risposta al login se questo va a buon fine. Local storage
        //viene inteso come lo spazio dedicato al salvataggio di dati da parte del browser
        .then((res) => {
          // https://stackoverflow.com/a/44534672
          localStorage.setItem("JWTToken", res.data);
          localStorage.setItem("Username", this.username);
          this.$emit("loginuser", this.username);
          this.$emit("login", "Login done - " + this.username);
          
        })
        .catch((err) => {
          this.$emit("login", err.response.data);
        })
    },
  },
};
</script>
