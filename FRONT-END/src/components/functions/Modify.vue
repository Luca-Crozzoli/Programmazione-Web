<template>
  <section>
    <header>
      <h3>Modify Information {{ role }}</h3>
    </header>
    <b-form @submit.prevent="modify" @reset="reset">
      <!--.sync per fare un binding bidirezionale-->
      <Credential
        :applicant="applicant"
        :required="false"
        :context="'modify'"
        :username.sync="username"
        :password.sync="password"
      />
      <!--.sync per far un binding bidirezionale-->
      <AccountInfo :required="false" :context="'modify'" :name.sync="name" :email.sync="email" />

      <Logo v-if="role === 'uploader'" :required="false" :logo.sync="logo" />

      <b-button type="submit" variant="success">Modify</b-button>
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
  name: "ModInfo",
  props: ["applicant", "role"], //applicant chi richeide che avvenga la modifica, role: ruolo dell'utente da modificare
  components: {
    Credential,
    AccountInfo,
    Logo
  },
  data() {
    return {
      username: "",
      password: "",
      name: "",
      email: "",
      logo:"",
    };
  },
  methods: {
    modify() {
      axios
        .post(
          `${process.env.VUE_APP_APIROOT}/accounts/modify`,
          {
            username: this.username,
            password: this.password,
            name: this.name,
            email: this.email,
            role: this.role,
            logo: this.logo
          }
        )
        .then((res) => {
          const actor = {
            user: res.data,
            role: this.role,
          };
          this.$emit("modinfouser", actor);
          if (this.username !== ""){
            this.$emit("modInfo", "Actor modified - " + this.username); //utilizzato per visulaizzare mesaggi
          }else{
            this.$emit("modInfo", "Self info modified"); //utilizzato solamente per essere visualizzzto nei messaggi
          }
          this.reset();
        })
        .catch((err) => {
          this.$emit("modInfo", err.response.data); //utilizzato solamente per mvisualizzare dei messagi d'errore
        })
    },
    reset() {
      this.username = "";
      this.password = "";
      this.name = "";
      this.email = "";
      this.logo=""
    }
  }
};
</script>