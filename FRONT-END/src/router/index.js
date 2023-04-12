import Vue from 'vue'
import VueRouter from 'vue-router'
import Home_page from '../views/Home_page.vue'
import Consumer_page from '../views/Consumer_page.vue'
import Uploader_page from '../views/Uploader_page.vue'
import Admin_page from '../views/Admin_page.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home_page',
    component: Home_page
  },
  {
    path: '/consumer_page',
    name: 'Consumer_page',
    component: Consumer_page,
    meta:{ requiresAuth: true }
  },
  {
    path: '/uploader_page',
    name: 'Uploader_page',
    component: Uploader_page,
    meta:{ requiresAuth: true }
  },
  {
    path: '/admin_page',
    name: 'Admin_page',
    component: Admin_page,
    meta:{ requiresAuth: true }
  },
  {
    path: "*",
    redirect: "/",
  },

]

const router = new VueRouter({
  routes
})
//https://router.vuejs.org/guide/advanced/meta.html
//https://tenmilesquare.com/resources/software-development/creating-an-authentication-navigation-guard-in-vue/
router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!localStorage.getItem("JWTToken")) { //if null falsy = enter the if body
      next({
        path: '/',
        query: { redirect: to.fullPath }//reindirzzio l'utente all'url che mi aveva chiesto dopo che sarà avvenuta l'autenticazione
      })
    } else if (from.matched.some((record) => record.meta.requiresAuth)) {
      if (localStorage.getItem("Username").length === 16) {
        this.$router.push("/consumer_page");
      } else if (localStorage.getItem("Username").length === 4) {
        this.$router.push("/uploader_page");
      } else if (localStorage.getItem("Username").includes("@")) {
        this.$router.push("/admi_page");
      } else {
        this.$bvToast.toast(`ERR- Username unrecognized`,{
          title: "ERR",
          variant:"danger"
        });
      }
    } else {
      next(); 
    }
  } else {
    next(); // make sure to always call next()!
  }
});

export default router;

