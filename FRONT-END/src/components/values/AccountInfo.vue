<template>
  <b-container fluid>
    <b-form-group label="Name/Surname:" :label-for="nameInputId">
      <b-form-text v-show="context === 'modify'"
        >New Name and Surname, leave empty if you don't want to modify it
      </b-form-text>
      <b-form-text v-show="context === 'load'">
        If the consumer is a new one enter Name/Surname
      </b-form-text>
      <b-form-input
        :id="nameInputId"
        type="text"
        name="nameInput"
        v-model="nameInput"
        maxlength="100"
        placeholder="Enter name"
        required
      />
    </b-form-group>

    <b-form-group label="Email:" :label-for="emailInputId">
      <b-form-text v-show="context === 'modify'"
        >New mail, leave empty if you don't want to modify it</b-form-text
      >
      <b-form-text v-show="context === 'load'">
        If the consumer is a new one enter email
      </b-form-text>
      <b-form-input
        :id="emailInputId"
        type="email"
        name="emailInput"
        v-model="emailInput"
        maxlength="100"
        placeholder="Enter email"
        required
      />
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";
export default {
  name: "Userinfo",
  props: ["required", "context", "name", "email"],
  data() {
    return {
      nameInputId: "",
      emailInputId: "",
    };
  },
  computed: {
    nameInput: {
      get: function () {
        return this.name;
      },
      set: function (newValue) {
        this.$emit("update:name", newValue);
      },
    },
    emailInput: {
      get: function () {
        return this.email;
      },
      set: function (newValue) {
        this.$emit("update:email", newValue);
      },
    },
  },
  mounted() {
    if (this.required !== true) {
      document.getElementById(this.nameInputId).removeAttribute("required");
      document.getElementById(this.emailInputId).removeAttribute("required");
    }
  },
  created() {
    this.nameInputId = uuidv4();
    this.emailInputId = uuidv4();
  },
};
</script>