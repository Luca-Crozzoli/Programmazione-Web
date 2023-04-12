<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="dark">
      <b-navbar-brand class="ml-my" @click="$emit('showSection', 'home')"
        ><h2><slot></slot></h2
      ></b-navbar-brand>

      <b-navbar-nav class="ml-auto"
        ><Logout @logout="$emit('logout')"
      /></b-navbar-nav>

      <b-navbar-toggle target="navbar-toggle-collapse" />

      <b-collapse id="navbar-toggle-collapse" is-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-item @click="$emit('showSection', 'home')">
            {{ mainReport }}</b-nav-item
          >

          <b-nav-item
            v-show="applicant === 'administrator' || applicant === 'consumer'"
            @click="$emit('showSection', 'list2')"
          >
            {{ secondaryReport }}</b-nav-item
          >
          <!--register account-->
          <b-nav-item-dropdown
            v-if="applicant === 'administrator'"
            text="Register account"
          >
            <b-dropdown-item
              @click="
                $emit('showSection', 'registration'),
                  $emit('formRole', 'administrator')
              "
              >Administrator</b-dropdown-item
            >
            <b-dropdown-item
              @click="
                $emit('showSection', 'registration'),
                  $emit('formRole', 'uploader')
              "
              >Uploader</b-dropdown-item
            >
          </b-nav-item-dropdown>

          <b-nav-item
            v-if="applicant === 'uploader'"
            @click="
              $emit('showSection', 'registration'),
                $emit('formRole', 'consumer')
            "
            >Register consumer</b-nav-item
          >
          <!--modify info-->
          <b-nav-item
            v-if="applicant === 'consumer'"
            @click="
              $emit('showSection', 'modifyInfo'), $emit('formRole', 'consumer')
            "
            >Modify Info</b-nav-item
          >

          <b-nav-item-dropdown v-else text="Modify Info user">
            <b-dropdown-item
              v-if="applicant === 'administrator'"
              @click="
                $emit('showSection', 'modifyInfo'),
                  $emit('formRole', 'administrator')
              "
              >Administrator</b-dropdown-item
            >
            <b-dropdown-item
              @click="
                $emit('showSection', 'modifyInfo'),
                  $emit('formRole', 'uploader')
              "
              >Uploader</b-dropdown-item
            >
            <b-dropdown-item
              @click="
                $emit('showSection', 'modifyInfo'),
                  $emit('formRole', 'consumer')
              "
              >Consumer</b-dropdown-item
            >
          </b-nav-item-dropdown>

          <!--upload-->
          <b-nav-item
            v-if="applicant === 'uploader'"
            @click="$emit('showSection', 'load')"
            >Upload file</b-nav-item
          >
          <!--delet for all except for consumer-->
          <b-nav-item
            v-if="applicant !== 'consumer'"
            @click="$emit('showSection', 'delete')"
            >Delete</b-nav-item
          >
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import Logout from "../functions/Logout.vue";
export default {
  name: "Navigationbar",
  props: ["applicant", "mainReport", "secondaryReport"],
  components: {
    Logout,
  },
};
</script>

<style scoped>
.ml-auto {
  margin-left: auto;
}
.ml-my {
  margin-left: 20px;
}
</style>