<template>
  <b-container fluid>
    <b-form-group id="input-grouplogo" label="Logo:" :label-for="idLogo">
      <b-form-file
        @change="handlelogo"
        :id="idLogo"
        type="file"
        name="logoInput"
        v-model="logoInput"
        placeholder="Choose an image or drop it here..."
        drop-placeholder="Drop image here..."
        accept="image/*"
        required
      />
    </b-form-group>
    <b-form-group label="Logo preview:" v-show="logoStringInput">
      <b-card>
        <b-avatar size="lg" :src="`${logoStringInput}`" alt="Logo preview" />
      </b-card>
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "Logo",
  props: ["required", "logo"],
  data() {
    return {
      idLogo: "",
      logoInput: null,
    };
  },
  methods: {
    //https://medium.com/js-dojo/how-to-upload-base64-images-in-vue-nodejs-4e89635daebc
    handlelogo(event) {
      const selectedlogo = event.target.files[0];
      this.transformbase64(selectedlogo);
    },
    transformbase64(fileObject) {
      const reader = new FileReader();

      reader.onload = (event) => {
        this.logoStringInput = event.target.result;
      };
      reader.onerror = function (err) {
        console.log("Error: ", err);
      };
      reader.readAsDataURL(fileObject);
    },
  },
  watch: {
    logoStringInput: function () {
      if (this.logoStringInput === "") {
        this.logoInput = null;
      }
    },
  },
  computed: {
    logoStringInput: {
      get: function () {
        return this.logo;
      },
      set: function (newValue) {
        this.$emit("update:logo", newValue);
      },
    },
  },

  mounted() {
    if (this.required !== true) {
      document.getElementById(this.idLogo).removeAttribute("required");
    }
  },
  created() {
    this.idLogo = uuidv4();
  },
};
</script>
