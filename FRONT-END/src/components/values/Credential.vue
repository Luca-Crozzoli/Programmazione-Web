<template>
  <b-container fluid>
    <b-form-group  v-show="applicant !== 'consumer'" label="Username:" :label-for="usernameInputId">
      <b-form-text v-show="context === 'modify'"
        >Select the account you want to modify.
        <strong 
          >If you leave the username blank your account will be modifed</strong
        ></b-form-text
      >
      <b-form-input 
        :id="usernameInputId"
        type="text"
        name="usernameInput"
        v-model="usernameInput"
        placeholder="Enter username"
        required
      />
    </b-form-group>

    <b-form-group label="Password:" :label-for="passwordInputId">
      <b-form-text v-show="context === 'modify'">
        Insert the new password
      </b-form-text>
      <b-form-input
        :id="passwordInputId"
        type="password"
        name="passwordInput"
        v-model="passwordInput"
        placeholder="Enter password"
        required
      />
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";
export default {
  name: "Credential",
  props: ["applicant","required", "context", "username", "password"], //context constesto in cui son usate le credenziali
  data() {
    return {
      usernameInputId: "",
      passwordInputId: "",
    };
  },
  computed: {
    usernameInput: {
      get: function () {
        //return in usernameInput the value from the parent (username props)
        return this.username;
      },
      set: function (newValue) {
        this.$emit("update:username", newValue);
      },
    },
    passwordInput: {
      get: function () {
        //return in passwordINput the value from th eparent (password props)
        return this.password;
      },
      set: function (newValue) {
        this.$emit("update:password", newValue);
      },
    },
  },

  mounted() {
    //remove the HTML attribute for the elements form input in the document
    if (this.required !== true) {
      document.getElementById(this.usernameInputId).removeAttribute("required");
      document.getElementById(this.passwordInputId).removeAttribute("required");
    }
  },
  created() {
    this.usernameInputId = uuidv4();
    this.passwordInputId = uuidv4();
  },
};
</script>
