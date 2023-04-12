<template>
  <section>
    <header>
      <h3>Registration {{ role }}</h3>
    </header>

    <b-form @submit.prevent="Register" @reset="reset">
      <!--.sync per fare un binding bidirezionale-->
      <AccountInfo :required="true" :name.sync="name" :email.sync="email" />
      <!--.sync per fare un binding bidirezionale-->
      <Credential
        :required="true"
        :context="role"
        :username.sync="username"
        :password.sync="password"
      />
      <!--.sync per fare un binding bidirezionale-->
      <Logo v-if="role === 'uploader'" :required="true" :logo.sync="logo" />

      <b-button type="submit" variant="success">Register</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
  </section>
</template>

<script>
import Credential from "../values/Credential.vue";
import AccountInfo from "../values/AccountInfo.vue";
import Logo from "../values/Logo.vue";
import axios from "axios";

export default {
  name: "Registration",
  props: ["applicant", "role"], //applicant== chi richiede la registarazione, role == che tipo di utente registro
  components: {
    Credential,
    AccountInfo,
    Logo,
  },
  data() {
    return {
      username: "",
      password: "",
      name: "",
      email: "",
      logo: "",
    };
  },
  methods: {
    Register() {
      axios
      .post(`${process.env.VUE_APP_APIROOT}/accounts/registration`, {
          username: this.username,
          password: this.password,
          name: this.name,
          email: this.email,
          role: this.role, 
          logo: this.logo,
        })
        .then((res) => {
          const newUser = {
            username: this.username,
            password: this.password,
            name: this.name,
            email: this.email,
            role: this.role, //riferimento alle props.
            logo: this.logo,
          };
          this.$emit("registrationuser", newUser);//aggiungere gli utenti nuovi alla lista
          this.$emit("registration", res.data); //usato per trasemttere il risultato della respons tramite funzonalità messaggi
          this.reset();
        })
        .catch((err) => {
          this.$emit("registration", err.response.data); //usato per trasemttere l'errore tramite le funzionalità messaggi
        })
    },
    reset() {
      this.username = "";
      this.password = "";
      this.name = "";
      this.email = "";
      this.logo=""
    },
  },
};
</script>