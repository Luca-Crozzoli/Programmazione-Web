<template>
  <b-button @click="logout" variant="primary">Logout</b-button>
</template>

<script>
import axios from "axios";
export default {
  name: "Logout",
  methods: {
    logout() {
      axios.get(`${process.env.VUE_APP_APIROOT}/logout`).then((res) => {
        this.$emit("logout", res.data);
        localStorage.removeItem("JWTToken");
        localStorage.removeItem("Username");
        this.$router.push("/");
      });
    },
  },
};
</script>